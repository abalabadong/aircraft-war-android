package edu.hitsz.ui;

import edu.hitsz.dao.ScoreDao;
import edu.hitsz.dao.ScoreRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ScoreBoardPanel extends JPanel {
    private JTable scoreTable;
    private DefaultTableModel model;
    private JButton deleteButton;
    private JButton addButton;
    private JTextField nameField;
    private JLabel nameLabel;
    private ScoreDao scoreDao;
    private String difficulty;

    public ScoreBoardPanel(ScoreDao scoreDao, String difficulty, int score) {
        this.scoreDao = scoreDao;
        this.difficulty = difficulty;
        setLayout(new BorderLayout());
        String[] columnNames = {"玩家", "得分"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        scoreTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        nameLabel = new JLabel("姓名:");
        nameField = new JTextField(10);
        addButton = new JButton("记录本局得分");
        deleteButton = new JButton("删除选中记录");
        bottomPanel.add(nameLabel);
        bottomPanel.add(nameField);
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入姓名");
                return;
            }
            scoreDao.addScore(new ScoreRecord(name, score));
            refreshTable();
        });

        deleteButton.addActionListener(e -> {
            int row = scoreTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请选择要删除的记录");
                return;
            }
            String name = (String) model.getValueAt(row, 0);
            int value = (int) model.getValueAt(row, 1);
            scoreDao.removeScore(new ScoreRecord(name, value));
            refreshTable();
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<ScoreRecord> records = scoreDao.getAllScores();
        for (ScoreRecord r : records) {
            // ScoreRecord uses getPlayerName()
            model.addRow(new Object[]{r.getPlayerName(), r.getScore()});
        }
    }
}
