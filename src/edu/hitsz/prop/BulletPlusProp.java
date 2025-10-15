package edu.hitsz.prop;

import edu.hitsz.strategy.RingShoot;
import edu.hitsz.aircraft.HeroAircraft;

/**
 * 超级火力道具
 */
public class BulletPlusProp extends BaseProp {

    public BulletPlusProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void ringShoot(HeroAircraft heroAircraft) {
        heroAircraft.setShootNum(20);
        heroAircraft.setStartAngle(2 * Math.PI / 20);
        heroAircraft.setShootStrategy(new RingShoot());
    }
}
