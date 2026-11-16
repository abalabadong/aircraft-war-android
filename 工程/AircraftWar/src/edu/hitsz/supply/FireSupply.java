package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.HeroSpreadStrategy;


public class FireSupply extends AbstractSupply {

    public FireSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY, 100);
    }

    @Override
    public void effect() {
        // 火力道具生效：切换为散射策略
        System.out.println("FireSupply active! 切换到散射弹道");
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.setFireStrategy(new HeroSpreadStrategy());
        hero.addFireDuration(7);  // 持续6次射击
        vanish();
    }
}
