package edu.hitsz.factory.prop;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.BulletPlusProp;

/**
 * 超级火力道具工厂
 */
public class BulletPlusPropFactory implements PropFactory {
    private int speedX = 0;
    private int speedY = 6;

    @Override
    public BaseProp createProp(int locationX, int locationY) {
        return new BulletPlusProp(locationX, locationY, speedX, speedY);
    }
}
