package edu.hitsz.prop;

import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 所有种类道具的抽象父类：
 * 火力、炸弹、加血
 * @author hitsz
 */
public abstract class BaseProp extends AbstractFlyingObject {
    /**
     * @param locationX 道具的x坐标
     * @param locationY 道具的y坐标
     * @param speedX 道具的x速度
     * @param speedY 道具的y速度
     */
    public BaseProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public BaseProp() {
    }

    @Override
    public void forward(){
        super.forward();
        //向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    /**
     * 英雄机使用道具完毕后恢复弹道
     * @param heroAircraft 英雄机
     */
    public void directShoot(HeroAircraft heroAircraft){
        heroAircraft.setShootNum(1);
        heroAircraft.setShootStrategy(new DirectShoot());
    }
}
