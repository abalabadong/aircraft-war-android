package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AceEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * 炸弹道具
 * Boss敌机不受影响，对王牌敌机造成伤害，
 * 清除界面上其它敌机和所有敌机子弹，英雄机可获得所有坠毁敌机的分数
 * @author hitsz
 */
public class BombProp extends AbstractBaseProp {

    // 对王牌敌机造成的伤害
    private static final int ACE_DAMAGE = 50;

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {

    }

    /**
     * 激活炸弹道具
     * @param heroAircraft 英雄机
     * @param enemyAircrafts 敌机列表
     * @param enemyBullets 敌机子弹列表
     * @param score 当前分数（用于累加）
     * @return 增加后的分数
     */
    public int activate(HeroAircraft heroAircraft,
                        List<AbstractAircraft> enemyAircrafts,
                        List<BaseBullet> enemyBullets,
                        int score) {
        System.out.println("BombSupply active!");

        // 清除所有敌机子弹
        for (BaseBullet bullet : enemyBullets) {
            bullet.vanish();
        }

        // 处理所有敌机
        int bonusScore = 0;
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy) {
                // Boss不受影响
                continue;
            } else if (enemy instanceof AceEnemy) {
                // 王牌敌机受到伤害
                enemy.decreaseHp(ACE_DAMAGE);
                if (enemy.notValid()) {
                    bonusScore += 50;
                }
            } else {
                // 其他敌机直接清除
                enemy.vanish();
                bonusScore += enemy.getScore();
            }
        }

        System.out.println("Bomb cleared enemies! Bonus score: " + bonusScore);
        vanish();
        return score + bonusScore;
    }

}