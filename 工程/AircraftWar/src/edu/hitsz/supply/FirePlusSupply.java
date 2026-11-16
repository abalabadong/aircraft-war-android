package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.HeroCircleStrategy;


public class FirePlusSupply extends AbstractSupply {

    public FirePlusSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY, 80);
    }

    @Override
    public void effect() {
        // 超级火力道具生效：切换为环射策略
        System.out.println("FirePlusSupply active! 切换到环射弹道");
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.setFireStrategy(new HeroCircleStrategy());
        hero.addFireDuration(7);  // 持续6次射击
        vanish();
    }
}
