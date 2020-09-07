package model;

public class NormalTrooper extends Trooper {

    NormalTrooper(Helicopter enemy){
        super();
        this.posX = enemy.getPosX();
        this.posY = enemy.getPosY();
        direction = this.posX < Game.CENTRE;
        this.chute = new Parachute(posX,posY);
        this.chuteFallSpeed = 1; //0.5
        this.runSpeed = 1;    //1
        this.damage = 10;
        this.points = 1;
    }


    public double getPosX() {
        return this.posX;
    }

    public double getPosY(){
        return this.posY;
    }

    public void setPosY(int i) {
        this.posY = i;
    }



}
