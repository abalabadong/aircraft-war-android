package edu.hitsz.dao;

import java.util.List;

/**
 * 得分数据访问对象接口 (DAO Interface)
 * 定义所有数据访问相关方法
 */
public interface ScoreDao {
    /**
     * 添加新的得分记录
     * @param record 得分记录
     */
    void addScore(ScoreRecord record);

    /**
     * 获取所有得分记录（按分数降序排序）
     * @return 得分记录列表
     */
    List<ScoreRecord> getAllScores();

    /**
     * 删除指定索引的得分记录
     * @param index 记录索引
     */
    void deleteScore(int index);

    /**
     * 删除指定得分记录（通过对象匹配）
     */
    void removeScore(ScoreRecord record);

    /**
     * 显示排行榜（控制台打印）
     */
    void displayLeaderboard();
}