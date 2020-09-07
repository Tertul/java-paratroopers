package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Drawer extends JPanel {

    private final Game game;

    private final int width;
    private final int height;
    public boolean displayingHighscores;

    Drawer(Game game){
        this.game = game;
        this.height = Game.HEIGHT;
        this.width = Game.WIDTH;
        setBackground(Color.BLACK);
        displayingHighscores = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAll(g);
        repaint();
    }

    private void drawAll(Graphics g) {
        setBackground(Color.BLACK);
        drawBullets(g);
        drawTurret(g);
        drawHelicopters(g);
        drawTroopers(g);
        if (game.isGameOver) {
            drawGameOver(g);
        }

    }

    private void drawTurret(Graphics g){

        // turret barrel, rotating
        Graphics2D g2d = (Graphics2D) g.create();                                            //Graphics2D needed for rotational drawing
        AffineTransform old = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        g2d.transform(transform);
        g2d.rotate(Math.toRadians(game.turret.getAngle()),width/2.0 ,height-60);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(500,height-65,35,10);
        g2d.setTransform(old);

        //circular dome of turret
        g.setColor(Color.GRAY);
        g.fillOval(width/2 -36/2,height -80, 36,36);

        //turret base/building
        g.setColor(Color.WHITE);
        g.fillRect(width/2 -(game.turret.getWidth()/2),height-65 ,game.turret.getWidth(),game.turret.getHeight());

        // health bar
        g.setColor(Color.RED);
        g.fillRect(width/2 -50*2,40, 100*2,10);
        g.setColor(Color.GREEN);
        g.fillRect(width/2 -50*2, 40, game.turret.getHealth()*2, 10);

    }

    private void drawBullets(Graphics g){
        g.setColor(Color.WHITE);
        for (Bullet currentBullet : game.bullets){
            g.fillRect((int) Math.round(currentBullet.getPosX()-currentBullet.getWidth()/2.0),(
                    int) Math.round(currentBullet.getPosY()-currentBullet.getHeight()/2.0),
                    currentBullet.getWidth(),currentBullet.getHeight());
        }
    }

    private void drawHelicopters(Graphics g){
        g.setColor(Color.ORANGE);
        for (Helicopter currentHeli : game.helicopters)
            g.fillRect((int) Math.round(currentHeli.getPosX()-(currentHeli.getWidth()/2.0)),
                    (int) Math.round(currentHeli.getPosY() - (currentHeli.getHeight()/2.0)),
                    currentHeli.getWidth(),currentHeli.getHeight());
    }

    private void drawTroopers(Graphics g){
        int bodyCentreX, bodyCentreY, bodyTopY;
        for (Trooper cs : game.troopers){
            if (cs instanceof SuperTrooper)
                g.setColor(Color.BLUE);
            else
                g.setColor(Color.RED);

            //body
            bodyCentreX = (int) cs.getPosX();
            bodyCentreY = (int) cs.getPosY();
            bodyTopY = bodyCentreY - cs.getHeight()/2;
            g.fillRect((int) Math.round(bodyCentreX-cs.getWidth()/2.0),(int) Math.round(bodyCentreY - cs.getHeight()/2.0),
                    cs.getWidth(), cs.getHeight());

            //parachute (if they have one)
            if (cs.hasChute) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(bodyCentreX, bodyTopY, bodyCentreX - 5, bodyTopY - 20);         //L line
                g.drawLine(bodyCentreX, bodyTopY, bodyCentreX + 5, bodyTopY - 20);        //R line
                g.fillRect((int) cs.chute.getPosX(), (int) cs.chute.getPosY(), cs.chute.getWidth(), cs.chute.getHeight());
            }
        }
    }

    private void drawGameOver(Graphics g) {
        if (!displayingHighscores) {
            g.setColor(Color.RED);
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 30));
            g.drawString("GAME OVER", Game.CENTRE - 100, 400);

            Timer timer = new Timer(1500, e -> {                           // this block only happens once
                repaint();
                displayingHighscores = true;
                game.scorePanel.highscoreTable.setVisible(true);

                if (! (game.scorePanel.nameField == null)){
                    game.scorePanel.nameField.requestFocusInWindow();
                    game.scorePanel.nameField.setVisible(true);
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}

