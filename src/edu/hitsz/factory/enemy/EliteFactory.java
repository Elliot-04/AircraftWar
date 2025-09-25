package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * 精英敌机工厂
 */
public class EliteFactory implements EnemyFactory {
    private int locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
    private int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
    private int speedX = 0;
    private int speedY = 5;
    private int hp = 50;
    private int score = 100;

    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        return new EliteEnemy(locationX, locationY, speedX, speedY, hp, score);
    }
}
