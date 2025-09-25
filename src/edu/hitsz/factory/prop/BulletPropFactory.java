package edu.hitsz.factory.prop;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.BulletProp;

/**
 * 火力道具工厂
 */
public class BulletPropFactory implements PropFactory {
    private int speedX = 0;
    private int speedY = 6;

    @Override
    public BaseProp createProp(int locationX, int locationY) {
        return new BulletProp(locationX, locationY, speedX, speedY);
    }

}
