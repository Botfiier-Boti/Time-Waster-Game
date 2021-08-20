package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.util.Entity;

public class GenericSwordPattern extends BulletPattern {

	public GenericSwordPattern() {
		mindamage = 75;
		maxdamage = 100;
		bulletSpeed = 200;
		duration = 300;
		atkScaling = true;
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		super.createBullet(owner, x, y, angle, 0);
	}

}
