package ui;

import exceptions.GameOverException;
import exceptions.InvalidKeyCodeException;
import exceptions.InvalidStatsFileException;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

class AckAck extends JFrame {
    private final Game game;
    private final ScorePanel scorePanel;
    private final KeyParser kp;

    private AckAck(){
        super("Ack Ack");        // sets window title
        game = new Game();
        this.kp = game.kp;
        Dimension dimension = new Dimension(Game.WIDTH, Game.HEIGHT);
        setSize(dimension);
        setPreferredSize(dimension);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowHandler());
        setLocationRelativeTo(null);
        addKeyListener(new InputHandler());
        setFocusable(true);

        scorePanel = new ScorePanel(game);
        Drawer drawer = new Drawer(game);
        game.drawer = drawer;

    
        drawer.setLayout(null);
        createTimer();

        add(drawer);
        drawer.add(scorePanel);
        scorePanel.setBounds(0,0, Game.WIDTH, Game.HEIGHT);
        pack();
        setVisible(true);
    }

    private void createTimer(){
        Timer timer = new Timer(30, e -> {
            game.update();
            scorePanel.update();
        });

        timer.start();
    }

    private class InputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke){
            try {
                kp.keyPressed(ke.getKeyCode());
            }
            catch (GameOverException kce) {
            //kce.printStackTrace();
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            try {
                kp.keyReleased(ke.getKeyCode());
            }
            catch (InvalidKeyCodeException kce){
                //kce.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        new AckAck();
    }


    private class WindowHandler extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            System.out.println("closing window");
            try {
                game.stats.writeStatsToFile();
                //game.terminate();
            } catch (IOException ioe) {
                System.out.println("invalid stats file");
            } catch (InvalidStatsFileException e1) {
                e1.printStackTrace();
            }
        }
    }
}
