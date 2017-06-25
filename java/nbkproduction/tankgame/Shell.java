package nbkproduction.tankgame;

/**
 * Created by Bruger on 24-05-2017.
 */

public class Shell
{
    private float initialX;
    private float initialY;

    public float distance;
    public float height;

    private double angle;
    private double speed;

    private boolean visible;

    private final double gravity = 9.81;
    private double a ;
    private double b ;
    private double d;
    private float t;

    public Shell(int initialX, int initialY, double angle, double speed)
    {

        this.initialX = initialX;
        this.initialY = initialY;
        this.distance = 0;
        this.height = initialY;
        a = gravity/2;
        t = 0;
        setSpeed(speed);
        setAngle(angle);
        visible = true;
    }

    public float trajectory(float deltaTime){
        t += deltaTime*5;
        distance = (float) d *t;
        height = (float)(initialY-(t*b)+((t*t)*a) );
        return height;
    }
    public void setAngle(double angle)
    {
        this.angle = Math.toRadians(angle);

        b = speed*Math.sin(this.angle);
        d = speed*Math.cos(this.angle);
    }

    public float getDistance()//kompenserer for at distance != koordinat
    {
        return distance+initialX;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public double getAngle()
    {
        return Math.toDegrees(angle);
    }



    public double getSpeed()
    {
        return speed;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visibel)
    {
        this.visible = visibel;
    }
}
