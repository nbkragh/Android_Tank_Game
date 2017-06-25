package nbkproduction.tankgame;
import android.util.Log;

import nbkproduction.tankgame.Engine.GameEngine;

/**
 * Created by Bruger on 24-05-2017.
 */

public class World //gamelogic
{

    GameEngine game;
    private Shell projectile;
    private Explosion explosion;
    private BOB bob;

    final int groundLevel;

    private int focuspoint;
    private boolean impact;
    private int placeBob;
    public World(GameEngine game){
        this.game = game;
        groundLevel = 300;
        focuspoint = 50;
        placeBob = 1850;
        bob = new BOB(placeBob,groundLevel);
        projectile = new Shell(focuspoint, groundLevel-20,45,140);
        impact = false;
    }
    public void update(float deltaTime)
    {
        if(!impact){
            if( projectile.getHeight() <= groundLevel)
            {
                projectile.trajectory(deltaTime);
                focuspoint = (int)projectile.getDistance();
            }else{
                impact = true;
                explosion = new Explosion((int)projectile.getDistance(),groundLevel, 96, 96, 15);
                projectile.setVisible(false);
                if (((explosion.posX+explosion.width)>bob.posX) && (explosion.posX<(bob.posX+bob.width))){//døde bob?
                    bob.killBOB();
                }
            }
        }else{
            if(explosion != null && explosion.update(deltaTime)> explosion.getFrames()){//udfører i evalueringen
                explosion = null;
            }
            if(!bob.isAlive()){
                bob.updateGore(deltaTime);
            }
        }


    }
    public void reset()
    {

        focuspoint = 50;
        impact = false;
        projectile = new Shell(focuspoint, groundLevel-20, 45, 140 );
        bob = new BOB(placeBob,groundLevel);
        explosion = null;
    }
    public Shell getProjectile()
    {
        return projectile;
    }

    public Explosion getExplosion()
    {
        return explosion;
    }

    public int getFocuspoint()
    {
        return focuspoint;
    }


    public void incAngle(){
        if(projectile == null){
            return;
        }Log.d("angle is", ""+getProjectile().getAngle());
        if((projectile.getAngle()+1)>90){
            projectile.setAngle(90);
        }else {

            projectile.setAngle(projectile.getAngle()+1);

        }
    }
    public void decAngle(){

        if(projectile == null){
            return;
        }
        if((projectile.getAngle()-1)<1){
            projectile.setAngle(0);
        }else {
            projectile.setAngle(projectile.getAngle()-1);
        }
    }

    public BOB getBob()
    {
        return bob;
    }
}
