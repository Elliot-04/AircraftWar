package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatterShoot;

/**
 * 火力道具
 */
public class BulletProp extends BaseProp{

    public BulletProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void scatterShoot(HeroAircraft heroAircraft){
        heroAircraft.setShootNum(3);
        heroAircraft.setStartAngle(Math.PI / 12);
        heroAircraft.setShootStrategy(new ScatterShoot());
    }

}
