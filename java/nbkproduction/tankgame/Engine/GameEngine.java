package nbkproduction.tankgame.Engine;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public abstract class GameEngine extends Activity implements Runnable, SensorEventListener
{
    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Screen screen;
    private Canvas canvas = null;
    Rect src = new Rect();
    Rect dst = new Rect();
    private Bitmap offScreenSurface;
    private TouchHandler touchHandler;
    //private List<TouchEvent> touchEventBuffer = new ArrayList<>();
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<>();
    private List<TouchEvent> touchEventsCopied = new ArrayList<>();
    private List<TouchEvent> touchEvents = new ArrayList<>();
    private float[] accelerometer = new float[3]; // x, y, z
    private SoundPool soundPool;
    private int framesPerSeconds = 0;
    private Paint paint = new Paint();
    public Music music;

    public abstract Screen createStartScreen();

    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        surfaceView= new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();
        fixTheScreen();
        touchHandler = new MultiTouchHandler(surfaceView, touchEventsBuffer, touchEventPool);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
        {
            Sensor accSensor = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        //this.soundPool = new SoundPool.Builder().setAudioAttributes().build();
        screen = createStartScreen();
    }

    public void setScreen(Screen screen)
    {
        if(this.screen != null)this.screen.dispose();
        this.screen = screen;
    }

    public Bitmap loadBitmap(String fileName)
    {
        InputStream in = null;
        Bitmap bitmap = null;

        try
        {
            in = getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap == null)
            {
                throw new RuntimeException("Could not create bitmap from file"+fileName);
            }
            return bitmap;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load the bloody file name:"+fileName);
        }
        finally
        {
            if(in != null)
            {
                try
                {
                    in.close();
                }
                catch(IOException e)
                {
                    Log.e("GameEngine","loadBitmap() failed to close the file:"+fileName);
                }
            }
        }
    }
    public Sound loadSound(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor,0);
            return new Sound(soundPool, soundId);
        }catch(IOException e)
        {
            throw new RuntimeException("Could not load sound file" + fileName+"");
        }
    }
    public Music loadMusic(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not load music file2" +fileName+"22222222222");
        }
    }

    //framebuffer, color == farve som en frame udviskes med
    public void clearFrameBuffer(int color)
    {
        canvas.drawColor(color);
    }

    public void fixTheScreen()
    {
        if (surfaceView.getWidth() > surfaceView.getHeight())
        {
            setOffScreenSurface(480, 340);
        }
        else
        {
            setOffScreenSurface(340, 480);
        }
    }

    public void setOffScreenSurface(int width, int height)
    {
        if(offScreenSurface != null)offScreenSurface.recycle();// .recycle() frigiver memory
        offScreenSurface = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(offScreenSurface);

    }

    public int getFrameBufferWidth()
    {
        return surfaceView.getWidth();
    }
    public int getFrameBufferHeight()
    {
        return surfaceView.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if(canvas != null) canvas.drawBitmap(bitmap, x, y, null);
    }

    public void drawBitmap(Bitmap bitmap, int x, int y,
                           int srcX, int srcY,
                           int srcWidth, int srcHeight)
    {
        if(canvas == null)return;
        src.left = srcX;
        src.top = srcY;
        src.right = srcX +srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }
    public List<TouchEvent> getTouchEvents()
    {
        return touchEventsCopied;
    }
    public int getTouchX(int pointer)
    {
        return (int)(touchHandler.getTouchX(pointer)*(float)offScreenSurface.getWidth()/(float)surfaceView.getWidth());
    }

    public int getTouchY(int pointer)
    {
        return (int)(touchHandler.getTouchY(pointer)*(float)offScreenSurface.getHeight()/(float)surfaceView.getHeight());
    }
    private void fillEvents()
    {
        synchronized (touchEventsBuffer)
        {
            for (int i = 0; i < touchEventsBuffer.size(); i++)
            {
                touchEventsCopied.add(touchEventsBuffer.get(i)); //copy all touch events
            }
            touchEventsBuffer.clear(); //empty buffer
        }
    }
    private void freeEvents()
    {
        synchronized (touchEventsCopied)
        {
            for (int i = 0; i < touchEventsCopied.size(); i++)
            {
                touchEventPool.free(touchEventsCopied.get(i));
            }
        }
        touchEventsCopied.clear();// empty list of events
    }
    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {}

    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
    }

    public Typeface loadFont(String fileName)
    {
        Log.d("GameEngine", "loadFont entered");
        Typeface font = Typeface.createFromAsset(getAssets(), fileName);
        if(font == null)
        {
            throw new RuntimeException("Could not load font from file:" + fileName);
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int color, int size)
    {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(color);
        canvas.drawText(text, x, y, paint);
    }

    public void run()
    {
        int frames = 0;
        long lastTime = System.nanoTime();
        long currTime = lastTime;
        while(true)
        {

            fixTheScreen();
            synchronized (stateChanges)
            {
                for(int i = 0; i < stateChanges.size(); i++)
                {
                    state = stateChanges.get(i);
                    if(state == State.Disposed)
                    {
                        if(screen != null)
                        {
                            screen.dispose();
                        }
                        Log.d("GameEngine", "state changed to disposed");
                        return;
                    }
                    if(state == State.Paused)
                    {
                        if(screen != null)
                        {
                            screen.pause();
                        }
                        Log.d("GameEngine", "state changed to paused");
                        return;
                    }
                    if(state == State.Resumed)
                    {
                        if(screen != null)
                        {
                            screen.resume();
                        }
                        state = State.Running;
                        //Log.d("GameEngine", "state changed to Running / Resumed");
                    }
                    //stateChanges.clear();
                    if(state == State.Running)
                    {
                        if(!surfaceHolder.getSurface().isValid()) continue;
                        Canvas canvas = surfaceHolder.lockCanvas(); //
                        //we will do all the drawing here
                        if(screen != null) screen.update(0);
                        fillEvents();
                        currTime = System.nanoTime();
                        if(screen !=null) screen.update((currTime - lastTime)/1000000000.0f);
                        lastTime = currTime;
                        freeEvents();
                        src.left =0;
                        src.top = 0;
                        src.right = offScreenSurface.getWidth() -1;
                        src.bottom = offScreenSurface.getHeight() -1;
                        dst.left = 0;
                        dst.top = 0;
                        dst.right = surfaceView.getWidth() -1;
                        dst.bottom = surfaceView.getHeight() -1;
                        canvas.drawBitmap(offScreenSurface, src, dst, null);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        canvas = null; //ryder op
                    }
                }
            }
        }
    }
    public int getFramesRate()
    {
        return framesPerSeconds;
    }
    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if(isFinishing())
            {
                soundPool.release();
                //close accelerometer
                stateChanges.add(stateChanges.size(), State.Disposed);
                ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            }
            else
            {
                stateChanges.add(stateChanges.size(), State.Paused);
            }
        }
        mainLoopThread.interrupt(); //når pausen slutter, så vil onResume() danne en ny tråd,
                                    //så den aktuelle tråd, der kører når onPause() bliver kaldt, skal afsluttes
        try
        {
            mainLoopThread.join();
        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void onResume()
    {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start(); //call run() on this object of this class
        synchronized (stateChanges) //only let one thread in here at a time
        {
            stateChanges.add(stateChanges.size(), State.Resumed);

            //restart accelerometer
            SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
            {
                Sensor accSensor = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
                manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
            }

        }
    }
}
