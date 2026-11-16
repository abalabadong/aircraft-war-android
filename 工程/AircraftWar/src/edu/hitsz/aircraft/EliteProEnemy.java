package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.strategy.SpreadStrategy;


import edu.hitsz.supply.PropObserver;

public class EliteProEnemy extends AbstractEnemy implements PropObserver {

    public EliteProEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 40, 0.9);

        setFireStrategy(new SpreadStrategy());
    }

    private boolean slowed = false;

    @Override
    public void forward() {
        if (!slowed) {
            super.forward();
            // 左右移动
            if (locationX <= getWidth() / 2 || locationX >= Main.WINDOW_WIDTH - getWidth() / 2) {
                speedX = -speedX;
            }
        } else {
            // 减速状态下移动
            locationX += speedX / 2;
            locationY += speedY / 2;
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
        // 冰冻效果：减速5秒后恢复
        if (!slowed) {
            slowed = true;
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                slowed = false;
            }).start();
        }
    }
}
