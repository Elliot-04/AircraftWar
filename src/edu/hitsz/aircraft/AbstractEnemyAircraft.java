package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.prop.BloodPropFactory;
import edu.hitsz.factory.prop.BombPropFactory;
import edu.hitsz.factory.prop.BulletPropFactory;
import edu.hitsz.factory.prop.PropFactory;
import edu.hitsz.prop.BaseProp;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有种类敌机的抽象父类：
 * 敌机（BOSS, ELITE, MOB）
 * @author xzb
 */
public abstract class AbstractEnemyAircraft extends AbstractAircraft {
    protected int score;
    protected int propNum; // 掉落道具数量
    protected PropFactory propFactory;

    public AbstractEnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp);
        this.score = score;
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    public int getScore() {
        return score;
    }

    public List<BaseProp> generateNewProp() {
        List<BaseProp> props = new ArrayList<>();
        for (int i = 0; i < propNum; i++) {
            double isGenProp = Math.random();
            if (isGenProp < 0.4) {
                propFactory = new BloodPropFactory();
            } else if (isGenProp < 0.6) {
                propFactory = new BombPropFactory();
            } else if (isGenProp < 0.8) {
                propFactory = new BulletPropFactory();
            } else {
                propFactory = null;
            }

            if (propFactory != null) {
                props.add(propFactory.createProp(this.getLocationX(), this.getLocationY()));
            }
        }

        return props;
    }
}
