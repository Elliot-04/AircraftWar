package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * 超级精英敌机工厂
 */
public class ElitePlusFactory implements EnemyFactory {

    private int locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
    private int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
    private int speedX = 2;
    private int speedY = 5;
    private int hp = 90;
    private int score = 200;

    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        return new ElitePlusEnemy(locationX,locationY,speedX,speedY,hp,score);
    }
}
