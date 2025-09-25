package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;
/**
 * 直射模式
 */
public class DirectShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + aircraft.getDirection() * 8;
        int shootNum = aircraft.getShootNum();
        int power = aircraft.getPower();
        BaseBullet bullet;

        if (aircraft instanceof HeroAircraft) {
            for (int i = 0; i < shootNum; i++){
                // 英雄机发射英雄机子弹
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
                res.add(bullet);
            }
        } else if (aircraft instanceof AbstractEnemyAircraft){
            // 敌机发射敌机子弹
            speedY -= 3; // 敌机子弹速度较慢
            for (int i = 0; i < shootNum; i++){
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
                res.add(bullet);
            }
        }
        return res;
    }
}
