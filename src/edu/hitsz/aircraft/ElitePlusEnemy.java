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

    /**
     * 重写观察者更新方法
     * 受到炸弹影响：血量减少 60
     */
    @Override
    public int update() {
        this.decreaseHp(60);
        // 如果扣血后被摧毁，则返回分数
        if (this.notValid()) {
            return this.getScore();
        }
        return 0; // 未被摧毁，不加分
    }
}