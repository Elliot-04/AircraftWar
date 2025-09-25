package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 加血道具
 */
public class BloodProp extends BaseProp{

    /**
     * 加血量
     */
    private int blood = 20;


    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 给英雄机加血
     * @param heroAircraft 英雄机
     */
    public void addBlood(HeroAircraft heroAircraft) {
        heroAircraft.increaseHp(this.blood);
    }


}
