package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;

/**
 * 敌机工厂接口
 * 工厂模式：定义创建敌机的接口，让子类决定实例化哪个类
 */
public interface EnemyFactory {
    /**
     * 创建敌机
     * @param locationX 初始X坐标
     * @param locationY 初始Y坐标
     * @param multiplier 难度倍数（影响速度和血量）
     * @return 创建好的敌机对象
     */
    AbstractAircraft createEnemy(int locationX, int locationY, double multiplier);
}
