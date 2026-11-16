package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.strategy.SingleLineStrategy;


import edu.hitsz.supply.PropObserver;

public class EliteEnemy extends AbstractEnemy implements PropObserver {

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 20, 0.9);

        setFireStrategy(new SingleLineStrategy());
    }

    private boolean frozen = false;

    @Override
    public void forward() {
        if (!frozen) {
            super.forward();
        }
        if (locationY >= Main.WINDOW_HEIGHT) {
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
        // 冰冻效果：静止4秒后恢复
        frozen = true;
        new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frozen = false;
        }).start();
    }
}
