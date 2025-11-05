package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.factory.enemy.EliteFactory;
import edu.hitsz.factory.enemy.MobFactory;
import java.awt.image.BufferedImage;

public class EasyGame extends Game {

    public EasyGame() {
        super();
        System.out.println("Easy Game Start!");
    }

    @Override
    protected void setDifficultyParameters() {
        this.difficultyIncreasesOverTime = false; // 难度不随时间增加
        this.bossScoreThreshold = Integer.MAX_VALUE; // 无Boss敌机
        this.enemyMaxNumber = 3; // 敌机数量最大值
        this.cycleDuration = 700; // 敌机产生周期 (较慢)
        this.eliteSpawnRate = 0.3; // 精英敌机产生概率 (较低)
    }

    @Override
    protected void spawnEnemies() {
        // 简单模式只产生普通敌机和精英敌机
        double randomNumber = Math.random();
        if (randomNumber < eliteSpawnRate) {
            enemyFactory = new EliteFactory();
        } else {
            enemyFactory = new MobFactory();
        }
        enemyAircrafts.add(enemyFactory.createEnemyAircraft());
    }

    @Override
    protected void checkAndSpawnBoss() {
        // 简单模式：无Boss机
    }

    @Override
    protected void enemyShoot() {
        for (AbstractEnemyAircraft aea : enemyAircrafts) {
            enemyBullets.addAll(aea.shoot());
        }
    }

    @Override
    protected BufferedImage getBackgroundImage() {
        return ImageManager.BACKGROUND_IMAGE;
    }

    @Override
    protected void updateDifficultyOverTime() {
        // 简单模式下难度不随时间增加
    }
}
