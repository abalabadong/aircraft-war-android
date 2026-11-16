package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 超级火力道具
 * 大幅提升英雄机射速50%，持续5秒
 * @author hitsz
 */
public class FirePlusProp extends AbstractBaseProp {

    // 持续时间5秒
    private static final long DURATION = 5000;

    public FirePlusProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        System.out.println("FirePlusSupply active!");
        heroAircraft.activateFirePlusSupply(DURATION);
        vanish();
    }
}
