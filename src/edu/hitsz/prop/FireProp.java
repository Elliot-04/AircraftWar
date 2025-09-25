package edu.hitsz.prop;

/**
 * 火力道具
 */
public class FireProp extends BaseProp{

    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void fire() {
        System.out.println("FireSupply active!");
    }

}
