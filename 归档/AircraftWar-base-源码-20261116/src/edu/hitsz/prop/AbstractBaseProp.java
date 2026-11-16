package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 所有道具的抽象父类
 * @author hitsz
 */
public abstract class AbstractBaseProp extends AbstractFlyingObject {

    public AbstractBaseProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    /**
     * 道具生效方法
     * @param heroAircraft 英雄机
     */
    public abstract void activate(HeroAircraft heroAircraft);
}