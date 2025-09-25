package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * 普通敌机工厂
 */
public class MobFactory implements EnemyFactory {
    private int locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
    private int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
    private int speedX = 0;
    private int speedY = 5;
    private int hp = 30;
    private int score = 10;

    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        return new MobEnemy(locationX, locationY, speedX, speedY, hp, score);
    }
}
