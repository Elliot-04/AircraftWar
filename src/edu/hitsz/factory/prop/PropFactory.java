package edu.hitsz.factory.prop;

import edu.hitsz.prop.BaseProp;

/**
 * 道具工厂接口
 */
public interface PropFactory {
    BaseProp createProp(int locationX, int locationY);
}
