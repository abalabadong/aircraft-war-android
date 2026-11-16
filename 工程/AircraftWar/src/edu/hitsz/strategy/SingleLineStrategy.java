package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;


public class SingleLineStrategy implements FireStrategy {
    
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getHeight() / 2;
        int speedX = 0;
        int speedY = 10;
        int power = 10;
        
        BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, power);
        bullets.add(bullet);
        
        return bullets;
    }
}
