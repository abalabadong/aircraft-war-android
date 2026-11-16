package edu.hitsz.application;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 排行榜记录
 * 存储一条游戏记录信息
 * @author hitsz
 */
public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {

    private static final long serialVersionUID = 1L;

    // 玩家名称
    private String playerName;

    // 得分
    private int score;

    // 游戏难度
    private String difficulty;

    // 记录时间
    private Date recordTime;

    public ScoreRecord(String playerName, int score, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
        this.recordTime = new Date();
    }

    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public String getDifficulty() { return difficulty; }
    public Date getRecordTime() { return recordTime; }

    @Override
    public int compareTo(ScoreRecord other) {
        // 按分数降序排列
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s | %d分 | %s | %s",
                playerName, score, difficulty, sdf.format(recordTime));
    }
}