package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.FireStrategy;

import java.util.LinkedList;
import java.util.List;


public class HeroAircraft extends AbstractAircraft {


    private static volatile HeroAircraft instance;


    private FireStrategy fireStrategy;


    private int fireDuration = 0;


    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.maxHp = hp;
        // 默认不使用策略（使用基础射击逻辑）
        this.fireStrategy = null;
    }


    public static HeroAircraft getInstance() {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(
                            256,  // 初始X坐标
                            700,  // 初始Y坐标
                            0,    // X速度
                            0,    // Y速度
                            100   // 初始生命值
                    );
                }
            }
        }
        return instance;
    }


    public static void reset() {
        instance = null;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }


    public void setFireStrategy(FireStrategy strategy) {
        this.fireStrategy = strategy;
    }


    public void addFireDuration(int duration) {
        this.fireDuration += duration;
    }


    public int getFireDuration() {
        return fireDuration;
    }


    public void decreaseFireDuration() {
        if (fireDuration > 0) {
            fireDuration--;
        }
        if (fireDuration == 0) {
            // 火力持续时间结束，恢复默认策略
            fireStrategy = null;
        }
    }


    public boolean hasFireSupply() {
        return fireDuration > 0;
    }


    public void increaseHp(int healAmount) {
        this.hp = Math.min(this.hp + healAmount, this.maxHp);
    }

    @Override

    public List<BaseBullet> shoot() {
        decreaseFireDuration();


        if (fireStrategy != null) {
            return fireStrategy.fire(this);
        }


        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() - 2;
        int speedX = 0;
        int speedY = -20;
        int power = 30;
        int shootNum = 1;

        BaseBullet bullet;
        for (int i = 0; i < shootNum; i++) {

            bullet = new HeroBullet(x, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }

}
