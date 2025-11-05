package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.factory.enemy.*;
import java.awt.image.BufferedImage;

public class HardGame extends Game {

    public HardGame() {
        super();
        System.out.println("Hard Game Start!");
    }

    @Override
    protected void setDifficultyParameters() {
        this.difficultyIncreasesOverTime = true; // 难度是否随时间增加: 是
        this.bossScoreThreshold = 500; // Boss敌机产生的分数阈值(较低)
        this.currentBossHp = 700; // Boss血量 (更高)
        this.bossHpIncrease = 150; // 每次召唤提升Boss机血量
        this.enemyMaxNumber = 7; // 敌机数量最大值 (更高)
        this.cycleDuration = 450; // 敌机产生周期 (更快)
        this.mobSpawnRate = 0.15; // 普通敌机产生概率 (更低)
        this.eliteSpawnRate = 0.6; // 精英敌机概率也更高
        this.bossShootCycle = 900; // BOSS敌机射击周期 (更快)
        this.difficultyIncreaseCycle = 5000; // 难度提升更快
    }

    @Override
    protected void spawnEnemies() {
        // 困难模式：高概率精英
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
        // 困难模式：Boss血量递增
        if (score - lastScore >= bossScoreThreshold && bossNum == 0) {
            if (isPlayMusic) {
                bgmThread.stopMusic();
                bgmBossThread.start();
            }

            // 使用当前的 currentBossHp
            enemyFactory = new BossFactory(this.currentBossHp);
            System.out.println("Now Boss HP:" + this.currentBossHp);
            enemyAircrafts.add(enemyFactory.createEnemyAircraft());
            bossNum++;

            // Boss增加血量
            this.currentBossHp += this.bossHpIncrease;
            System.out.println("Next Boss HP:" + this.currentBossHp);
        }
    }

    @Override
    protected void enemyShoot() {
        // 困难模式：Boss射击更频繁
        for (AbstractEnemyAircraft aea : enemyAircrafts) {
            if (aea instanceof BossEnemy) {
                if (time % bossShootCycle == 0) {
                    enemyBullets.addAll(aea.shoot());
                }
            } else {
                enemyBullets.addAll(aea.shoot());
            }
        }
    }

    @Override
    protected BufferedImage getBackgroundImage() {
        return ImageManager.BACKGROUND_IMAGE_HARD;
    }
}
