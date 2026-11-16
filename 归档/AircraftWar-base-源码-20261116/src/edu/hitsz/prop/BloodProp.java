package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 加血道具
 * 可使英雄机恢复一定血量，但不能超过英雄机初始的最大血量
 * @author hitsz
 */
public class BloodProp extends AbstractBaseProp {

    // 恢复血量值
    private int increaseHp = 30;

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        int currentHp = heroAircraft.getHp();
        int maxHp = heroAircraft.maxHp;
        int newHp = Math.min(currentHp + increaseHp, maxHp);
        // 通过增加生命值的方式实现（需要反射或直接修改，这里简化处理）
        // 实际应该在HeroAircraft中提供增加生命值的方法
        heroAircraft.hp = newHp;
        System.out.println("BloodSupply active!");
        vanish();
    }
}