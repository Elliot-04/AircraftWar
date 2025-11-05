package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * BOSS敌机工厂
 */
public class BossFactory implements EnemyFactory {

    private int locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
    private int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
    private int speedX = 5;
    private int speedY = 0;
    private int hp;
    private int score = 300;

    public BossFactory(int hp) {
        this.hp = hp;
    }

    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        return new BossEnemy(locationX, locationY, speedX, speedY, hp, score);
    }
}
