package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;


public class CircleStrategy implements FireStrategy {
    
    // 环射子弹数量
    private static final int BULLET_COUNT = 20;
    
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getHeight() / 2;
        int speed = 8;
        int power = 12;
        
        // 20颗子弹，均匀分布在360度
        for (int i = 0; i < BULLET_COUNT; i++) {
            double angle = 2 * Math.PI * i / BULLET_COUNT;
            int speedX = (int) (speed * Math.cos(angle));
            int speedY = (int) (speed * Math.sin(angle));
            
            BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, power);
            bullets.add(bullet);
        }
        
        return bullets;
    }
}
