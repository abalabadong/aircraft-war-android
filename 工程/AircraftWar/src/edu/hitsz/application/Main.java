package edu.hitsz.application;

import edu.hitsz.ui.MainMenuPanel;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        frame.setContentPane(cardPanel);

        MainMenuPanel mainMenu = new MainMenuPanel(difficulty -> {
            System.out.println("Difficulty selected: " + difficulty);
            cardPanel.removeAll();
            Game game = new Game(difficulty);
            // ensure game fills the panel and is focusable
            game.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            game.setFocusable(true);
            cardPanel.add(game, "game");
            cardLayout.show(cardPanel, "game");
            // revalidate and repaint to ensure UI updates
            cardPanel.revalidate();
            cardPanel.repaint();
            game.requestFocusInWindow();
            game.action();
        });
        cardPanel.add(mainMenu, "menu");
        cardLayout.show(cardPanel, "menu");
        frame.setVisible(true);
    }
}