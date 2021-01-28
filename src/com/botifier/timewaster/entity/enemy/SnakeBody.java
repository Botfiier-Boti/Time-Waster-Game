package com.botifier.timewaster.entity.enemy;


import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.behaviors.CircleBehavior;
import com.botifier.timewaster.util.behaviors.FollowBehavior;
import com.botifier.timewaster.util.movements.EnemyController;

public class SnakeBody extends Enemy{
	long shotdelay = 6000;
	public SnakeBody(float x, float y, Entity owner) {
		super("SnakeBody", MainGame.getImage("Body"), new EnemyController(x,y,200,1f,100,false), null, null);
		setMaxHealth(2000, true);
		o = owner;
		def = 50;
		dex = 1;
		posMod.y = 3;
		linger = false;
		healthbarVisible = false;
		requireForce = true;
		FollowBehavior fb = new FollowBehavior(this);
		fb.setMaxDistance(5);
		behaviors.add(fb);
		currentBehavior = 0;
		invulnerable = true;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (o == null) {
			destroy = true;
			return;
		}
		if (o.destroy == true) {
			destroy = true;
			return;
		}
		FollowBehavior fb = (FollowBehavior)behaviors.get(0);
		//int index = o.spawns.indexOf(this);

		if (shotdelay > 0)
			shotdelay -= delta;
		float angle = (float) (Math2.calcAngle(getLocation(), ((CircleBehavior)((Enemy)getOwner()).behaviors.get(0)).getCirclePos())-Math.PI);
		if (cooldown <= 0 && shotdelay <= 0) {
			Bullet bul = Bullet.createBullet("Bob", getController().getLoc().x, getController().getLoc().y, 70, angle, 4000, 75, 0,this,false,true,false,false,false);
			bul.setImage(MainGame.getImage("smallrock"));
			b.add(bul);
			cooldown = (int)(60/(2.5f + 6.5f*((dex+bDex)/75f)));
		}
		fb.setTarget(o);
		/*if (index > 0) {
			fb.setTarget(o.spawns.get(index-1));
		} else {
			fb.setTarget(o);
		}*/
		getController().speed = o.getController().speed*2;
	}
}
