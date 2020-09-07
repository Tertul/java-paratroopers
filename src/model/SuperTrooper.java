package model;

public class SuperTrooper extends Trooper {

    private final boolean direction;           // true = going right

    SuperTrooper(Helicopter enemy){
        super();
        this.posX = enemy.getPosX();
        this.posY = enemy.getPosY();
        this.direction = enemy.direction;
        this.chuteFallSpeed = 1.5;
        this.chute = new Parachute(posX,posY);
        this.runSpeed = 1;
        this.damage = 15;
        this.points = 2;
    }

    @Override
    public void fall(){
        if (hasChute) {
            posY += chuteFallSpeed;
            chute.setPosY(posY - 30);
        }
        else
            posY += noChuteFallSpeed;

        if (direction) {             // true = going right
            posX += 1;
            chute.setPosX(chute.getPosX()+1);
        }
        else {
            posX -= 1;
            chute.setPosX(chute.getPosX()-1);
        }
    }

    @Override
    public void attack(){
        if (posX < Game.WIDTH/2)
            posX += 1;
        else
            posX -= 1;
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
