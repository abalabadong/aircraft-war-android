package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * 使用单例模式确保全局唯一实例
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    private volatile static HeroAircraft instance;

    //每次射击发射子弹数量
    private int shootNum = 1;

    //子弹威力
    private int power = 30;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;

    //火力道具持续时间（毫秒）
    private long fireSupplyEndTime = 3000;
    private long firePlusSupplyEndTime = 3000;

    //原始射速（用于恢复）
    private double originalShootCycle = 20;
    private double currentShootCycle = 20;

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }

    /**
     * 激活火力道具（提升射速）
     * @param duration 持续时间（毫秒）
     */
    public void activateFireSupply(long duration) {
        fireSupplyEndTime = System.currentTimeMillis() + duration;
        currentShootCycle = originalShootCycle * 0.7; // 射速提升30%
        System.out.println("FireSupply active! Shoot cycle: " + currentShootCycle);
    }

    /**
     * 激活超级火力道具（大幅提升射速）
     * @param duration 持续时间（毫秒）
     */
    public void activateFirePlusSupply(long duration) {
        firePlusSupplyEndTime = System.currentTimeMillis() + duration;
        currentShootCycle = originalShootCycle * 0.5; // 射速提升50%
        System.out.println("FirePlusSupply active! Shoot cycle: " + currentShootCycle);
    }

    /**
     * 检查道具效果是否过期
     */
    public void checkSupplyExpiration() {
        long now = System.currentTimeMillis();
        if (now > firePlusSupplyEndTime && now > fireSupplyEndTime) {
            // 所有火力道具都过期了，恢复原始射速
            if (currentShootCycle != originalShootCycle) {
                currentShootCycle = originalShootCycle;
                System.out.println("Fire supply expired. Shoot cycle restored: " + currentShootCycle);
            }
        } else if (now > firePlusSupplyEndTime && fireSupplyEndTime > now) {
            // 超级火力过期但普通火力还在
            currentShootCycle = originalShootCycle * 0.7;
        }
    }

    public double getShootCycle() {
        return currentShootCycle;
    }

    public void setOriginalShootCycle(double cycle) {
        this.originalShootCycle = cycle;
        this.currentShootCycle = cycle;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction * 5;
        BaseBullet bullet;
        for (int i = 0; i < shootNum; i++) {
            bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }

    @Override
    public int getScore() {
        return 0; // 英雄机不被击落，返回0分
    }
}