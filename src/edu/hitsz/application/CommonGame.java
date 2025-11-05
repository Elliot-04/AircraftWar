package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.factory.enemy.*;

import java.awt.image.BufferedImage;

public class CommonGame extends Game {

    public CommonGame() {
        super();
        System.out.println("Common Game Start!");
    }

    @Override
    protected void setDifficultyParameters() {
        this.difficultyIncreasesOverTime = true; // 难度是否随时间增加: 是
        this.bossScoreThreshold = 700; // Boss敌机产生的分数阈值
        this.currentBossHp = 500; // Boss血量
        this.bossHpIncrease = 0; // 每次召唤不改变Boss机血量
        this.enemyMaxNumber = 5; // 敌机数量最大值
        this.cycleDuration = 600; // 敌机产生周期
        this.mobSpawnRate = 0.3; // 普通敌机产生概率 (较低)
        this.eliteSpawnRate = 0.8; // 精英敌机产生概率 (中等)
        this.bossShootCycle = 1200; // BOSS射击周期
        this.difficultyIncreaseCycle = 10000; // 难度增加周期
    }

    @Override
    protected void spawnEnemies() {
        // 普通模式：三种敌机都产生
        double randomNumber = Math.random();
        if (randomNumber < mobSpawnRate) {
            enemyFactory = new MobFactory();
        } else if (randomNumber < eliteSpawnRate) {
            enemyFactory = new EliteFactory();
        } else {
            enemyFactory = new ElitePlusFactory();
        }
        enemyAircrafts.add(enemyFactory.createEnemyAircraft());
    }

    @Override
    protected void checkAndSpawnBoss() {
        // 普通模式：Boss血量固定
        if (score - lastScore >= bossScoreThreshold && bossNum == 0) {
            if (isPlayMusic) {
                bgmThread.stopMusic();
                bgmBossThread.start();
            }
            // 使用固定的 currentBossHp
            enemyFactory = new BossFactory(this.currentBossHp);
            System.out.println("Now Boss HP:" + this.currentBossHp);
            enemyAircrafts.add(enemyFactory.createEnemyAircraft());
            bossNum++;
        }
    }

    @Override
    protected void enemyShoot() {
        for (AbstractEnemyAircraft aea : enemyAircrafts) {
            if (aea instanceof BossEnemy) {
                if (time % bossShootCycle == 0) { // Boss按周期射击
                    enemyBullets.addAll(aea.shoot());
                }
            } else {
                enemyBullets.addAll(aea.shoot());
            }
        }
    }

    @Override
    protected BufferedImage getBackgroundImage() {
        return ImageManager.BACKGROUND_IMAGE_COMMON;
    }
}
