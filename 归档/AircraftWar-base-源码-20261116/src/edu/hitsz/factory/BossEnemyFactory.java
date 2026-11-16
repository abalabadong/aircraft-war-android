package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.GameDifficulty;

/**
 * Boss敌机工厂
 */
public class BossEnemyFactory implements EnemyFactory {
    
    private GameDifficulty difficulty;
    
    public BossEnemyFactory(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, double multiplier) {
        // Boss血量根据难度设定
        int bossHp = (difficulty == GameDifficulty.HARD) ? 400 : 300;
        int speedX = 2;
        return new BossEnemy(locationX, locationY, speedX, 0, bossHp);
    }
}
