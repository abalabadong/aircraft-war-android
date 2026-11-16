package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * Boss敌机
 * 悬浮于界面上方，仅左右移动，可射击
 * @author hitsz
 */
public class BossEnemy extends AbstractAircraft {

    // 子弹威力
    private int power = 30;
    // 子弹射击方向 (向下发射：1)
    private int direction = 1;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        // Boss悬浮于上方，仅左右移动，不向下移动
        locationX += speedX;
        // 横向边界检测，超出则反向
        if (locationX <= 0 || locationX >= edu.hitsz.application.Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = direction * 5;
        BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, power);
        res.add(bullet);
        return res;
    }

    @Override
    public int getScore() {
        return 100;
    }
}