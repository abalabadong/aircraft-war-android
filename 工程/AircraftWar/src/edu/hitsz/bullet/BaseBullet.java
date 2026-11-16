package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

import edu.hitsz.supply.PropObserver;

public abstract class BaseBullet extends AbstractFlyingObject implements PropObserver {

    private int power = 0;

    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    private boolean frozen = false;

    @Override
    public void forward() {
        if (!frozen) {
            super.forward();
            // 判定 x 轴出界
            if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
                vanish();
            }
            // 判定 y 轴出界
            if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
                vanish();
            }else if (locationY <= 0){
                vanish();
            }
        }
    }

    @Override
    public void onBomb() {
        // 炸弹效果：消失
        vanish();
    }

    @Override
    public void onFreeze() {
        // 冰冻效果：静止5秒后恢复
        frozen = true;
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frozen = false;
        }).start();
    }

    public int getPower() {
        return power;
    }
}
