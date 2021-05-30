package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.util.Entity;

public class DefenseTestPattern extends BulletPattern {

	public DefenseTestPattern() {
		fireSpeed = 0.65f;
		atkScaling = false;
		bulletSpeed = 50;
		duration = 3000;
		mindamage = 1000;
		shots = 1;
	}
	
	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, x, y, angle,i);
		}
	}


}
