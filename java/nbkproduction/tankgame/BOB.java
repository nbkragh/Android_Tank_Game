package nbkproduction.tankgame;

/**
 * Created by Bruger on 27-05-2017.
 */

public class BOB
{
    private final double gravity = 9.81; //skal bruges til bobParts
    private class BobParts{
        double angle;
        double a;
        double b;
        double d;
        double speed;
        int posX;
        int posY;
        int initialY;
        int direction;
        protected BobParts(int posX, int posY){
            this.posX = posX;
            this.posY = posY;
            initialY = posY;
            if(((-1)+(Math.random()*2))< 0){
                direction = -1;
            }else{
                direction = 1;
            }
            angle = Math.toRadians((Math.random()*70)+20);

            speed = 50+(Math.random()*100);
            a = gravity/2;
            b = speed*Math.sin(angle);
            d = speed*Math.cos(angle);
        }
    }
    BobParts[] bParts = new BobParts[6];

    public void updateGore(float deltaTime){
        t += deltaTime*5;
        for (int i = 0; i < 6; i++)
        {
            bParts[i].posX = (bParts[i].direction)*(int)(bParts[i].d*t);
            bParts[i].posY = bParts[i].initialY-(int)(bParts[i].b*t)+(int)((t*t)*bParts[i].a);
        }

    }

    int posX;
    int posY;

    int height = 86;
    int width = 70;

    private boolean alive;
    private float t;

    public BOB(int posX, int posY)
    {
        t = 0;
        this.posX = posX;
        this.posY = posY-height;
        alive = true;
        bParts[0] = new BobParts(0,posY-height);
        bParts[1] = new BobParts((width/2),posY-height);
        bParts[2] = new BobParts(0,posY-(2*height/3));
        bParts[3] = new BobParts((width/2),posY-(2*height/3));
        bParts[4] = new BobParts(0,posY-(height/3));
        bParts[5] = new BobParts((width/2),posY-(height/3));
    }

    public void killBOB(){
        alive = false;
    }

    public boolean isAlive(){
        return  alive;
    }

    public int getbPartX(int i){
        return bParts[i].posX;
    }
    public int getbPartY(int i){
        return bParts[i].posY;
    }

}