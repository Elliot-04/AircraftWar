package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 炸弹道具
 */
public class BombProp extends BaseProp{

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    // 观察者列表
    private List<BombObserver> observers = new ArrayList<>();

    // 增加观察者
    public void addObserver(BombObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    // 删除观察者
    public void removeObserver(BombObserver observer) {
        observers.remove(observer);
    }

    /**
     * 爆炸道具生效，通知所有观察者
     * @return 此次爆炸摧毁的敌机总分
     */
    public int bomb() {
        System.out.println("BombSupply active!");
        int totalScore = 0;
        for (BombObserver observer : observers) {
            totalScore += observer.update();
        }

        // 通知后清空观察者列表
        observers.clear();
        return totalScore;
    }
}
