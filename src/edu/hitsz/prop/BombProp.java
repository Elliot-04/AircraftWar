package edu.hitsz.prop;

/**
 * 炸弹道具
 */
public class BombProp extends BaseProp{

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void bomb() {
        System.out.println("BombSupply active!");
    }
}
