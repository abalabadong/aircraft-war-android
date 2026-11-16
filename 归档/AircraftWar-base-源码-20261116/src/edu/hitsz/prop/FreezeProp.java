package edu.hitsz.prop;

import edu.hitsz.aircraft.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 冰冻道具
 * Boss敌机不受影响，王牌敌机减速，
 * 精锐和精英敌机短暂冻结后恢复，普通敌机永久冻结
 * @author hitsz
 */
public class FreezeProp extends AbstractBaseProp {

    // 冰冻效果持续时间（毫秒）
    private static final long FREEZE_DURATION = 3000;

    // 存储敌机原始速度
    private static final Map<AbstractAircraft, int[]> originalSpeeds = new HashMap<>();

    public FreezeProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 激活冰冻道具
     * @param enemyAircrafts 敌机列表
     */
    public void activate(List<AbstractAircraft> enemyAircrafts) {
        System.out.println("FreezeSupply active!");

        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy) {
                // Boss不受影响
                continue;
            } else if (enemy instanceof AceEnemy) {
                // 王牌敌机减速50%
                saveOriginalSpeed(enemy);
                enemy.speedX = (int) (enemy.speedX * 0.5);
                enemy.speedY = (int) (enemy.speedY * 0.5);
            } else if (enemy instanceof ElitePlusEnemy || enemy instanceof EliteEnemy) {
                // 精锐和精英敌机短暂冻结
                saveOriginalSpeed(enemy);
                enemy.speedX = 0;
                enemy.speedY = 0;
            } else if (enemy instanceof MobEnemy) {
                // 普通敌机永久冻结（速度设为0）
                saveOriginalSpeed(enemy);
                enemy.speedX = 0;
                enemy.speedY = 0;
            }
        }

        // 启动恢复线程
        new Thread(() -> {
            try {
                Thread.sleep(FREEZE_DURATION);
                restoreSpeeds(enemyAircrafts);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        vanish();
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        // 简化版本
        System.out.println("FreezeSupply active!");
        vanish();
    }

    private void saveOriginalSpeed(AbstractAircraft enemy) {
        if (!originalSpeeds.containsKey(enemy)) {
            originalSpeeds.put(enemy, new int[]{enemy.speedX, enemy.speedY});
        }
    }

    private void restoreSpeeds(List<AbstractAircraft> enemyAircrafts) {
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (originalSpeeds.containsKey(enemy)) {
                int[] speeds = originalSpeeds.get(enemy);
                // 普通敌机不恢复（永久冻结）
                if (!(enemy instanceof MobEnemy)) {
                    enemy.speedX = speeds[0];
                    enemy.speedY = speeds[1];
                }
            }
        }
        originalSpeeds.clear();
        System.out.println("Freeze effect ended (except MobEnemy).");
    }
}