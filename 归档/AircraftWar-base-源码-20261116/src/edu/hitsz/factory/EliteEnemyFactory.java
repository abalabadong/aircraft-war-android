package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;

/**
 * 精英敌机工厂
 */
public class EliteEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, double multiplier) {
        return new EliteEnemy(
                locationX, locationY, 0, (int) (8 * multiplier), (int) (50 * multiplier)
        );
    }
}
