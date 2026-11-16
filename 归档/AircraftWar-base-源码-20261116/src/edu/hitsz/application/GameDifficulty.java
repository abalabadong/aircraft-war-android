package edu.hitsz.application;

/**
 * 游戏难度设置
 * 包含三种难度：简单、普通、困难
 * @author hitsz
 */
public enum GameDifficulty {

    EASY("简单", 5, 30, 30, 500, 0.3),
    NORMAL("普通", 7, 22, 22, 400, 0.6),
    HARD("困难", 10, 15, 15, 300, 1.0);

    // 难度名称
    private final String name;
    // 屏幕最大敌机数量
    private final int maxEnemyCount;

    // 敌机生成周期（游戏帧数）
    private final int enemySpawnCycle;

    // 英雄机射击周期（游戏帧数）
    private final int heroShootCycle;

    // Boss机触发阈值（分数）
    private final int bossScoreThreshold;

    // 难度系数（影响敌机血量和速度）
    private final double difficultyMultiplier;

    GameDifficulty(String name, int maxEnemyCount, int enemySpawnCycle,
                   int heroShootCycle, int bossScoreThreshold, double difficultyMultiplier) {
        this.name = name;
        this.maxEnemyCount = maxEnemyCount;
        this.enemySpawnCycle = enemySpawnCycle;
        this.heroShootCycle = heroShootCycle;
        this.bossScoreThreshold = bossScoreThreshold;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public String getName() { return name; }
    public int getMaxEnemyCount() { return maxEnemyCount; }
    public int getEnemySpawnCycle() { return enemySpawnCycle; }
    public int getHeroShootCycle() { return heroShootCycle; }
    public int getBossScoreThreshold() { return bossScoreThreshold; }
    public double getDifficultyMultiplier() { return difficultyMultiplier; }
}