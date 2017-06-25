package nbkproduction.tankgame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import nbkproduction.tankgame.Engine.GameEngine;

/**
 * Created by Bruger on 24-05-2017.
 */

public class Picture
{
    private GameEngine game;
    private Bitmap background;
    private Bitmap ball;
    private Bitmap panel;
    private Bitmap fireball;
    private Bitmap tank;
    private Bitmap bob;
    private int[][] gore;
    private World world;
    private GUI gui;
    private Typeface typeface;

    private int distThreshold = 300;
    private int backgroundWidth = 2500;
    private int backgroundHeight = 340;

    public Picture(GameEngine game, World world, GUI gui){
        this.game = game;
        this.world = world;
        this.gui = gui;
        background = game.loadBitmap("t_gameBackground.png");
        ball = game.loadBitmap("ball.png");
        panel = game.loadBitmap("panel.png");
        fireball = game.loadBitmap("explosion.png");
        tank = game.loadBitmap("tank.png");
        bob = game.loadBitmap("bob.png");
        gore = new int[][]{{0,0},{35,0},{0,29},{35,29},{0,58},{35,58}}; //bob bitmap parts koordinater
        typeface = game.loadFont("font.ttf");

    }

    public void paintWorld()
    {

        int focuspoint = world.getFocuspoint();
        int ballFramePos; //relative to the frame
        int backSrcPos; //relative to the background bitmap

        if ( focuspoint > distThreshold){
            ballFramePos = distThreshold;
            backSrcPos = focuspoint-distThreshold;
        }else{
            ballFramePos = focuspoint;
            backSrcPos = 0;
        }
        int bobPos = world.getBob().posX-backSrcPos;
        game.drawBitmap(background, 0, 0, backSrcPos, 0, 480, 300);
        if(world.getProjectile().isVisible()){
            game.drawBitmap(ball, ballFramePos-7, (int) world.getProjectile().height-7);
        }else if(world.getExplosion() != null){


            game.drawBitmap(fireball,
                    ballFramePos-(world.getExplosion().width/2), world.getExplosion().posY-world.getExplosion().height ,
                    world.getExplosion().width*world.getExplosion().getFrame(), 0,
                    world.getExplosion().width, world.getExplosion().height);
        }
        game.drawBitmap(tank, 20-backSrcPos, world.groundLevel-50);

        if(world.getBob().isAlive()){
            game.drawBitmap(bob, bobPos, world.getBob().posY);
        }else{
            for (int i = 0; i < 6; i++)
            {
                game.drawBitmap(bob,bobPos-world.getBob().getbPartX(i),world.getBob().getbPartY(i),
                                gore[i][0],gore[i][1],
                                35,28);
            }
        }
    }

    public void paintGUI(TankGame.Phase phase){
        if(phase == TankGame.Phase.PREPARING)
        {
            game.drawText(typeface, (int)world.getProjectile().getAngle()+"", 16,24, Color.RED, 16);
            game.drawBitmap(panel, 0, 300, 0, 0, 360, 40);
            game.drawText(typeface, GUI.Button.PLUS.getTitel(), GUI.Button.PLUS.getX()+50, GUI.Button.PLUS.getY()+34, Color.WHITE, 32);
            game.drawText(typeface, GUI.Button.MINUS.getTitel(), GUI.Button.MINUS.getX()+50, GUI.Button.MINUS.getY()+40, Color.WHITE, 32);
            game.drawText(typeface, GUI.Button.FIRE.getTitel(), GUI.Button.FIRE.getX()+23, GUI.Button.FIRE.getY()+30, Color.WHITE,24);

        }else{
            game.drawBitmap(panel, 360 , 300, 360, 0, 120, 40);
            game.drawText(typeface, GUI.Button.CANCEL.getTitel(), GUI.Button.CANCEL.getX()+17, GUI.Button.CANCEL.getY()+30, Color.WHITE, 20);
        }
    }
}
