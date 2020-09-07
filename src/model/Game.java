package model;

import exceptions.*;
import ui.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;

public class Game {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;                // game window dimensions
    public static final int CENTRE = WIDTH / 2;

    private final int MAX_HELICOPTERS = 10;                // 5
    private final int MAX_TROOPERS = 8;
    final int MAX_BULLETS = 100;

    public int abilityUses = 3;

    public Turret turret;
    public final ArrayList<Bullet> bullets;
    public final ArrayList<Helicopter> helicopters;
    public final ArrayList<Trooper> troopers;
    public ScorePanel scorePanel;
    public final PlayerStatistics stats;
    public final KeyParser kp;

    private final Random rand = new Random();

    public boolean isGameOver = false;
    public boolean inputtingName = false;
    public Drawer drawer;

    public Game() {
        bullets = new ArrayList<>();
        helicopters = new ArrayList<>();
        troopers = new ArrayList<>();
        this.stats = new PlayerStatistics(this);
        this.kp = new KeyParser(this);

        newGame();
    }

    public void newGame() {
        try{
            drawer.displayingHighscores = false;
            drawer.repaint();
            scorePanel.restartPanel.setVisible(false);
        }catch (NullPointerException npe){
            System.out.println("game launched");
        }
        finally {
            isGameOver = false;
            turret = new Turret(this);
            bullets.clear();
            helicopters.clear();
            troopers.clear();
            stats.newGame();
            kp.inputEnabled = true;
        }
    }

    public void update() {
        try{
            updateTurret();
        }catch (TurretNotAliveException tnae){
            //tnae.printStackTrace();
        }finally {
            updateBullets();
            updateHelicopters();
            updateTroopers();
            hitDetection();
            if (!isGameOver)
                checkEndState();
            if (inputtingName)
                scorePanel.giveName();
        }
    }

    private void updateTurret() throws TurretNotAliveException {
        turret.currentBulletInterval++;                   // interval is updated here so it is always incrementing with the timer, such that if someone taps to shoot, the interval wont be stuck until enough shots have been fired in shoot()
        if (turret.getAlive()) {
            if (kp.spaceDown)
                try {
                    turret.shoot();
                } catch (TooManyBulletsException tmbe) {
                    System.out.println("too many bullets");
                } catch (BulletOnCooldownException boce) {
                    // do nothing
                }
            if (kp.rightDown)
                turret.aim(KeyEvent.VK_RIGHT);
            if (kp.leftDown)
                turret.aim(KeyEvent.VK_LEFT);
        } else
            throw new TurretNotAliveException();
    }

    private void updateTroopers() {
        for (int i = 0; i < troopers.size(); i++) {
            Trooper ct = troopers.get(i);
            if (ct.onGround){
                ct.attack();
            }
            else
                ct.fall();

            if (!ct.onGround) {
                if (ct.getPosY() + 40 >= HEIGHT) {
                    ct.setPosY(HEIGHT - 45);                 // resets their y pos to be more uniform on the ground
                    ct.onGround = true;
                    if (!ct.hasChute){
                        stats.score+=ct.points;
                        troopers.remove(ct);
                    }
                    else
                        ct.hasChute = false;
                }
            }

            if (((ct.direction && ct.getPosX() >= CENTRE) || (!ct.direction && ct.getPosX() <= CENTRE)) && ct.onGround) {      // if soldier has reached base
                turret.takeDamage(ct.damage);
                troopers.remove(ct);
            }

            if (checkIfOutOfSides(ct.getPosX())) {                                                                             // if soldier moves out of bounds
                troopers.remove(ct);
            }
        }
    }

