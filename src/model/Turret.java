package model;

import exceptions.BulletOnCooldownException;
import exceptions.TooManyBulletsException;
import ui.PlayerStatistics;

import java.awt.event.KeyEvent;

public class Turret {
    private final int width = 50;
    private final int height = 30;                                  // dimensions of the turret, for rendering purposes

    private int angle;                                              // angle of turret cannon
    public final int angleDelta;                                    // speed at which turret angle changes per key input
    private static final int MAX_ANGLE_LEFT = -160;
    private static final int MAX_ANGLE_RIGHT = -20;

    int currentBulletInterval = 0;
    private static final int BULLET_INTERVAL = 4;                   // lower = faster shooting/smaller gaps

    private int health;
    private boolean alive;

    private final Game game;
    private final PlayerStatistics stats;


    public Turret(Game g){
        game = g;
        this.stats = g.stats;
        this.angle = -90;
        this.health = 100;
        this.angleDelta = 1;
        this.alive = true;
    }

    void aim(int keycode){
        if (keycode == KeyEvent.VK_LEFT) {
            if ((this.angle -= this.angleDelta) >= MAX_ANGLE_LEFT)
                this.angle -= angleDelta;
            else
                this.angle = MAX_ANGLE_LEFT;
        }
        else if (keycode == KeyEvent.VK_RIGHT){
            if ((this.angle += this.angleDelta) <= MAX_ANGLE_RIGHT)
                this.angle += angleDelta;
            else
                this.angle = MAX_ANGLE_RIGHT;
        }
    }

    void shoot() throws TooManyBulletsException, BulletOnCooldownException {
        if (currentBulletInterval > BULLET_INTERVAL) {
            if (game.bullets.size() < game.MAX_BULLETS) {
                game.bullets.add(new Bullet(angle));
                stats.sessionShots++;
                stats.totalShots++;
                currentBulletInterval = 0;
            } else
                throw new TooManyBulletsException();
        } else
            throw new BulletOnCooldownException();
    }

    public void ability() {
        if (game.bullets.size() + 28 <= game.MAX_BULLETS) {
            game.abilityUses--;
            for (int i = -160; i <= -20; i += 5) {               // 28 bullets generated per wave
                game.bullets.add(new Bullet(i));
            }
        }
    }

    void takeDamage(int dmg) {
        health-=dmg;
    }

    public int getAngle(){
        return this.angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    boolean getAlive() {
        return this.alive;
    }

    void setNotAlive() {
        this.alive = false;
    }

    public int getHealth() {
        return health;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
