package edu.hitsz.application;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 排行榜数据访问对象
 * 使用DAO模式管理排行榜数据的持久化
 * @author hitsz
 */
public class ScoreDAO {

    // 数据文件路径
    private String filePath;

    // 记录列表
    private List<ScoreRecord> records;

    public ScoreDAO(String difficulty) {
        this.filePath = "scores_" + difficulty + ".dat";
        this.records = new ArrayList<>();
        loadFromFile();
    }

    /**
     * 添加新记录
     * @param record 记录对象
     */
    public void addRecord(ScoreRecord record) {
        records.add(record);
        Collections.sort(records);
        saveToFile();
    }

    /**
     * 删除指定排名的记录
     * @param rank 排名（从1开始）
     */
    public void deleteRecord(int rank) {
        if (rank > 0 && rank <= records.size()) {
            records.remove(rank - 1);
            saveToFile();
        }
    }

    /**
     * 获取所有记录
     * @return 记录列表
     */
    public List<ScoreRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    /**
     * 获取前N名记录
     * @param n 数量
     * @return 前N名记录列表
     */
    public List<ScoreRecord> getTopN(int n) {
        if (n >= records.size()) {
            return new ArrayList<>(records);
        }
        return new ArrayList<>(records.subList(0, n));
    }

    /**
     * 从文件加载记录
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            records = (List<ScoreRecord>) ois.readObject();
        } catch (Exception e) {
            System.out.println("加载排行榜失败: " + e.getMessage());
            records = new ArrayList<>();
        }
    }

    /**
     * 保存记录到文件
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(records);
        } catch (Exception e) {
            System.out.println("保存排行榜失败: " + e.getMessage());
        }
    }
}