package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.Main;


public class EliteEnemyFactory implements EnemyFactory {
    
    @Override
    public AbstractEnemy createEnemy() {
        return new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - 60)),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                8,
                50
        );
    }
}
