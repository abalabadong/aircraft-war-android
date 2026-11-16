package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.strategy.CircleStrategy;

import edu.hitsz.supply.PropObserver;

public class BossEnemy extends AbstractEnemy implements PropObserver {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 100, 1.0);

        setFireStrategy(new CircleStrategy());
    }

    @Override
    public void forward() {
        locationX += speedX;
        if (locationX <= getWidth() / 2 || locationX >= Main.WINDOW_WIDTH - getWidth() / 2) {
            speedX = -speedX;
        }
    }

    @Override
    public void onBomb() {
        // Boss不受炸弹影响
    }

    @Override
    public void onFreeze() {
        // Boss不受冰冻影响
    }
}
