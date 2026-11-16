package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 精锐敌机
 * 可射击，可左右移动，向下直射单排子弹
 * @author hitsz
 */
public class ElitePlusEnemy extends AbstractAircraft {

    // 子弹威力
    private int power = 15;
    // 子弹射击方向 (向下发射：1)
    private int direction = 1;

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedY = this.getSpeedY() + direction * 5;
        // 精锐敌机向下射出两排子弹
        res.add(new EnemyBullet(x - 10, y, 0, speedY, power));
        res.add(new EnemyBullet(x + 10, y, 0, speedY, power));
        return res;
    }

    @Override
    public int getScore() {
        return 30;
    }
}