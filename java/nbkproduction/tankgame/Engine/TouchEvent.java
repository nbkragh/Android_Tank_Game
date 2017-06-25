package nbkproduction.tankgame.Engine;

/**
 * Created by Bruger on 28-02-2017.
 */

public class TouchEvent
{
    public enum TouchEventType
    {
        DOWN,
        UP,
        DRAGGED
    }

    public TouchEventType type; //the type of event
    public int x;               // coordinates
    public int y;               // --||--
    public int pointer;         //the pointer id(from the android system)


}
