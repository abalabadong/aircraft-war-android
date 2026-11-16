package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dao.ScoreDao;
import edu.hitsz.dao.ScoreDaoImpl;
import edu.hitsz.dao.ScoreRecord;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.ElitePlusEnemyFactory;
import edu.hitsz.factory.EliteProEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.SupplyFactory;
import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.ui.ScoreBoardPanel;
import edu.hitsz.audio.AudioManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;


public class Game extends JPanel {

    private int backGroundTop = 0;


    private final Timer timer;

    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractSupply> supplies;


    private int enemyMaxNumber = 5;


    private final EnemyFactory mobEnemyFactory = new MobEnemyFactory();
    private final EnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    private final EnemyFactory elitePlusEnemyFactory = new ElitePlusEnemyFactory();
    private final EnemyFactory eliteProEnemyFactory = new EliteProEnemyFactory();
    private final EnemyFactory bossEnemyFactory = new BossEnemyFactory();


    protected double enemySpawnCycle  =  20;
    private int enemySpawnCounter = 0;


    protected double shootCycle = 20;
    private int shootCounter = 0;


    private int score = 0;


    private boolean gameOverFlag = false;


    private boolean bossSpawned = false;


    private final int bossSpawnScoreThreshold = 500;

    // DAO实例用于管理得分记录
    private final ScoreDao scoreDao;

    private String difficulty;

    public Game(String difficulty) {
        this.difficulty = difficulty;
        heroAircraft = HeroAircraft.getInstance();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        supplies = new LinkedList<>();
        this.scoreDao = new ScoreDaoImpl(difficulty);
        new HeroController(this, heroAircraft);
        this.timer = new Timer("game-action-timer", true);
        // 根据难度设置对应地图背景
        switch (difficulty.toLowerCase()){
            case "easy":
            case "e":
                ImageManager.setBackground("src/images/bg.jpg"); // bg1
                enemyMaxNumber = 5;
                break;
            case "normal":
            case "n":
                ImageManager.setBackground("src/images/bg3.jpg"); // bg3
                enemyMaxNumber = 7;
                break;
            case "hard":
            case "h":
                ImageManager.setBackground("src/images/bg5.jpg"); // bg5
                enemyMaxNumber = 10;
                break;
            default:
                // 使用默认
                enemyMaxNumber = 5;
                break;
        }
    }

    // 保持原有无参构造，兼容旧代码
    public Game() {
        this("normal");
    }


    public void action() {

        // 启动背景音乐（循环播放）
        try {
            AudioManager.playBgm("src/videos/bgm.wav", true);
        } catch (Exception e) {
            System.err.println("无法播放背景音乐: " + e.getMessage());
        }


        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;

                    if (enemyAircrafts.size() < enemyMaxNumber) {

                        if (!bossSpawned && score >= bossSpawnScoreThreshold) {

                            enemyAircrafts.add(bossEnemyFactory.createEnemy());
                            bossSpawned = true;
                            System.out.println("Boss敌机出现！");
                            // 切换到Boss专属背景音乐
                            try {
                                AudioManager.stopBgm();
                                AudioManager.playBossBgm("src/videos/bgm_boss.wav", true);
                            } catch (Exception e) {
                                System.err.println("无法播放Boss背景音乐: " + e.getMessage());
                            }
                        } else {

                            double random = Math.random();
                            if (random < 0.4) {

                                enemyAircrafts.add(mobEnemyFactory.createEnemy());
                            } else if (random < 0.7) {

                                enemyAircrafts.add(eliteEnemyFactory.createEnemy());
                            } else if (random < 0.85) {

                                enemyAircrafts.add(elitePlusEnemyFactory.createEnemy());
                            } else {

                                enemyAircrafts.add(eliteProEnemyFactory.createEnemy());
                            }
                        }
                    }
                }

                shootAction();

                bulletsMoveAction();

                aircraftsMoveAction();

                suppliesMoveAction();

                crashCheckAction();

