package nbkproduction.tankgame.Engine;

/**
 * Created by Bruger on 28-02-2017.
 */

public interface TouchHandler
{
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
}
