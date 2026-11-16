package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 火力道具
 * 提升英雄机射速30%，持续5秒
 * @author hitsz
 */
public class FireProp extends AbstractBaseProp {

    // 持续时间5秒
    private static final long DURATION = 5000;

    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        System.out.println("FireSupply active!");
        heroAircraft.activateFireSupply(DURATION);
        vanish();
    }
}