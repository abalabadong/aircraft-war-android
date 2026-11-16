package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;


public class SpreadStrategy implements FireStrategy {
    
    @Override
    public List<BaseBullet> fire(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getHeight() / 2;
        int speedY = 10;
        int power = 15;
        
        // 3颗子弹：左、中、右（扇形散射）
        BaseBullet bullet1 = new EnemyBullet(x - 15, y, -2, speedY, power);  // 左
        BaseBullet bullet2 = new EnemyBullet(x, y, 0, speedY, power);         // 中
        BaseBullet bullet3 = new EnemyBullet(x + 15, y, 2, speedY, power);   // 右
        bullets.add(bullet1);
        bullets.add(bullet2);
        bullets.add(bullet3);
        
        return bullets;
    }
}