                postProcessAction();

                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task,0,timeInterval);

    }



    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            //英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
            // 敌机射击
            for (AbstractAircraft enemy : enemyAircrafts) {
                if (enemy.notValid()) {
                    continue;
                }
                enemyBullets.addAll(enemy.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }
    
    private void suppliesMoveAction() {
        for (AbstractSupply supply : supplies) {
            supply.forward();
            // 道具移动到底部则消失
            if (supply.getLocationY() >= Main.WINDOW_HEIGHT) {
                supply.vanish();
            }
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }



    private void crashCheckAction() {
        // 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    // 播放子弹击中音效
                    try {
                        AudioManager.playEffect("src/videos/bullet_hit.wav");
                    } catch (Exception e) {
                        System.err.println("无法播放子弹命中音效: " + e.getMessage());
                    }
                    if (enemyAircraft.notValid()) {
                        // 获得分数
                        if (enemyAircraft instanceof AbstractEnemy) {
                            score += ((AbstractEnemy) enemyAircraft).getScore();

                            // 概率掉落道具
                            double dropProbability = ((AbstractEnemy) enemyAircraft).getDropProbability();
                            if (Math.random() < dropProbability) {
                                // 根据敌机类型生成不同道具
                                AbstractSupply supply = generateSupplyByEnemyType(
                                        (AbstractEnemy) enemyAircraft,
                                        enemyAircraft.getLocationX(),
                                        enemyAircraft.getLocationY()
                                );
                                if (supply != null) {
                                    supplies.add(supply);
                                }
                                
                                // Boss敌机被击毁时，额外掉落2个道具（共3个）
                                if (enemyAircraft instanceof BossEnemy) {
                                    System.out.println("Boss敌机被击毁！掉落3个道具！");
                                    for (int i = 0; i < 2; i++) {
                                        AbstractSupply extraSupply = generateRandomSupply(
                                                enemyAircraft.getLocationX(),
                                                enemyAircraft.getLocationY()
                                        );
                                        if (extraSupply != null) {
                                            supplies.add(extraSupply);
                                        }
                                    }
                                    // Boss被击毁，停止Boss背景音乐并恢复普通背景音乐
                                    try {
                                        AudioManager.stopBossBgm();
                                        AudioManager.playBgm("src/videos/bgm.wav", true);
                                    } catch (Exception e) {
                                        System.err.println("切换回普通背景音乐失败: " + e.getMessage());
                                    }
                                }
                            }
                        } else {
                            score += 10;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (AbstractSupply supply : supplies) {
            if (supply.notValid()) {
                continue;
            }
            if (heroAircraft.crash(supply)) {
                // 观察者模式注册
                if (supply instanceof edu.hitsz.supply.BombSupply) {
                    edu.hitsz.supply.BombSupply bomb = (edu.hitsz.supply.BombSupply) supply;
                    for (AbstractAircraft enemy : enemyAircrafts) {
                        if (enemy instanceof edu.hitsz.supply.PropObserver) {
                            bomb.addObserver((edu.hitsz.supply.PropObserver) enemy);
                        }
                    }
                    for (BaseBullet bullet : enemyBullets) {
                        if (bullet instanceof edu.hitsz.supply.PropObserver) {
                            bomb.addObserver((edu.hitsz.supply.PropObserver) bullet);
                        }
                    }
                } else if (supply instanceof edu.hitsz.supply.FreezeSupply) {
                    edu.hitsz.supply.FreezeSupply freeze = (edu.hitsz.supply.FreezeSupply) supply;
                    for (AbstractAircraft enemy : enemyAircrafts) {
                        if (enemy instanceof edu.hitsz.supply.PropObserver) {
                            freeze.addObserver((edu.hitsz.supply.PropObserver) enemy);
                        }
                    }
                    for (BaseBullet bullet : enemyBullets) {
                        if (bullet instanceof edu.hitsz.supply.PropObserver) {
                            freeze.addObserver((edu.hitsz.supply.PropObserver) bullet);
                        }
                    }
                }
                supply.effect();
                supply.vanish();
                // 播放获取道具音效
                try {
                    AudioManager.playEffect("src/videos/get_supply.wav");
                } catch (Exception e) {
                    System.err.println("无法播放获取道具音效: " + e.getMessage());
                }
            }
        }

    }
    

    private AbstractSupply generateSupplyByEnemyType(AbstractEnemy enemy, int x, int y) {
        double random = Math.random();

        // 精锐敌机：掉落4种道具（不含冰冻）
        if (enemy instanceof edu.hitsz.aircraft.ElitePlusEnemy) {
            if (random < 0.3) {
                return SupplyFactory.createSupply("blood", x, y);
            } else if (random < 0.6) {
                return SupplyFactory.createSupply("fire", x, y);
            } else if (random < 0.85) {
                return SupplyFactory.createSupply("firePlus", x, y);
            } else {
                return SupplyFactory.createSupply("bomb", x, y);
            }
        }
        // 王牌敌机：掉落全部5种道具
        else if (enemy instanceof edu.hitsz.aircraft.EliteProEnemy) {
            if (random < 0.25) {
                return SupplyFactory.createSupply("blood", x, y);
            } else if (random < 0.45) {
                return SupplyFactory.createSupply("fire", x, y);
            } else if (random < 0.65) {
                return SupplyFactory.createSupply("firePlus", x, y);
            } else if (random < 0.85) {
                return SupplyFactory.createSupply("bomb", x, y);
            } else {
                return SupplyFactory.createSupply("freeze", x, y);
            }
        }
        // 普通敌机和精英敌机：掉落3种道具（加血、火力、超级火力）
        else {
            if (random < 0.4) {
                return SupplyFactory.createSupply("blood", x, y);
            } else if (random < 0.7) {
                return SupplyFactory.createSupply("fire", x, y);
            } else {
                return SupplyFactory.createSupply("firePlus", x, y);
            }
        }
    }



    private AbstractSupply generateRandomSupply(int x, int y) {
        String[] supplyTypes = {"blood", "fire", "firePlus", "bomb", "freeze"};
        double random = Math.random();
        int index;
        
        if (random < 0.25) {
            index = 0;  // blood
        } else if (random < 0.45) {
            index = 1;  // fire
        } else if (random < 0.65) {
            index = 2;  // firePlus
        } else if (random < 0.85) {
            index = 3;  // bomb
        } else {
            index = 4;  // freeze
        }
        
        return SupplyFactory.createSupply(supplyTypes[index], x, y);
    }


    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supplies.removeIf(AbstractFlyingObject::notValid);
    }

    private void showScoreBoard() {
        JFrame frame = new JFrame("得分排行榜");
        frame.setSize(350, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ScoreBoardPanel panel = new ScoreBoardPanel(scoreDao, difficulty, score);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void checkResultAction(){
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 取消定时器并终止所有调度任务
            gameOverFlag = true;
            System.out.println("Game Over!");
            // 停止所有音乐并播放游戏结束音效
            try {
                AudioManager.stopBgm();
                AudioManager.stopBossBgm();
                AudioManager.playEffect("src/videos/game_over.wav");
            } catch (Exception e) {
                System.err.println("播放游戏结束音效失败: " + e.getMessage());
            }
            showScoreBoard();
        }
    };

    //***********************
    //      Paint 各部分
    //***********************

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // 绘制道具
        paintImageWithPositionRevised(g, supplies);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}