package edu.hitsz.bullet;

import edu.hitsz.prop.BombObserver;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements BombObserver {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    /**
     * 观察者更新方法
     * 敌机子弹被炸弹清除
     */
    @Override
    public int update() {
        this.vanish();
        return 0;
    }
}
