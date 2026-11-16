package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 王牌敌机
 * 可射击，可左右移动，向下直射单排子弹
 * @author hitsz
 */
public class AceEnemy extends AbstractAircraft {

    // 子弹威力
    private int power = 20;
    // 子弹射击方向 (向下发射：1)
    private int direction = 1;

    public AceEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
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
        // 王牌敌机扇形弹道，每次3个子弹，x有速度
        res.add(new EnemyBullet(x - 15, y, -2, speedY, power));  // 左偏
        res.add(new EnemyBullet(x, y, 0, speedY, power));         // 正中
        res.add(new EnemyBullet(x + 15, y, 2, speedY, power));   // 右偏
        return res;
    }

    @Override
    public int getScore() {
        return 50;
    }
}