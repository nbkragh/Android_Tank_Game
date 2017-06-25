package nbkproduction.tankgame.Engine;


public class SimpleGame extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        return new SimpleScreen(this);
    }
}
