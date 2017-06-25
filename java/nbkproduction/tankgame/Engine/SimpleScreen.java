package nbkproduction.tankgame.Engine;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;


public class SimpleScreen extends Screen
{
    float x = 0;
    float y = 0;

    Bitmap bitmap;
    Random rand = new Random();
    int clearColor = Color.DKGRAY;
    Sound sound;
    Music music;

    public SimpleScreen(GameEngine game)
    {
        super(game);
        bitmap = game.loadBitmap("bob.png");
        sound = game.loadSound("bounce.wav"); /////////////////////////////////////////////
        music = game.loadMusic("music.ogg"); /////////////////////////////////////////////
        music.setLooping(true);
        music.play();
    }
    @Override
    public void update(float deltaTime)
    {
        //Log.d("SimpleScreen", "_________________ fps:" +game.getFramesRate());
        game.clearFrameBuffer(clearColor);
        for (int pointer = 0; pointer < 5; pointer++)
        {
            if(game.isTouchDown(pointer)){
                game.drawBitmap(bitmap, game.getTouchX(pointer), game.getTouchY(pointer));
                //sound.play(1);
                if (music.isPlaying())
                {
                    music.pause();

                }else
                {
                    music.play();
                }

            }
        }
        /*
        float x = game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];
        float accKonstant = 50;
        x = (x/accKonstant)*game.getFrameBufferWidth() + game.getFrameBufferWidth()/2;
        y = (y/accKonstant)*game.getFrameBufferHeight() + game.getFrameBufferHeight()/2;
        game.drawBitmap(bitmap, (int)(x-((float)bitmap.getWidth()/2)), (int)(y-((float)bitmap.getHeight()/2)) );
        */

        x = x + (1 * deltaTime); // classvariabel (int) x, ikke local variabel (float) x (accelerometer)
        if(x > 320) x = 0;
        game.drawBitmap(bitmap, (int)x, 50);
    }

    @Override
    public void pause()
    {
        music.pause();
    }

    @Override
    public void resume()
    {
        if(!music.isPlaying()) music.play();
    }

    @Override
    public void dispose()
    {
        music.isPlaying();
    }
}
