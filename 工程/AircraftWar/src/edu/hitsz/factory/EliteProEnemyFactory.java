package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.application.Main;

public class EliteProEnemyFactory implements EnemyFactory {
    
    @Override
    public AbstractEnemy createEnemy() {
        return new EliteProEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - 60)),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                1,
                6,
                100
        );
    }
}
