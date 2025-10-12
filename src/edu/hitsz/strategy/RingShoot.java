package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 环射模式
 */
public class RingShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 2;
        double startAngle = -aircraft.getStartAngle();
        double angle = -startAngle;
        int speed = aircraft.getSpeedY() + aircraft.getDirection() * 10;
        List<BaseBullet> res = new ArrayList<>();
        int power = aircraft.getPower();
        int shootNum = aircraft.getShootNum();
        BaseBullet bullet;
        for(int i = 0; i < shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            int speedX = (int) (speed * Math.sin(angle));
            int speedY = (int) (speed * Math.cos(angle));
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(x, y, speedX, speedY, power);
            } else {
                bullet = new EnemyBullet(x, y, speedX, speedY, power);
            }
            angle += startAngle;
            res.add(bullet);
        }
        return res;
    }
}
