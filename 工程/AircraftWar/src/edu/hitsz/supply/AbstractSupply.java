package edu.hitsz.supply;

import edu.hitsz.basic.AbstractFlyingObject;


public abstract class AbstractSupply extends AbstractFlyingObject {
    
    // 道具效果持续时间
    protected int effectTime;

    public AbstractSupply(int locationX, int locationY, int speedX, int speedY, int effectTime) {
        super(locationX, locationY, speedX, speedY);
        this.effectTime = effectTime;
    }


    public abstract void effect();
}
