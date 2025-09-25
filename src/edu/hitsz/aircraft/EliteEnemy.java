package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.prop.BloodPropFactory;
import edu.hitsz.factory.prop.BombPropFactory;
import edu.hitsz.factory.prop.FirePropFactory;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.strategy.DirectShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * 精英敌机
 * 可射击
 *
 */
public class EliteEnemy extends AbstractEnemyAircraft {

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
        this.shootNum = 1;
        this.direction = 1;
        this.power = 10;
        this.shootStrategy = new DirectShoot();
        this.propNum = 1;
    }

    /**
     * 击败精英敌机随机产生道具
     * @return 产生的道具
     */
    @Override
    public List<BaseProp> generateNewProp() {
        List<BaseProp> props = new LinkedList<>();
        for (int i = 0; i < propNum; i++) {
            double isGenProp = Math.random();
            if (isGenProp < 0.4) {
                propFactory = new BloodPropFactory();
            } else if (isGenProp < 0.6) {
                propFactory = new BombPropFactory();
            } else if (isGenProp < 0.8) {
                propFactory = new FirePropFactory();
            } else {
                propFactory = null;
            }

            if (propFactory != null){
                props.add(propFactory.createProp(this.getLocationX(), this.getLocationY()));
            }
        }

        return props;
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return super.shoot();
    }
}
