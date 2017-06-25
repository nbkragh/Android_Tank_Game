package nbkproduction.tankgame;

/**
 * Created by Bruger on 26-05-2017.
 */

public class Explosion
{
    int posX;
    int posY;
    int width;
    int height;
    float timer;
    int frames;
    int frame;

    public Explosion(int posX, int posY, int width, int height, int frames)
    {
        this.posX = posX-(width/2);
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.frames = frames;
        frame = 0;
    }

    public int getFrame()
    {
        return frame;
    }

    public int update(float deltaTime){
        timer += deltaTime*22;
        if((int)timer > frame){
            frame++;
        }

        return frame;
    }

    public int getFrames()
    {
        return frames;
    }
}
