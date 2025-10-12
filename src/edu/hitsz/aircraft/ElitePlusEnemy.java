package edu.hitsz.aircraft;

import edu.hitsz.strategy.ScatterShoot;

/**
 * 超级精英敌机
 * 可射击
 */
public class ElitePlusEnemy extends AbstractEnemyAircraft {

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
        this.startAngle = Math.PI / 12;
        this.shootNum = 3;
        this.direction = 1;
        this.power = 10;
        this.shootStrategy = new ScatterShoot();
        this.propNum = 1;
    }

}