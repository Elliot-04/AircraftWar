package edu.hitsz.prop;

/**
 * 炸弹道具观察者接口
 */
public interface BombObserver {
    /**
     * 炸弹道具爆炸时，观察者调用此方法
     * @return 若观察者被摧毁，返回其分数，否则返回0
     */
    int update();
}
