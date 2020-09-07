package test;

import model.Bullet;
import model.Game;
import model.Turret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    private Bullet bullet;

    @BeforeEach
    void setUp(){
        Turret turret = new Turret(new Game());
        bullet = new Bullet(turret.getAngle());
        checkBeginPosition();
    }

    @Test
    void TestBulletPathStraightUp(){

        updatePosition();

        assertEquals(bullet.getPosX(),500);
        assertEquals(bullet.getPosY(), 930);
    }

    @Test
    void TestBulletPath45Deg(){
        bullet.setBulletAngle(-45);


        updatePosition();

        assertEquals(bullet.getPosX(),500 + bullet.getBulletSpeed() * Math.cos(Math.toRadians(-45)));
        assertEquals(bullet.getPosY(),940 + bullet.getBulletSpeed() * Math.sin(Math.toRadians(-45)));
    }

    private void checkBeginPosition(){
        assertEquals(bullet.getPosX(), 500);
        assertEquals(bullet.getPosY(), 940);
    }

    private void updatePosition(){
        bullet.setPosX(bullet.getPosX() + bullet.getBulletSpeed() * Math.cos(Math.toRadians(bullet.getBulletAngle())));
        bullet.setPosY(bullet.getPosY() + bullet.getBulletSpeed() * Math.sin(Math.toRadians(bullet.getBulletAngle())));
    }
}
