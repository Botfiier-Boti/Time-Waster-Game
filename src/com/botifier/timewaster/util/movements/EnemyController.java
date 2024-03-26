package com.botifier.timewaster.util.movements;

import java.util.Random;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class EnemyController extends EntityController {
	long paitence = 1000;
	Random r = new Random();
	public boolean outside = true;
	private float wanderMod = 1;
	public long wanderCooldown = 0;
	public long cooldown = 0;
	public Circle wanderArea = null;
	
	public EnemyController(float x, float y, float wanderMod, long wanderCooldown) {
		super(x, y);
		this.obeysCollision = true;
		this.wanderMod = wanderMod;
		this.wanderCooldown = wanderCooldown;
	}
	
	public EnemyController(float x, float y, float wanderMod, long wanderCooldown, boolean obeysCollision) {
		super(x, y, obeysCollision);
		this.wanderMod = wanderMod;
		this.wanderCooldown = wanderCooldown;
	}
	
	@Override
	public void move(int delta) {
		super.move(delta);
		if (cooldown > 0)
			cooldown--;
	}

	public boolean wander(boolean force, float rangeMult) {
		if (isMoving() == true)
			return false;
		fleeing = false;
		Entity e = owner;
		float rad = 0;
		int nx = 0, ny = 0;
		double theta =  (Math.random()*2*Math.PI)*wanderMod;
		if (wanderArea != null) {
			rad =(float) ((wanderArea.getRadius()*rangeMult)*Math.sqrt(Math.random()));
			nx = (int) (wanderArea.getCenterX() + rad * (r.nextBoolean() == true ? -1 : 1 ) * Math.cos(theta));
			ny = (int) (wanderArea.getCenterY() + rad * (r.nextBoolean() == true ? -1 : 1 ) * Math.sin(theta));
		} else {
			rad = (float) (((e.getInfluence()*rangeMult)*Math.sqrt(Math.random())));
			nx = (int) (e.getLocation().x + rad * (r.nextBoolean() == true ? -1 : 1 ) * Math.cos(theta));
			ny = (int) (e.getLocation().y + rad * (r.nextBoolean() == true ? -1 : 1 ) * Math.sin(theta));
		}
		if (force) {
			dst = null;
			setDestination(nx, ny); 
			return true;
		} else if (cooldown <= 0) {
			if (nx > src.getX() || nx < src.getX() || ny > src.getY() || ny < src.getY()) {
				//Vector2f v = new Vector2f(nx, ny);
				if ((outside == false && (nx/16 > MainGame.getCurrentMap().getWidthInTiles()-1 || ny/16 > MainGame.getCurrentMap().getHeightInTiles()-1 || nx/16 < 0 || ny/16 < 0))) {
					return false;
				} else if (MainGame.getCurrentMap().blocked(nx/16, ny/16) == false) {
					dst = null;
					setDestination(nx, ny); 
					cooldown = (long) ((Math.random()*(wanderCooldown*0.75))+wanderCooldown*0.5);
					return true;
				} else {
					return wander(force, rangeMult);
				}
			}
		}
		return false;
		
	}
	
	public void dash(float nx, float ny) {
		fleeing = false;
		stop();
		setDestination(nx, ny); 
	}
	
	public void dash(Vector2f v) {
		dash(v.x, v.y);
	}
	
	
	public void flee(float nx, float ny) {
		fleeing = true;
		Vector2f v = new Vector2f(nx, ny);
		setDestination(v.x, v.y); 
	}
}
