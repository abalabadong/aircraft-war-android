package edu.hitsz.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 得分记录值对象 (Value Object)
 * 用于封装单局游戏的得分记录信息
 */
public class ScoreRecord {
    private String playerName;
    private int score;
    private String recordTime;

    public ScoreRecord(String playerName, int score, String recordTime) {
        this.playerName = playerName;
        this.score = score;
        this.recordTime = recordTime;
    }

    public ScoreRecord(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        // 使用当前时间作为记录时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        this.recordTime = now.format(formatter);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return playerName + "," + score + "," + recordTime;
    }
}
