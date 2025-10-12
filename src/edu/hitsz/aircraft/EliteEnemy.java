package edu.hitsz.aircraft;

import edu.hitsz.strategy.DirectShoot;

/**
 * 精英敌机
 * 可射击
 */
public class EliteEnemy extends AbstractEnemyAircraft {

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
        this.shootNum = 1;
        this.direction = 1;
        this.power = 10;
        this.shootStrategy = new DirectShoot();
        this.propNum = 1;
    }

}
