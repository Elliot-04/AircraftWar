package edu.hitsz.aircraft;

import edu.hitsz.factory.prop.PropFactory;
import edu.hitsz.prop.BaseProp;
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

    public int getScore() {
        return score;
    }

    public abstract List<BaseProp> generateNewProp();
}
