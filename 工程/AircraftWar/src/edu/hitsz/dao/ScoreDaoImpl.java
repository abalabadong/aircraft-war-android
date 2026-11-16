package edu.hitsz.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 得分数据访问对象实现类 (DAO Implementation)
 * 实现文件读写和排行榜功能
 */
public class ScoreDaoImpl implements ScoreDao {
    private List<ScoreRecord> scoreRecords;
    private String filePath;

    public ScoreDaoImpl(String difficulty) {
        // 根据难度级别设置不同的文件路径
        this.filePath = "scores_" + difficulty + ".txt";
        scoreRecords = new ArrayList<>();
        loadScores();
    }

    /**
     * 从文件加载得分记录
     */
    private void loadScores() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String playerName = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    String recordTime = parts[2];
                    scoreRecords.add(new ScoreRecord(playerName, score, recordTime));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("加载得分记录时出错: " + e.getMessage());
        }
    }

    /**
     * 保存得分记录到文件
     */
    private void saveScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (ScoreRecord record : scoreRecords) {
                writer.println(record.toString());
            }
        } catch (IOException e) {
            System.err.println("保存得分记录时出错: " + e.getMessage());
        }
    }

    @Override
    public void addScore(ScoreRecord record) {
        scoreRecords.add(record);
        saveScores();
    }

    @Override
    public List<ScoreRecord> getAllScores() {
        // 按分数降序排序
        List<ScoreRecord> sortedRecords = new ArrayList<>(scoreRecords);
        sortedRecords.sort(Comparator.comparingInt(ScoreRecord::getScore).reversed());
        return sortedRecords;
    }

    @Override
    public void deleteScore(int index) {
        List<ScoreRecord> sortedRecords = getAllScores();
        if (index >= 0 && index < sortedRecords.size()) {
            ScoreRecord toDelete = sortedRecords.get(index);
            // 从原始列表中移除
            scoreRecords.removeIf(record ->
                record.getPlayerName().equals(toDelete.getPlayerName()) &&
                record.getScore() == toDelete.getScore() &&
                record.getRecordTime().equals(toDelete.getRecordTime())
            );
            saveScores();
        }
    }

    @Override
    public void removeScore(ScoreRecord record) {
        scoreRecords.removeIf(r ->
            r.getPlayerName().equals(record.getPlayerName()) &&
            r.getScore() == record.getScore()
        );
        saveScores();
    }

    @Override
    public void displayLeaderboard() {
        List<ScoreRecord> sortedRecords = getAllScores();
        System.out.println("\n========== 得分排行榜 ==========");
        if (sortedRecords.isEmpty()) {
            System.out.println("暂无记录");
        } else {
            for (int i = 0; i < sortedRecords.size(); i++) {
                ScoreRecord record = sortedRecords.get(i);
                System.out.printf("第%d名: %s, %d, %s%n",
                    i + 1,
                    record.getPlayerName(),
                    record.getScore(),
                    record.getRecordTime());
            }
        }
        System.out.println("================================\n");
    }
}