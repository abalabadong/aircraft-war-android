package edu.hitsz.factory;

import edu.hitsz.prop.AbstractBaseProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.FirePlusProp;
import edu.hitsz.prop.FireProp;
import edu.hitsz.prop.FreezeProp;

/**
 * 道具工厂（简单工厂模式）
 * 一个工厂类负责创建所有具体道具实例
 */
public class PropFactory {

    /**
     * 道具类型枚举
     */
    public enum PropType {
        BLOOD,      // 加血道具
        FIRE,       // 火力道具
        FIRE_PLUS,  // 超级火力道具
        BOMB,       // 炸弹道具
        FREEZE      // 冰冻道具
    }

    /**
     * 创建道具
     * @param type 道具类型
     * @param locationX 初始X坐标
     * @param locationY 初始Y坐标
     * @param speedX X轴速度
     * @param speedY Y轴速度
     * @return 创建好的道具对象
     */
    public static AbstractBaseProp createProp(PropType type, int locationX, int locationY, int speedX, int speedY) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case BLOOD:
                return new BloodProp(locationX, locationY, speedX, speedY);
            case FIRE:
                return new FireProp(locationX, locationY, speedX, speedY);
            case FIRE_PLUS:
                return new FirePlusProp(locationX, locationY, speedX, speedY);
            case BOMB:
                return new BombProp(locationX, locationY, speedX, speedY);
            case FREEZE:
                return new FreezeProp(locationX, locationY, speedX, speedY);
            default:
                return null;
        }
    }
}
