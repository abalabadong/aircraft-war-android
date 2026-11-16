package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;


public class DoubleLineStrategy implements FireStrategy {
    
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getHeight() / 2;
        int speedX = 0;
        int speedY = 10;
        int power = 10;
        

        BaseBullet bullet1 = new EnemyBullet(x - 10, y, speedX, speedY, power);
        BaseBullet bullet2 = new EnemyBullet(x + 10, y, speedX, speedY, power);
        bullets.add(bullet1);
        bullets.add(bullet2);
        
        return bullets;
    }
}
