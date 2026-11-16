package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 程序入口
 * 提供难度选择界面
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");

        // 显示难度选择界面
        SwingUtilities.invokeLater(() -> showDifficultySelection());
    }

    /**
     * 显示难度选择界面
     */
    private static void showDifficultySelection() {
        JFrame frame = new JFrame("飞机大战 - 选择难度");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        // 标题
        JLabel titleLabel = new JLabel("飞机大战", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 36));
        titleLabel.setForeground(new Color(30, 58, 95));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        // 难度说明
        JLabel infoLabel = new JLabel("请选择游戏难度:", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        buttonPanel.add(infoLabel);

        // 简单按钮
        JButton easyBtn = createDifficultyButton("简单模式", new Color(76, 175, 80));
        easyBtn.addActionListener(e -> startGame(frame, GameDifficulty.EASY));
        buttonPanel.add(easyBtn);

        // 普通按钮
        JButton normalBtn = createDifficultyButton("普通模式", new Color(255, 152, 0));
        normalBtn.addActionListener(e -> startGame(frame, GameDifficulty.NORMAL));
        buttonPanel.add(normalBtn);

        // 困难按钮
        JButton hardBtn = createDifficultyButton("困难模式", new Color(244, 67, 54));
        hardBtn.addActionListener(e -> startGame(frame, GameDifficulty.HARD));
        buttonPanel.add(hardBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * 创建难度选择按钮
     */
    private static JButton createDifficultyButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * 开始游戏
     */
    private static void startGame(JFrame selectionFrame, GameDifficulty difficulty) {
        selectionFrame.dispose();

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame gameFrame = new JFrame("Aircraft War - " + difficulty.getName());
        gameFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameFrame.setResizable(false);
        gameFrame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game game = new Game(difficulty);
        gameFrame.add(game);
        gameFrame.setVisible(true);
        game.action();
    }
}