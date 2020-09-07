package test;

import model.Helicopter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelicopterTest {

    private Helicopter heli;

    @BeforeEach
    void setUp(){
        heli = new Helicopter();
    }


    @Test
    void HeliMoveTest(){


        heli.move();

        if (heli.direction)
            assertEquals(heli.getPosX(), 0 + heli.flySpeed);
        else
            assertEquals(heli.getPosX(), 1000 - heli.flySpeed);
    }
}
