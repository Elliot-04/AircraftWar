package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 散射模式
 */
public class ScatterShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new ArrayList<>();
        double startAngle = aircraft.getStartAngle();
        double angle = -startAngle;
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 3;
        int speedY = aircraft.getSpeedY() + aircraft.getDirection() * 5;
        BaseBullet bullet;
        int power = aircraft.getPower();
        int shootNum = aircraft.getShootNum();
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            int speedX = (int) (speedY * Math.tan(angle));
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(x , y, speedX , speedY-2, power);
            } else {
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX + aircraft.getSpeedX(), speedY, power);
            }
            angle += startAngle;
            res.add(bullet);
        }
        return res;
    }
}
