package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HeroAircraft单元测试类
 * 测试英雄机类的3个方法，包括父类方法
 */
class HeroAircraftTest {
    
    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        // 重置单例以确保每次测试都从干净状态开始
        HeroAircraft.reset();
        heroAircraft = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        HeroAircraft.reset();
    }

    /**
     * 测试1: 单例模式 - 测试getInstance()方法
     * 确保多次调用getInstance返回同一实例
     */
    @Test
    @DisplayName("测试单例模式 - getInstance返回同一实例")
    void testGetInstance() {
        HeroAircraft anotherInstance = HeroAircraft.getInstance();
        assertSame(heroAircraft, anotherInstance, "两次getInstance()应返回同一实例");
    }

    /**
     * 测试2: 射击方法 - 测试shoot()方法
     * 验证射击方法能正确生成子弹
     */
    @Test
    @DisplayName("测试射击方法 - shoot()生成单发子弹")
    void testShoot() {
        // 使用默认射击逻辑（无策略）
        List<BaseBullet> bullets = heroAircraft.shoot();
        
        assertNotNull(bullets, "射击返回的子弹列表不应为null");
        assertEquals(1, bullets.size(), "默认射击应生成1发子弹");
        
        BaseBullet bullet = bullets.get(0);
        assertNotNull(bullet, "子弹对象不应为null");
        assertEquals(30, bullet.getPower(), "默认子弹威力应为30");
        assertTrue(bullet.getSpeedY() < 0, "子弹Y方向速度应为负值（向上飞行）");
    }

    /**
     * 测试3: 生命值管理 - 测试decreaseHp()方法（继承自AbstractAircraft父类）
     * 验证生命值减少功能，以及生命值为0时飞机失效
     */
    @Test
    @DisplayName("测试减少生命值 - decreaseHp()父类方法")
    void testDecreaseHp() {
        int initialHp = heroAircraft.getHp();
        
        // 测试1: 减少20点生命值
        heroAircraft.decreaseHp(20);
        assertEquals(initialHp - 20, heroAircraft.getHp(), "减少20点HP后应正确更新");
        
        // 测试2: 再次减少30点
        heroAircraft.decreaseHp(30);
        assertEquals(initialHp - 50, heroAircraft.getHp(), "再次减少30点HP后应正确更新");
        
        // 测试3: 将HP减少到0，验证飞机失效
        heroAircraft.decreaseHp(heroAircraft.getHp()); // 减少到0
        assertEquals(0, heroAircraft.getHp(), "HP应减少到0");
        assertTrue(heroAircraft.notValid(), "HP为0时飞机应标记为无效");
    }
}
