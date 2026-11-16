package edu.hitsz.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主菜单界面，提供难度选择，使用CardLayout切换到游戏界面
 */
public class MainMenuPanel extends JPanel {
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;
    private JLabel titleLabel;
    private JPanel buttonPanel;

    public MainMenuPanel(DifficultySelectListener listener) {
        setLayout(new BorderLayout());
        titleLabel = new JLabel("飞机大战 - 请选择难度", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        easyButton = new JButton("简单模式 EASY");
        normalButton = new JButton("普通模式 NORMAL");
        hardButton = new JButton("困难模式 HARD");
        buttonPanel.add(easyButton);
        buttonPanel.add(normalButton);
        buttonPanel.add(hardButton);
        add(buttonPanel, BorderLayout.CENTER);

        easyButton.addActionListener(e -> listener.onDifficultySelected("easy"));
        normalButton.addActionListener(e -> listener.onDifficultySelected("normal"));
        hardButton.addActionListener(e -> listener.onDifficultySelected("hard"));
    }

    public interface DifficultySelectListener {
        void onDifficultySelected(String difficulty);
    }
}
