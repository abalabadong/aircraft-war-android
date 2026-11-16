package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AceEnemy;

/**
 * 王牌敌机工厂
 */
public class AceEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, double multiplier) {
        int speedX = Math.random() < 0.5 ? -3 : 3;
        return new AceEnemy(
                locationX, locationY, speedX, (int) (10 * multiplier), (int) (80 * multiplier)
        );
    }
}
