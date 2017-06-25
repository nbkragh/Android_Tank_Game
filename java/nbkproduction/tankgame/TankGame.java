package nbkproduction.tankgame;
import android.util.Log;

import nbkproduction.tankgame.Engine.GameEngine;
import nbkproduction.tankgame.Engine.Screen;

/**
 * Created by Bruger on 24-05-2017.
 */

public class TankGame extends Screen
{
    public enum Phase {
        PREPARING, FIRING
    }

    World world;
    GUI gui;
    Picture picture;
    Phase phase;

    public TankGame(GameEngine game)
    {
        super(game);
        world = new World(game);
        gui = new GUI(game);
        picture = new Picture(game, world, gui);
        phase = phase.PREPARING;

    }

    @Override
    public void update(float deltaTime)
    {   GUI.Button button = gui.buttonPressed(phase);
        switch(phase){

            case PREPARING:

                if(button == GUI.Button.FIRE){
                    Log.d("gamescreen", "register Button.FIRE");
                    phase = phase.FIRING;
                }else if(button == GUI.Button.PLUS){
                    Log.d("gamescreen", "register Button.PLUS");
                    world.incAngle();
                }else if(button == GUI.Button.MINUS){
                    world.decAngle();
                }
                break;
            case FIRING:
                world.update(deltaTime);
                if(button == GUI.Button.CANCEL){
                    world.reset();
                    phase = phase.PREPARING;
                }
                break;
            default:
                break;
        }

        picture.paintWorld();
        picture.paintGUI(phase);
    }

    public Phase getPhase()
    {
        return phase;
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }

}
