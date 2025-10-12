package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BaseProp;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractEnemyAircraft {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
    }

    @Override
    public List<BaseBullet> shoot() {
        return new ArrayList<>();
    }

    @Override
    public List<BaseProp> generateNewProp() {
        return new ArrayList<>(); // 普通敌机不掉落道具
    }
}
