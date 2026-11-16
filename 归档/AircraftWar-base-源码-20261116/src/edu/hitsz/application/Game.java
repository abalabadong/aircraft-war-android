package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.factory.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * 游戏主面板，游戏启动
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    //调度器, 用于定时任务调度
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractBaseProp> props;

    //游戏难度
    private GameDifficulty difficulty;

    //屏幕中出现的敌机最大数量
    private int enemyMaxNumber;

    //敌机生成周期
    private double enemySpawnCycle;
    private int enemySpawnCounter = 0;

    //英雄机和敌机射击周期
    private double shootCycle;
    private int shootCounter = 0;

    //当前玩家分数
    private int score = 0;

    //Boss机触发阈值
    private int bossScoreThreshold;
    //是否已召唤Boss
    private boolean bossSpawned = false;

    //游戏结束标志
    private boolean gameOverFlag = false;

    //游戏开始时间
    private long gameStartTime;

    //难度递增计数器
    private int difficultyIncreaseCounter = 0;

    //排行榜DAO
    private ScoreDAO scoreDAO;

    // 工厂对象
    private MobEnemyFactory mobEnemyFactory;
    private EliteEnemyFactory eliteEnemyFactory;
    private ElitePlusEnemyFactory elitePlusEnemyFactory;
    private AceEnemyFactory aceEnemyFactory;
    private BossEnemyFactory bossEnemyFactory;

    public Game(GameDifficulty difficulty) {
        this.difficulty = difficulty;
        this.scoreDAO = new ScoreDAO(difficulty.getName());

        // 根据难度初始化参数
        this.enemyMaxNumber = difficulty.getMaxEnemyCount();
        this.enemySpawnCycle = difficulty.getEnemySpawnCycle();
        this.shootCycle = difficulty.getHeroShootCycle();
        this.bossScoreThreshold = difficulty.getBossScoreThreshold();

        // 初始化工厂
        this.mobEnemyFactory = new MobEnemyFactory();
        this.eliteEnemyFactory = new EliteEnemyFactory();
        this.elitePlusEnemyFactory = new ElitePlusEnemyFactory();
        this.aceEnemyFactory = new AceEnemyFactory();
        this.bossEnemyFactory = new BossEnemyFactory(difficulty);

        heroAircraft = HeroAircraft.getInstance(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 100);
        heroAircraft.setOriginalShootCycle(shootCycle);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);
        this.gameStartTime = System.currentTimeMillis();
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 检查火力道具效果是否过期
                heroAircraft.checkSupplyExpiration();

                // 普通和困难模式下随时间增加难度
                if (difficulty != GameDifficulty.EASY) {
                    increaseDifficultyOverTime();
                }

                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    spawnEnemy();
                }

                // 检查是否召唤Boss
                checkBossSpawn();

                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
                // 道具移动
                propsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行
        timer.schedule(task, 0, timeInterval);
    }

    /**
     * 随时间增加难度
     */
    private void increaseDifficultyOverTime() {
        difficultyIncreaseCounter++;
        // 每30秒增加一次难度
        if (difficultyIncreaseCounter >= 750) { // 750 * 40ms = 30秒
            difficultyIncreaseCounter = 0;
            enemySpawnCycle = Math.max(5, enemySpawnCycle * 0.95);
            System.out.println("Difficulty increased! Enemy spawn cycle: " + enemySpawnCycle);
        }
    }

    /**
     * 生成敌机
     * 使用工厂模式创建不同类型的敌机
     */
    private void spawnEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            double random = Math.random();
            int locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - 50));
            int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
            double multiplier = difficulty.getDifficultyMultiplier();

            AbstractAircraft enemy = null;

            if (random < 0.4) {
                // 普通敌机 (40%)
                enemy = mobEnemyFactory.createEnemy(locationX, locationY, multiplier);
            } else if (random < 0.7) {
                // 精英敌机 (30%)
                enemy = eliteEnemyFactory.createEnemy(locationX, locationY, multiplier);
            } else if (random < 0.9) {
                // 精锐敌机 (20%)
                enemy = elitePlusEnemyFactory.createEnemy(locationX, locationY, multiplier);
            } else {
                // 王牌敌机 (10%)
                enemy = aceEnemyFactory.createEnemy(locationX, locationY, multiplier);
            }

            if (enemy != null) {
                enemyAircrafts.add(enemy);
            }
        }
    }

    /**
     * 检查是否召唤Boss
     * 使用工厂模式创建Boss敌机
     */
    private void checkBossSpawn() {
        if (!bossSpawned && score >= bossScoreThreshold) {
            bossSpawned = true;
            int locationX = Main.WINDOW_WIDTH / 2;
            int locationY = 50;
            // 使用工厂创建Boss，multiplier在BossEnemyFactory内部根据difficulty处理
            AbstractAircraft boss = bossEnemyFactory.createEnemy(locationX, locationY, 1.0);
            enemyAircrafts.add(boss);
            System.out.println("Boss appeared!");
        }
    }

    private void shootAction() {
        shootCounter++;
        double currentCycle = heroAircraft.getShootCycle();
        if (shootCounter >= currentCycle) {
            shootCounter = 0;
            //英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
            // 敌机射击
            for (AbstractAircraft enemy : enemyAircrafts) {
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

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractBaseProp prop : props) {
            prop.forward();
        }
    }

    private void crashCheckAction() {
        // 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) continue;
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        score += enemyAircraft.getScore();
                        // Boss被击落时重置boss生成标志，并重新计算分数阈值
                        if (enemyAircraft instanceof BossEnemy) {
                            bossSpawned = false;
                            // 重新计算Boss触发阈值，当前分数+300
                            bossScoreThreshold = score + 300;
                            System.out.println("Next Boss threshold: " + bossScoreThreshold);
                        }
                        // 非普通敌机坠毁时随机生成道具
                        if (!(enemyAircraft instanceof MobEnemy)) {
                            generateProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY(), enemyAircraft);
                        }
                    }
                }
                // 英雄机与敌机相撞
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具
        for (AbstractBaseProp prop : props) {
            if (prop.notValid()) continue;
            if (heroAircraft.crash(prop)) {
                activateProp(prop);
            }
        }
    }

    /**
     * 激活道具
     */
    private void activateProp(AbstractBaseProp prop) {
        if (prop instanceof BloodProp) {
            ((BloodProp) prop).activate(heroAircraft);
        } else if (prop instanceof FireProp) {
            ((FireProp) prop).activate(heroAircraft);
        } else if (prop instanceof FirePlusProp) {
            ((FirePlusProp) prop).activate(heroAircraft);
        } else if (prop instanceof BombProp) {
            score = ((BombProp) prop).activate(heroAircraft, enemyAircrafts, enemyBullets, score);
        } else if (prop instanceof FreezeProp) {
            ((FreezeProp) prop).activate(enemyAircrafts);
        }
    }

    /**
     * 生成道具
     * 使用简单工厂模式创建道具
     * 精锐敌机：掉落除冰冻外的所有道具
     * 王牌敌机：掉落全部道具
     */
    private void generateProp(int locationX, int locationY, AbstractAircraft enemy) {
        double random = Math.random();
        PropFactory.PropType propType = null;

        if (enemy instanceof AceEnemy) {
            // 王牌敌机掉落全部5种道具
            if (random < 0.2) {
                propType = PropFactory.PropType.BLOOD;
            } else if (random < 0.4) {
                propType = PropFactory.PropType.FIRE;
            } else if (random < 0.6) {
                propType = PropFactory.PropType.FIRE_PLUS;
            } else if (random < 0.8) {
                propType = PropFactory.PropType.BOMB;
            } else {
                propType = PropFactory.PropType.FREEZE;
            }
        } else if (enemy instanceof ElitePlusEnemy) {
            // 精锐敌机掉落除冰冻外的4种道具
            if (random < 0.25) {
                propType = PropFactory.PropType.BLOOD;
            } else if (random < 0.5) {
                propType = PropFactory.PropType.FIRE;
            } else if (random < 0.75) {
                propType = PropFactory.PropType.FIRE_PLUS;
            } else {
                propType = PropFactory.PropType.BOMB;
            }
        }

        AbstractBaseProp prop = PropFactory.createProp(propType, locationX, locationY, 0, 5);
        if (prop != null) {
            props.add(prop);
        }
    }

    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    private void checkResultAction() {
        if (heroAircraft.getHp() <= 0) {
            timer.cancel();
            gameOverFlag = true;
            System.out.println("Game Over! Final Score: " + score);
            showGameOverDialog();
        }
    }

    /**
     * 显示游戏结束对话框
     */
    private void showGameOverDialog() {
        SwingUtilities.invokeLater(() -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // 询问是否保存记录
            int result = JOptionPane.showConfirmDialog(parent,
                    "游戏结束！你的得分是: " + score + "\n是否保存到排行榜?",
                    "游戏结束", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                String playerName = JOptionPane.showInputDialog(parent,
                        "请输入你的名字:", "玩家");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    ScoreRecord record = new ScoreRecord(playerName.trim(), score, difficulty.getName());
                    scoreDAO.addRecord(record);

                    // 显示排行榜
                    RankingBoard rankingBoard = new RankingBoard(parent, difficulty.getName());
                    rankingBoard.setVisible(true);
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 绘制子弹
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        // 绘制敌机
        paintImageWithPositionRevised(g, enemyAircrafts);
        // 绘制道具
        paintImageWithPositionRevised(g, props);
        // 绘制英雄机
        g.drawImage(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        // 绘制得分和生命值
        paintScoreAndLife(g);
    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) return;
        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            if (image != null) {
                g.drawImage(image,
                        object.getLocationX() - image.getWidth() / 2,
                        object.getLocationY() - image.getHeight() / 2, null);
            }
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
        y = y + 20;
        g.drawString("DIFFICULTY: " + this.difficulty.getName(), x, y);
    }
}