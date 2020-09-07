package model;


public class Bullet {

    private double posX, posY;
    private int bulletAngle;
    private final int bulletSpeed = 10;

    private final int width;
    private final int height;

    public Bullet (int angle){
        bulletAngle = angle;
        posX = Game.CENTRE;
        posY = Game.HEIGHT - 60;
        width = 4;
        height = 4;
    }

    Bullet(double x, int angle){
        bulletAngle = angle;
        posX = x;
        posY = Game.HEIGHT-60;
        width = 4;
        height = 4;
    }

    public double getPosX(){
        return this.posX;
    }

    public double getPosY(){
        return this.posY;
    }

    public int getBulletAngle(){
        return this.bulletAngle;
    }

    public void setPosX(double x){
        this.posX = x;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setBulletAngle(int bulletAngle) {
        this.bulletAngle = bulletAngle;
    }

    void moveBullet(){
        this.setPosX(this.getPosX() + bulletSpeed * Math.cos(Math.toRadians(this.getBulletAngle())));
        this.setPosY(this.getPosY() + bulletSpeed * Math.sin(Math.toRadians(this.getBulletAngle())));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public int getBulletSpeed(){
        return bulletSpeed;
    }
}





