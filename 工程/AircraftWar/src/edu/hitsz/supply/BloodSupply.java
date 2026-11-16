package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;


public class BloodSupply extends AbstractSupply {
    
    // 恢复的血量
    private int healAmount = 30;

    public BloodSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY, 0);
    }

    @Override
    public void effect() {
        // 加血道具生效：恢复英雄机血量
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.increaseHp(healAmount);
        System.out.println("BloodSupply active! Heal: " + healAmount);
        vanish();
    }
}
