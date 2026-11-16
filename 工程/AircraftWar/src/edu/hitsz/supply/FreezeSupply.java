package edu.hitsz.supply;

public class FreezeSupply extends AbstractPropSubject {

    public FreezeSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect() {
        System.out.println("FreezeSupply active!");
        notifyObservers();
        vanish();
    }

    @Override
    public void notifyObservers() {
        for (PropObserver observer : observers) {
            observer.onFreeze();
        }
    }
}