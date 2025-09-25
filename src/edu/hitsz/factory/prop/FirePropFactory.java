package edu.hitsz.factory.prop;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.FireProp;

/**
 * 火力道具工厂
 */
public class FirePropFactory implements PropFactory {
    private int speedX = 0;
    private int speedY = 6;

    @Override
    public BaseProp createProp(int locationX, int locationY) {
        return new FireProp(locationX, locationY, speedX, speedY);
    }

}
