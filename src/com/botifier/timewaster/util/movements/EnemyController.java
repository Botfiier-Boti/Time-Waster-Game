package com.botifier.timewaster.util.movements;

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
		if (isMoving() == true)
			return;
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
			rad = (float) (((e.influence*rangeMult)*Math.sqrt(Math.random())));
			nx = (int) (e.getLocation().x + rad * (r.nextBoolean() == true ? -1 : 1 ) * Math.cos(theta));
			ny = (int) (e.getLocation().y + rad * (r.nextBoolean() == true ? -1 : 1 ) * Math.sin(theta));
		}
		if (force) {
			dst = null;
			setDestination(nx, ny); 
			return;
		} else if (cooldown <= 0) {
			if (nx > src.getX() || nx < src.getX() || ny > src.getY() || ny < src.getY()) {
				//Vector2f v = new Vector2f(nx, ny);
				if (MainGame.getCurrentMap().blocked(nx/16, ny/16) == false) {
					dst = null;
					setDestination(nx, ny); 
					cooldown = (long) ((Math.random()*(wanderCooldown*0.75))+wanderCooldown*0.5);
				} else {
					wander(force, rangeMult);
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
}
