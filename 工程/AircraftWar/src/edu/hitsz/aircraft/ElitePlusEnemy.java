package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.strategy.DoubleLineStrategy;


import edu.hitsz.supply.PropObserver;

public class ElitePlusEnemy extends AbstractEnemy implements PropObserver {

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 30, 0.9);

        setFireStrategy(new DoubleLineStrategy());
    }

    private boolean frozen = false;

    @Override
    public void forward() {
        if (!frozen) {
            super.forward();
            if (locationX <= getWidth() / 2 || locationX >= Main.WINDOW_WIDTH - getWidth() / 2) {
                speedX = -speedX;
            }
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
        // 冰冻效果：静止3秒后恢复
        frozen = true;
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frozen = false;
        }).start();
    }
}
