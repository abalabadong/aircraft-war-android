package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;

/**
 * 精锐敌机工厂
 */
public class ElitePlusEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, double multiplier) {
        int speedX = Math.random() < 0.5 ? -2 : 2;
        return new ElitePlusEnemy(
                locationX, locationY, speedX, (int) (8 * multiplier), (int) (60 * multiplier)
        );
    }
}
