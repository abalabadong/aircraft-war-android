package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;


public class HeroCircleStrategy implements FireStrategy {
    
    // 环射子弹数量
    private static final int BULLET_COUNT = 20;
    
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() - 20;
        int speed = 15;
        int power = 30;
        
        // 20颗子弹，均匀分布在上半圆（PI弧度）
        for (int i = 0; i < BULLET_COUNT; i++) {
            double angle = Math.PI + Math.PI * i / (BULLET_COUNT - 1);  // 从PI到2PI（上半圆）
            int speedX = (int) (speed * Math.cos(angle));
            int speedY = (int) (speed * Math.sin(angle));
            
            BaseBullet bullet = new HeroBullet(x, y, speedX, speedY, power);
            bullets.add(bullet);
        }
        
        return bullets;
    }
}
