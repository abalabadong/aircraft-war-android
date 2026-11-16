package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.Main;


public class BossEnemyFactory implements EnemyFactory {
    
    @Override
    public AbstractEnemy createEnemy() {
        return new BossEnemy(
                Main.WINDOW_WIDTH / 2,
                50,
                2,
                0,
                500
        );
    }
}
