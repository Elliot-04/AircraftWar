package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.enemy.*;
import edu.hitsz.prop.*;
import edu.hitsz.swing.ScoreTable;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    private int backGroundTop = 0;

    // 音效开关
    public static boolean isPlayMusic = true;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    // 音效专用线程池
    protected final ExecutorService audioExecutor;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    protected final List<AbstractEnemyAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    private final List<BaseProp> props;
    protected EnemyFactory enemyFactory;
    protected int bossNum = 0;

    /**
     * 部分音乐线程路径
     */
    protected final MusicThread bgmThread = new MusicThread("src/videos/bgm.wav");
    protected final MusicThread bgmBossThread = new MusicThread("src/videos/bgm_boss.wav");

    // 用于鼠标输入的线程安全坐标 "邮箱"
    protected final AtomicInteger heroDesiredLocationX;
    protected final AtomicInteger heroDesiredLocationY;

    /**
     * 屏幕中出现的敌机最大数量
     */
    protected int enemyMaxNumber;

    /**
     * 当前得分
     */
    protected int score = 0;
    public int getScore() {
        return score;
    }

    /**
     * 当前时刻
     */
    protected int time = 0;

    // 线程使用子弹道具标志
    private long shootMode = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration = 600;
    private int cycleTime = 0;
    protected int lastScore = 0;

    /** Boss射击周期 */
    protected int bossShootCycle;
    /** Boss机当前血量 */
    protected int currentBossHp;
    /** Boss机血量增长值 (困难模式) */
    protected int bossHpIncrease;
    /** 普通敌机产生概率 */
    protected double mobSpawnRate;
    /** 精英敌机产生概率 */
    protected double eliteSpawnRate;
    /** Boss机产生的得分阈值 */
    protected int bossScoreThreshold;
    /** 难度是否随时间增加 */
    protected boolean difficultyIncreasesOverTime;
    /** 难度增加周期 */
    protected int difficultyIncreaseCycle;
    private long lastPropToken = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    public Game() {
        heroAircraft = HeroAircraft.getInstance();
        props = new CopyOnWriteArrayList<>();
        enemyAircrafts = new CopyOnWriteArrayList<>();
        heroBullets = new CopyOnWriteArrayList<>();
        enemyBullets = new CopyOnWriteArrayList<>();

        // 用英雄机的初始位置来初始化邮箱
        heroDesiredLocationX = new AtomicInteger(heroAircraft.getLocationX());
        heroDesiredLocationY = new AtomicInteger(heroAircraft.getLocationY());

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());
        this.audioExecutor = Executors.newCachedThreadPool(
                new BasicThreadFactory.Builder().namingPattern("game-audio-thread").daemon(true).build()
        );
        this.setDifficultyParameters();

        //启动英雄机鼠标监听
        new HeroController(this, heroDesiredLocationX, heroDesiredLocationY);
    }

    /**
     * 设置各难度下的初始化参数
     */
    protected abstract void setDifficultyParameters();

    /**
     * 实现如何生成普通和精英敌机
     */
    protected abstract void spawnEnemies();

    /**
     * 实现如何生成Boss敌机
     */
    protected abstract void checkAndSpawnBoss();

    /**
     * 实现敌机的射击逻辑
     */
    protected abstract void enemyShoot();

    /**
     * 获取背景图片
     */
    protected abstract BufferedImage getBackgroundImage();

    /**
     * 难度随时间增加的逻辑
     * 子类可以重写此方法提供更复杂的难度增加
     */
    protected void updateDifficultyOverTime() {
        if (!difficultyIncreasesOverTime || time % difficultyIncreaseCycle != 0 || time == 0) {
            return;
        }

        // 默认的难度提升：减小敌机产生周期、减小英雄机和敌机的射击周期、提高敌机数量上限
        if (this.cycleDuration > 300) {
            this.cycleDuration -= 50;
            System.out.println("Difficulty Increased! New cycleDuration: " + this.cycleDuration);
        }
        if (this.enemyMaxNumber < 10) {
            this.enemyMaxNumber++;
            System.out.println("Difficulty Increased! New max enemies: " + this.enemyMaxNumber);
        }
    }

    /**
     * 从线程安全的邮箱中读取坐标，并更新英雄机位置
     */
    private void updateHeroLocation() {
        heroAircraft.setLocation(heroDesiredLocationX.get(), heroDesiredLocationY.get());
    }

    /**
     * 播放一个短音效
     * 使用专用的音效线程池来避免线程爆炸
     * @param filename 音频文件路径
     */
    private void playSoundEffect(String filename) {
        if (isPlayMusic) {
            audioExecutor.execute(new MusicThread(filename));
        }
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        if (isPlayMusic) {
            bgmThread.start();
        }
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            time += timeInterval;

            updateHeroLocation();

            // 生成BOSS敌机
            checkAndSpawnBoss();

            // 难度随时间增长
            updateDifficultyOverTime();

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 产生敌机
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    spawnEnemies();
                }
                // 飞机射出子弹
                shootAction();
            }

            //道具移动
            propsMoveAction();
            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction();
            // 撞击检测
            crashCheckAction();
            // 后处理
            postProcessAction();
            //每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0 && !gameOverFlag) {
                // 游戏结束
                gameOverFlag = true;
                executorService.shutdown();
                audioExecutor.shutdown();
                heroAircraft.increaseHp(100);

                // 停止音乐
                if (isPlayMusic) {
                    if (bgmBossThread.isAlive()) bgmBossThread.stopMusic();
                    bgmThread.stopMusic();
                    playSoundEffect("src/videos/game_over.wav");
                }

                System.out.println("Game Over!");

                SwingUtilities.invokeLater(() -> {
                    ScoreTable scoreTable = new ScoreTable();
                    Main.cardPanel.add(scoreTable.getMainPanel());
                    Main.cardLayout.last(Main.cardPanel);
                    scoreTable.inputName();
                });
            }
        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        enemyShoot();
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractEnemyAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (BaseProp prop : props) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemyAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    playSoundEffect("src/videos/bullet_hit.wav");
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        if (enemyAircraft instanceof BossEnemy && bossNum == 1) {
                            bossNum--;
                            lastScore = score;
                            if (isPlayMusic) {
                                bgmBossThread.stopMusic();
                                bgmThread.start();
                            }
                        }
                        score += enemyAircraft.getScore();
                        props.addAll(enemyAircraft.generateNewProp());
                    }
                }
                // 英雄机与敌机相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (BaseProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                playSoundEffect("src/videos/get_supply.wav");
                if (prop instanceof BloodProp bloodProp) {
                    bloodProp.addBlood(heroAircraft);
                } else if (prop instanceof BombProp bombProp) {
                    playSoundEffect("src/videos/bomb_explosion.wav");
                    // 注册所有当前在场敌机作为观察者
                    for (AbstractEnemyAircraft aea: enemyAircrafts) {
                        bombProp.addObserver(aea);
                    }
                    // 注册所有当前在场敌机子弹作为观察者
                    for (BaseBullet bullet: enemyBullets) {
                        if (bullet instanceof BombObserver) {
                            bombProp.addObserver((BombObserver) bullet);
                        }
                    }
                    // 炸弹生效，并更新获取的总分
                    int bombScore = bombProp.bomb();
                    score += bombScore;
                } else if (prop instanceof BulletProp bulletProp) {
                    // 创建一个唯一的令牌来标记这次道具激活
                    final long currentPropToken = ++lastPropToken;
                    shootMode = currentPropToken; // 记录当前激活的令牌
                    // 立即激活道具
                    bulletProp.scatterShoot(heroAircraft);
                    // 安排一个取消激活的任务到同一个 executorService
                    Runnable deactivationTask = () -> {
                        // 检查令牌是否不变，如果不是，说明在3秒内又捡了新道具，此时不应覆盖新道具的效果
                        if (shootMode == currentPropToken) {
                            bulletProp.directShoot(heroAircraft);
                            shootMode = 0;
                        }
                    };
                    // 安排这个任务在3秒后执行
                    executorService.schedule(deactivationTask, 3, TimeUnit.SECONDS);
                } else if (prop instanceof BulletPlusProp bulletPlusProp) {
                    // 创建一个唯一的令牌来标记这次道具激活
                    final long currentPropToken = ++lastPropToken;
                    shootMode = currentPropToken; // 记录当前激活的令牌
                    // 立即激活道具
                    bulletPlusProp.ringShoot(heroAircraft);
                    // 安排一个取消激活的任务到同一个 executorService
                    Runnable deactivationTask = () -> {
                        // 检查令牌是否不变，如果不是，说明在3秒内又捡了新道具，此时不应覆盖新道具的效果
                        if (shootMode == currentPropToken) {
                            bulletPlusProp.directShoot(heroAircraft);
                            shootMode = 0;
                        }
                    };
                    // 安排这个任务在3秒后执行
                    executorService.schedule(deactivationTask, 3, TimeUnit.SECONDS);
                }
                prop.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 根据难度选择背景图
        BufferedImage backgroundImage = getBackgroundImage();

        // 绘制背景,图片滚动
        g.drawImage(backgroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, props);
        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
