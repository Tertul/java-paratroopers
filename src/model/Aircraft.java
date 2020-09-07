package model;

public abstract class Aircraft implements Enemy {

    public double posX, posY;
    int width;
    int height;
    boolean hasDropped;

    public double getPosX(){
        return posX;
    }

    public double getPosY(){
        return posY;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }

    boolean getHasDropped() {
        return hasDropped;
    }
}
