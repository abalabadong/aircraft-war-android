package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;


public class NoFireStrategy implements FireStrategy {
    
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        return new LinkedList<>();
    }
}
