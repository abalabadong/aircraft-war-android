package edu.hitsz.supply;

import java.util.ArrayList;
import java.util.List;

// 继承道具父类，并且调用 5 参构造
public abstract class AbstractPropSubject extends AbstractSupply {

    protected List<PropObserver> observers = new ArrayList<>();

    // 必须写构造方法，传递 5 个参数给 AbstractSupply
    public AbstractPropSubject(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY, 0);
    }

    public void addObserver(PropObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PropObserver observer) {
        observers.remove(observer);
    }

    public abstract void notifyObservers();
}