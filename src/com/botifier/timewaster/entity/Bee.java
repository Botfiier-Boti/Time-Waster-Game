package com.botifier.timewaster.entity;


import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.behaviors.OrbitBehavior;
import com.botifier.timewaster.util.bulletpatterns.GuidedBulletPattern;
import com.botifier.timewaster.util.movements.EnemyController;

public class Bee extends Enemy {
	//Bullet Pattern
	GuidedBulletPattern gb;
	//Hit sound
	Sound s;
	//Shot cooldown
	long cooldown = 0;

	public Bee(float x, float y) {
		super("Bee", MainGame.getImage("Bee"), new EnemyController(x,y, (float) (20+Math.random()*50), 0.5f, 0), null, null);
		s = MainGame.getSound("yalikejazz");
		iModifier = 0.1f;
		maxhealth = 150;
		health = 150;
		atk = 10;
		def = 20000;
		fireSpeed = 1f;
		linger = false;
		behaviors.add(new OrbitBehavior(this));
		((OrbitBehavior)behaviors.get(0)).setRadius(1);
		gb = new GuidedBulletPattern();
		currentBehavior = 0;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		//Destroy if owner dies
		if (getOwner() == null) {
			destroy = true;
			return;
		}
		//Orbit the owner
		((OrbitBehavior)behaviors.get(0)).setTarget(getOwner());
		
		if (cooldown <= 0) {
			Entity cls = null;
			//Find closest target
			for (int i = MainGame.getEntities().size()-1; i > -1; i--) {
				Entity en = MainGame.getEntities().get(i);
				if (en instanceof Bullet || en.isInvincible() || en == this || en.team == team || en.invulnerable == true || en.active == false || en.visible == false || getLocation().distance(en.getLocation()) > influence)
					continue;
				if (cls == null)
					cls = en;
				if (getLocation().distance(en.getLocation()) < getLocation().distance(cls.getLocation())) {
					cls = en;
				}
			}
			//getController().wander(false, 10f);
			//Fire bullet if target is found
			if (cls != null) {
				try {
					float angle = Math2.calcAngle(getController().src,cls.getLocation());
					gb.fire(this, getLocation().getX(),  getLocation().getY(), angle, cls);
					cooldown = (long)(60-60/3.5f*fireSpeed*Math.random());
					attacking = true;
					return;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			} else if (cls == null && attacking == true){
				attacking = false;
			}
		}
		//Die over time
		health -= 0.1f;
		cooldown--;
	}
	
	@Override
	public void onHitByBullet(Bullet b) {
		super.onHitByBullet(b);
	}


}
