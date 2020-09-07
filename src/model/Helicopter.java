package model;

import java.util.Random;


public class Helicopter extends Aircraft {

    public final boolean direction;           // true: going right, false: going left
    public final double flySpeed;

    public boolean isCrashing;
    public final int points;

    private final Random random = new Random();

    private final int upperBound = 100;
    private final int lowerBound = 400;

    public Helicopter(){
        this.direction = random.nextBoolean();
        this.flySpeed = random.nextInt((2-1) + 1) + 1;

        this.hasDropped = false;
        this.points = 1;

        if (this.direction)
            this.posX = 0;
        else
            this.posX = Game.WIDTH;

        this.posY = random.nextInt((lowerBound - upperBound)+1);         // follows formula nextInt((max - min) + 1) + min

        this.width = 40;
        this.height = 15;

    }

    public Trooper attack() {              // helicopters attack will be to drop trooper
        Trooper trooper;
        if (random.nextInt(((3-1)+1) +1) == 1)                        // 1/5 chance of dropping a supertroop
            trooper = new SuperTrooper(this);
        else
            trooper = new NormalTrooper(this);


        if (random.nextInt(((4-1)+1) + 1) >= 4) // <= 2              // 25% chance of being able to drop again
            this.setHasDropped();

        return trooper;
    }
    public void move() {
        if (direction){
            posX += flySpeed;
        }
        else
            posX -= flySpeed;

    }

    public void crash() {
        move();                 // still want it to move horizontally
        posY += 10;
    }


    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }


    private void setHasDropped(){
        this.hasDropped = true;
    }


}
