package edu.hitsz.aircraft;

import edu.hitsz.strategy.RingShoot;

/**
 * BOSS敌机
 * 可射击
 */
public class BossEnemy extends AbstractEnemyAircraft{

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
        this.shootNum = 20;
        this.startAngle = 2 * Math.PI/shootNum;
        this.direction = 1;
        this.power = 10;
        this.shootStrategy = new RingShoot();
        this.propNum = 3;
    }

    /**
     * 重写观察者更新方法
     * Boss敌机不受炸弹影响
     */
    @Override
    public int update() {
        // 不执行任何操作，返回0分
        return 0;
    }
}
