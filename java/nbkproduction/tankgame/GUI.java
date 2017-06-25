package nbkproduction.tankgame;

import android.util.Log;

import nbkproduction.tankgame.Engine.GameEngine;


/**
 * Created by Bruger on 26-05-2017.
 */

public class GUI
{
    public enum Button{
        PLUS("+",0, GUI.top, 120, 40),
        MINUS("-",120, GUI.top, 120, 40),
        FIRE("FIRE!",240, GUI.top, 120, 40),
        CANCEL("AGAIN!",360, GUI.top, 120, 40);

        private String titel;
        private int coordinateX;
        private int coordinateY;
        private int width;
        private int height;

        private Button(String titel, int coordinateX, int coordinateY, int width, int height)
        {
            this.titel = titel;
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
            this.width = width;
            this.height = height;
        }

        public String getTitel()
        {
            return titel;
        }

        public int getX()
        {
            return coordinateX;
        }

        public int getY()
        {
            return coordinateY;
        }

        public int getWidth()
        {
            return width;
        }

        public int getHeight()
        {
            return height;
        }
    }
        GameEngine game;
        public static int top = 300;
        long time;

    public GUI(GameEngine game){
        this.game = game;
        time = System.currentTimeMillis();

    }

    public GUI.Button buttonPressed(TankGame.Phase phase){
        if(System.currentTimeMillis()-time < 160){
            return null;
        }
        time = System.currentTimeMillis();
        boolean onPanel = game.isTouchDown(0) && game.getTouchY(0) > top;

        if(phase == TankGame.Phase.PREPARING){
            if(onPanel){
                if(game.getTouchX(0)< Button.PLUS.getWidth()){
                    Log.d("prepare button", "plus");
                    return Button.PLUS;

                }else if(game.getTouchX(0)< Button.MINUS.getX()+ Button.MINUS.getWidth())
                {
                    Log.d("prepare button", "minus");
                    return Button.MINUS;

                }else if(game.getTouchX(0)> Button.FIRE.getX() && game.getTouchX(0) < (Button.FIRE.getX()+ Button.FIRE.getWidth()))
                {
                    Log.d("prepare button", "fire");
                    return Button.FIRE;
                }
            }
        }else {
            if (onPanel)
            {
                if( game.getTouchX(0) > Button.CANCEL.getX() )
                {
                    Log.d("fired button", "cancel");
                    return Button.CANCEL;

                }
            }
        }

        return null;
    }

}
