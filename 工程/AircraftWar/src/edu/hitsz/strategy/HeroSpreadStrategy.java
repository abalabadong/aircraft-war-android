package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;


public class HeroSpreadStrategy implements FireStrategy {
    
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() - 20;
        int power = 30;
        
        // 3颗子弹：左、中、右（向上散射）
        BaseBullet bullet1 = new HeroBullet(x - 15, y, -3, -20, power);  // 左
        BaseBullet bullet2 = new HeroBullet(x, y, 0, -20, power);         // 中
        BaseBullet bullet3 = new HeroBullet(x + 15, y, 3, -20, power);   // 右
        bullets.add(bullet1);
        bullets.add(bullet2);
        bullets.add(bullet3);
        
        return bullets;
    }
}
