package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 排行榜界面
 * 显示当前难度的历史排行榜
 * @author hitsz
 */
public class RankingBoard extends JDialog {

    private ScoreDAO scoreDAO;
    private JTextArea textArea;

    public RankingBoard(JFrame parent, String difficulty) {
        super(parent, "排行榜 - " + difficulty, true);
        this.scoreDAO = new ScoreDAO(difficulty);

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 标题
        JLabel titleLabel = new JLabel(difficulty + " 难度排行榜", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 58, 95));
        add(titleLabel, BorderLayout.NORTH);

        // 排行榜内容
        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBackground(new Color(247, 250, 252));

        refreshRanking();

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.addActionListener(e -> refreshRanking());
        buttonPanel.add(refreshBtn);

        JButton closeBtn = new JButton("关闭");
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 刷新排行榜显示
     */
    public void refreshRanking() {
        List<ScoreRecord> records = scoreDAO.getTopN(10);
        StringBuilder sb = new StringBuilder();
        sb.append("名次\t玩家\t\t分数\t\t时间\n");
        sb.append("─".repeat(60)).append("\n");

        for (int i = 0; i < records.size(); i++) {
            ScoreRecord record = records.get(i);
            sb.append(String.format("%d\t%-10s\t%6d\t\t%s\n",
                    i + 1,
                    record.getPlayerName(),
                    record.getScore(),
                    new java.text.SimpleDateFormat("MM-dd HH:mm").format(record.getRecordTime())));
        }

        if (records.isEmpty()) {
            sb.append("暂无记录\n");
        }

        textArea.setText(sb.toString());
    }

    /**
     * 添加新记录并显示输入对话框
     * @param score 分数
     */
    public void addNewRecord(int score) {
        String playerName = JOptionPane.showInputDialog(this,
                "游戏结束！你的得分是: " + score + "\n请输入你的名字:",
                "新纪录", JOptionPane.PLAIN_MESSAGE);

        if (playerName != null && !playerName.trim().isEmpty()) {
            ScoreRecord record = new ScoreRecord(playerName.trim(), score, scoreDAO.getClass().getSimpleName());
            scoreDAO.addRecord(record);
            refreshRanking();
            setVisible(true);
        }
    }
}