package com.botifier.timewaster.util.movements;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class EnemyController extends EntityController {
	long paitence = 1000;
	Random r = new Random();
	private float wanderMod = 1;
	long wanderCooldown = 0;
	long cooldown = 0;
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

	public void wander(boolean force, float rangeMult) {
		fleeing = false;
		Entity e = owner;
		float rad = 0;
		int nx = 0, ny = 0;
		double theta =  (Math.random()*2*Math.PI);
		if (wanderArea != null) {
			rad =(float) ((wanderArea.getRadius()*rangeMult)*Math.sqrt(Math.random()));
			nx = (int) (wanderArea.getCenterX() + rad * Math.cos(theta));
			ny = (int) (wanderArea.getCenterY() + rad * Math.sin(theta));
		} else {
			rad = (float) (((e.influence*rangeMult)*Math.sqrt(Math.random())));
			nx = (int) (e.getLocation().x + rad * Math.cos(theta));
			ny = (int) (e.getLocation().y + rad * Math.sin(theta));
		}
		nx = (((int)nx/16)*16)+8;
		ny = (((int)ny/16)*16)+8;
		if (force) {
			dst = null;
			setDestination(nx, ny); 
			return;
		} else if (cooldown <= 0) {
			if (nx > src.getX() || nx < src.getX() || ny > src.getY() || ny < src.getY()) {
				//Vector2f v = new Vector2f(nx, ny);
				if (MainGame.getCurrentMap().blocked(null, nx, ny) == false) {
					dst = null;
					setDestination(nx, ny); 
					cooldown = (long) ((Math.random()*(wanderCooldown*0.75))+wanderCooldown*0.5);
				}
			}
		}else if (cooldown > 0)
			cooldown--;
		
	}
	
	public void dash(float nx, float ny) {
		fleeing = false;
		stop();
		Vector2f v = new Vector2f(nx, ny);
		setDestination(v.x, v.y); 
	}
	
	public void dash(Vector2f v) {
		dash(v.x, v.y);
	}
	
	
	public void flee(float nx, float ny) {
		fleeing = true;
		Vector2f v = new Vector2f(nx, ny);
		setDestination(v.x, v.y); 
	}
	
	public EnemyController copy() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		try {
			return getClass().getConstructor(float.class, float.class, float.class, float.class, long.class, boolean.class).newInstance(src.x,src.y,wanderMod,wanderCooldown,obeysCollision);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
}
