package com.botifier.timewaster.entity.enemy;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.*;
import com.botifier.timewaster.util.behaviors.CircleBehavior;
import com.botifier.timewaster.util.movements.EnemyController;

public class SnakeHead extends Enemy {
	long shotdelay = 1500;

	public SnakeHead(float x, float y, int length) {
		super("SnakeHead", MainGame.getImage("Head"), new EnemyController(x,y,60,1f,100, false), null, null);
		setup(x,y,0,length);
	}
	
	public SnakeHead(float x, float y, int length, double angle) {
		super("SnakeHead", MainGame.getImage("Head"), new EnemyController(x,y,60,1f,100, false), null, null);
		setup(x,y,angle,length);
	}
	
	private void setup(float x, float y, double angle, int length) {
		linger = false;
		invulnerable = true;
		healthbarVisible = false;
		spawncap = 1000;
		setMaxHealth(4500, true);
		posMod.y = 3;
		atk = 10;
		def = 50;
		CircleBehavior fb = new CircleBehavior(this,Math.toRadians(angle));
		fb.setCirclePos(x, y);
		fb.setRadius(6);
		behaviors.add(fb);
		currentBehavior = 0;
		for (int i = 0; i < length; i++) {
			addFollower(new SnakeBody(x,y,this));
		}
	}

	@Override 
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (shotdelay > 0)
			shotdelay -= delta;
		float angle = Math2.calcAngle(getLocation(), ((CircleBehavior)behaviors.get(0)).getCirclePos());
		if (cooldown <= 0 && shotdelay <=0) {
			Bullet bul = Bullet.createBullet("Bob", getController().getLoc().x, getController().getLoc().y, 100, angle, 3000, 75, 0,this,false,true,true,false,false);
			bul.setImage(MainGame.getImage("Shiny"));
			b.add(bul);
			cooldown = (int)(60/(4f + 6.5f*((dex+bDex)/75f)));
		}
		//CircleBehavior cb = (CircleBehavior)behaviors.get(0);
		//if (cls != null && cls.getController().isMoving())
			//cb.setCirclePos(cls.getLocation());*/
	}
}