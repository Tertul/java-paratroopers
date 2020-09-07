package ui;

import model.Game;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ScorePanel extends JPanel {
    private final Game game;
    private final PlayerStatistics stats;
    private boolean showStats;
    private final JLabel shots;
    private final JLabel score;
    private String userName;
    JTextField nameField;
    private final int userNameMaxLength = 8;
    private final int highscoreTableOffset = 500;

    public HighscoreTable highscoreTable;

    JLabel newHighscoreLabel;
    public JLabel restartPanel;


    ScorePanel(Game game) {
        this.game = game;
        this.stats = game.stats;
        game.scorePanel = this;
        shots = new JLabel();
        score = new JLabel();
        showStats = true;
        highscoreTable = new HighscoreTable();

        setLayout(null);
        add(shots);
        add(score);

        shots.setBounds(0, 100, 200, 20);
        shots.setForeground(Color.WHITE);
        score.setBounds(Game.CENTRE - 100, 0, 200, 30);
        score.setForeground(Color.WHITE);
        score.setFont(new Font("SansSerif", Font.PLAIN, 30));
        score.setHorizontalAlignment(SwingConstants.CENTER);

        updateScore();
        updateShots();
        setOpaque(false);
        setVisible(true);

        add(highscoreTable);

        // New highscore
        newHighscoreLabel = new JLabel();
        newHighscoreLabel.setBounds(0, highscoreTableOffset - 60, 1000, 20);
        newHighscoreLabel.setText("NEW HIGH SCORE! INPUT NAME THEN PRESS ENTER");
        newHighscoreLabel.setOpaque(false);
        newHighscoreLabel.setForeground(Color.WHITE);
        newHighscoreLabel.setVisible(false);
        newHighscoreLabel.setForeground(Color.WHITE);
        newHighscoreLabel.setFont(new Font(newHighscoreLabel.getFont().getFontName(),Font.PLAIN,20));
        newHighscoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(newHighscoreLabel);

        //Restart game
        restartPanel = new JLabel();
        restartPanel.setBounds(0, highscoreTableOffset - 20,1000,20);
        restartPanel.setText("PRESS R TO START A NEW GAME");
        restartPanel.setOpaque(false);
        restartPanel.setVisible(false);
        restartPanel.setForeground(Color.WHITE);
        restartPanel.setFont(new Font(newHighscoreLabel.getFont().getFontName(),Font.PLAIN,20));
        restartPanel.setHorizontalAlignment(SwingConstants.CENTER);
        add(restartPanel);

    }

    void update() {
        updateScore();
        updateShots();
        highscoreTable.updateTable();
    }

    private void updateScore() {
        score.setText(Integer.toString(stats.score));
    }

    private void updateShots() {
        shots.setText("Shots fired: " + stats.sessionShots);
    }

    void toggleShowStats() {
        showStats = !showStats;
        shots.setVisible(!shots.isVisible());
    }

    void askForName(int positionOnTable) {
        newHighscoreLabel.setVisible(true);
        userName = "";
        nameField = new JTextField();
        nameField.setBounds(Game.CENTRE - 50 - 50, highscoreTableOffset + 40 * positionOnTable, 300, 40);
        nameField.setVisible(false);
        nameField.setOpaque(false);
        nameField.setEditable(true);
        nameField.setCaretColor(Color.WHITE);
        nameField.getCaret().setVisible(true);
        nameField.setBackground(Color.BLACK);
        nameField.setForeground(Color.WHITE);
        nameField.setFont(new Font(highscoreTable.getFont().getFontName(),Font.PLAIN,40));
        add(nameField);

        game.inputtingName = true;
        nameField.setFocusable(true);
        nameField.requestFocusInWindow();


        nameField.addActionListener(e -> {
            game.inputtingName = false;
            fillOutName();
            game.stats.addNameToEntry(positionOnTable,userName);
            remove(nameField);
        });
        giveName();

    }


    private void fillOutName() {
        for (int i = 0 ; i > userNameMaxLength - userName.length() ; i++)
            userName = userName.concat(" ");
    }

    public void giveName() {
        userName = nameField.getText();
        if (userName.length() > userNameMaxLength) {
            userName = replaceLastCharacterInName();
            nameField.setText(userName);
        }
    }

    private String replaceLastCharacterInName() {
        return userName.substring(0,userName.length()-1);
    }


    public class HighscoreTable extends JTable {

        final List<PlayerStatistics.Entry> highscores;
        private int editingEntry;
        public boolean isVisible;
        DefaultTableModel defTblMdl;

        HighscoreTable() {
            Font tableFont = new Font(getFont().getFontName(), Font.PLAIN, 40);
            defTblMdl = new DefaultTableModel(10, 3);
            highscores = stats.getHighscores();
            setModel(defTblMdl);

            setRowHeight(40);
            setEnabled(false);
            ((DefaultTableCellRenderer) getDefaultRenderer(Object.class)).setOpaque(false);
            setForeground(Color.WHITE);
            setShowGrid(false);
            isVisible = false;
            setVisible(false);
            setBounds(Game.CENTRE - 300, highscoreTableOffset, 600, 400);
            setOpaque(false);
            ((DefaultTableCellRenderer) getDefaultRenderer(Object.class)).setBackground(Color.BLACK);
            ((DefaultTableCellRenderer) getDefaultRenderer(Object.class)).setForeground(Color.WHITE);
            setCellSelectionEnabled(true);

            DefaultTableCellRenderer rightAlignRender = new DefaultTableCellRenderer();
            rightAlignRender.setHorizontalAlignment(SwingConstants.RIGHT);
            rightAlignRender.setOpaque(false);
            rightAlignRender.setForeground(Color.WHITE);
            rightAlignRender.setBackground(Color.BLACK);
            getColumnModel().getColumn(2).setCellRenderer(rightAlignRender);

            rightAlignRender.setFont(tableFont);
            setFont(tableFont);

            updateTable();


        }


        void updateTable() {
            for (int i = 0; i < highscores.size(); i++) {
                PlayerStatistics.Entry currentEntry = highscores.get(i);
                writeToTable(i, currentEntry.date, currentEntry.name, Integer.toString(currentEntry.score));
            }
        }


        void writeToTable(int row, String date, String name, String score) {
            setValueAt(date, row, 0);
            setValueAt(name, row, 1);
            setValueAt(score, row, 2);
            defTblMdl.fireTableCellUpdated(row, 0);
            defTblMdl.fireTableCellUpdated(row, 1);
            defTblMdl.fireTableCellUpdated(row, 2);
        }

        public void giveName(){
            userName = (String) getValueAt(editingRow,1);
        }

        public void toggleVisible() {
            setVisible(!isVisible());
        }
    }
}