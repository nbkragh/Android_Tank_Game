package nbkproduction.tankgame.Engine;

/**
 * Created by Bruger on 28-02-2017.
 */

public class TouchEventPool extends Pool<TouchEvent>
{
    @Override
    protected TouchEvent newItem()
    {
        return  new TouchEvent();
    };
}
