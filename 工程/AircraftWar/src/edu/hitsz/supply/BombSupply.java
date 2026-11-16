package edu.hitsz.supply;

import edu.hitsz.audio.AudioManager;

public class BombSupply extends AbstractPropSubject {

    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect() {
        System.out.println("BombSupply active!");
        notifyObservers();

        try {
            AudioManager.playEffect("src/videos/bomb_explosion.wav");
        } catch (Exception e) {
            System.err.println("音效播放失败");
        }

        vanish();
    }

    @Override
    public void notifyObservers() {
        for (PropObserver observer : observers) {
            observer.onBomb();
        }
    }
}