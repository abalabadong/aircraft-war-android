package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.NoFireStrategy;


import edu.hitsz.supply.PropObserver;



public class MobEnemy extends AbstractEnemy implements PropObserver {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 10, 0.0);

        setFireStrategy(new NoFireStrategy());
    }

    private boolean frozen = false;

    @Override
    public void forward() {
        if (!frozen) {
            super.forward();
        }
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public void onBomb() {
        // 炸弹效果：直接坠毁
        vanish();
    }

    @Override
    public void onFreeze() {
        // 冰冻效果：永久静止
        frozen = true;
    }
}
