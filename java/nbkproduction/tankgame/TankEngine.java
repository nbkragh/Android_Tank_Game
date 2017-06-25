package nbkproduction.tankgame;

import nbkproduction.tankgame.Engine.GameEngine;
import nbkproduction.tankgame.Engine.Screen;

/**
 * Created by Bruger on 24-05-2017.
 */

public class TankEngine extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        return new TankGame(this);
    }

}
