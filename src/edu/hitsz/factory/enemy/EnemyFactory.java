package edu.hitsz.factory.enemy;

import edu.hitsz.aircraft.AbstractEnemyAircraft;

/**
 * 敌机工厂接口
 */
public interface EnemyFactory {
    AbstractEnemyAircraft createEnemyAircraft();
}