    private void updateHelicopters() {
        if ((rand.nextInt((50 - 1) + 1) + 1 == 25) && helicopters.size() < MAX_HELICOPTERS) {          // rolls 1 to 10, equal 5, i.e 1/50 chance
            helicopters.add(new Helicopter());
        }

        if (!helicopters.isEmpty()) {
            for (int i = 0; i < helicopters.size(); i++) {
                Helicopter ch = helicopters.get(i);
                ch.move();

                double chX = ch.getPosX();
                if (checkIfOutOfSides(chX)) {
                    helicopters.remove(i);
                    i--;
                }

                if ((!ch.getHasDropped() &&                                                             // checks if heli can still drop a trooper
                        ((chX > 30 && chX < CENTRE - turret.getWidth() / 2 - 30) ||
                                ((chX < WIDTH - 30) && (chX > CENTRE + turret.getWidth() / 2 + 30)))          // within valid drop zones
                        && rand.nextInt((100 - 1) + 1) + 1 == 50) &&                                          // at a 1% chance to drop
                        troopers.size() < MAX_TROOPERS) {                                                     // and there is room for more troops
                    Trooper newTroop = ch.attack();
                    troopers.add(newTroop);
                }

                if (ch.isCrashing){
                    ch.crash();

                    if (ch.posY+ch.height/2 >= HEIGHT){
                        for (int angle = -160 ; angle <= -20 ; angle+=20){             // 8
                            bullets.add(new Bullet(ch.posX,angle));
                        }

                        helicopters.remove(ch);
                    }
                }
            }
        }
    }

    private void updateBullets() {
        if (!bullets.isEmpty()) {
            for (int i = 0; i < bullets.size(); i++) {
                Bullet cb = bullets.get(i);
                cb.moveBullet();

                if (checkIfOutOfSides(cb.getPosX()) || (cb.getPosY() < 0)) {
                    bullets.remove(i);
                    i--;
                }
            }
        }
    }

    private boolean checkIfOutOfSides(double x){
        return (x >= WIDTH || x<=0);
    }

    private void hitDetection() {
        for (int b = 0; b < bullets.size(); b++) {
            Bullet cb = bullets.get(b);
            Rectangle cbHitbox = new Rectangle((int) cb.getPosX() - cb.getWidth() / 2, (int) cb.getPosY() - cb.getHeight() / 2,
                    cb.getWidth(), cb.getHeight());

            if(hitHelicopter(cbHitbox)){
                bullets.remove(b);
                break;
            }

            if (hitTrooper(cbHitbox)){
                bullets.remove(b);
                break;
            }
        }
    }

    private boolean hitHelicopter(Rectangle bHitbox){
        for (int h = 0; h < helicopters.size(); h++) {
            Helicopter ch = helicopters.get(h);
            Rectangle chHitbox = getHitboxRectangle(ch);

            if (bHitbox.intersects(chHitbox) && !ch.isCrashing) {
                stats.score += ch.points;
                if (rand.nextInt((5-1)+1 +1) == 3)
                    ch.isCrashing = true;
                else
                    helicopters.remove(h);
                return true;
            }
        }
        return false;
    }

    private boolean hitTrooper(Rectangle bHitbox){
        for (int t = 0; t < troopers.size(); t++) {
            Trooper ct = troopers.get(t);
            Rectangle ctHitbox = getHitboxRectangle(ct);

            if (ct.hasChute) {
                Rectangle ctChuteHitbox = new Rectangle((int) (ct.chute.getPosX()), (int) ct.chute.getPosY(),
                        ct.chute.getWidth(), ct.chute.getHeight());

                if (bHitbox.intersects(ctChuteHitbox)) {
                    stats.score += ct.chute.points;
                    ct.hasChute = false;
                    return true;
                }
            }

            if (bHitbox.intersects(ctHitbox)) {
                stats.score+= ct.points;
                troopers.remove(t);
                return true;
            }
        }
        return false;
    }

    //E creates a rectangle object for use in hitbox calculations
    private Rectangle getHitboxRectangle(Enemy e){
        return new Rectangle((int) e.getPosX() - e.getWidth()/2, (int) e.getPosY() - e.getHeight()/2,
                e.getWidth(),e.getHeight());
    }

    private void checkEndState() {
        if (turret.getHealth() <= 0) {
            gameOver();
        }

        else if (stats.score >= 9999){
            System.out.println("win");
        }
    }

    public void gameOver() {
        turret.setNotAlive();
        isGameOver = true;
        kp.inputEnabled = false;
        bullets.clear();
        stats.checkScore();
    }

    public void terminate() throws IOException {
        try{
            if (!isGameOver)                 // if game is not already over
                gameOver();
       stats.writeStatsToFile();}
       catch (InvalidStatsFileException isfe){
           System.out.println("invalid stats file -- scores not saved");
       }
       finally {
            System.out.println("program terminated");
        }
    }
}