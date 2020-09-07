package test;

import model.Game;
import model.Turret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TurretTest {
    private Turret turret;

    @BeforeEach
    void setUp(){
        turret = new Turret(new Game());
    }


    @Test
    void TestAimAllowed(){
        assertEquals(turret.getAngle(), -90);

        aimLeft(turret);

        assertEquals(turret.getAngle(), -90 - turret.angleDelta);
    }

    @Test
    void TestAimLeftNotAllowed(){
        turret.setAngle(-160);

        assertEquals(turret.getAngle(), -160);

        aimLeft(turret);

        assertEquals(turret.getAngle(), -160);
    }

    @Test
    void TestAimRightNotAllowed(){
        turret.setAngle(-20);

        assertEquals(turret.getAngle(), -20);

        aimRight(turret);

        assertEquals(turret.getAngle(), -20);
    }

    private void aimLeft(Turret turret){
        turret.setAngle(turret.getAngle() - turret.angleDelta);
        if (turret.getAngle() <= -160)
            turret.setAngle(-160);
    }

    private void aimRight(Turret turret){
        turret.setAngle(turret.getAngle() + turret.angleDelta);
        if (turret.getAngle() >= -20)
            turret.setAngle(-20);
    }
}


