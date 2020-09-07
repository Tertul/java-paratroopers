package model;

public abstract class Trooper implements Enemy {

    double posX, posY;                // represents the centre of the object
    public Parachute chute;
    double chuteFallSpeed;
    final int noChuteFallSpeed = 8;         //8
    int runSpeed;
    public boolean hasChute;
    int damage;
    boolean onGround;
    boolean direction;

    int points;
    private final int width;
    private final int height;

    Trooper(){
        hasChute = true;
        onGround = false;
        width = 10;
        height = 20;
    }

    public void fall() {
        if (hasChute) {
            posY += chuteFallSpeed;
            chute.posY = posY - 30;
        }
        else
            posY += noChuteFallSpeed;
    }


    public void attack() {
        if (direction)
            posX += runSpeed;
        else
            posX -= runSpeed;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }


    public void setPosY(int i) {
        this.posY = i;
    }

    public class Parachute{
        private double posX, posY;
        private final int width = 30;
        private final int height = 10;
        public final int points;

        Parachute(double x, double y){
            posX = x-15;
            posY = y-30;
            points = 5;
        }

        public double getPosX() {
            return posX;
        }

        void setPosX(double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return posY;
        }

        void setPosY(double posY) {
            this.posY = posY;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}

