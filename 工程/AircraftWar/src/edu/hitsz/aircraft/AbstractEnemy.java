package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.FireStrategy;

import java.util.List;


public abstract class AbstractEnemy extends AbstractAircraft {


    protected int score;


    protected double dropProbability;


    protected FireStrategy fireStrategy;

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, double dropProbability) {
        super(locationX, locationY, speedX, speedY, hp);
        this.maxHp = hp;
        this.score = score;
        this.dropProbability = dropProbability;
    }

    public int getScore() {
        return score;
    }

    public double getDropProbability() {
        return dropProbability;
    }

    public void setFireStrategy(FireStrategy strategy) {
        this.fireStrategy = strategy;
    }

    @Override
    public List<BaseBullet> shoot() {

        if (fireStrategy == null) {
            return new java.util.LinkedList<>();
        }
        return fireStrategy.fire(this);
    }
}
