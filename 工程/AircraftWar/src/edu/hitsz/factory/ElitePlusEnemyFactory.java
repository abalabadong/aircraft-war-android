package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.application.Main;


public class ElitePlusEnemyFactory implements EnemyFactory {
    
    @Override
    public AbstractEnemy createEnemy() {
        return new ElitePlusEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - 60)),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                1,
                7,
                70
        );
    }
}
