package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.util.Entity;

public class SpherePattern extends BulletPattern {
	float angle = 0f;
	
	public SpherePattern() {
		fireSpeed = 1f;
		bulletSpeed = 25f;
		duration = 600;
		shots = 8;
		spread = (float) (360/shots);
		mindamage = 15; 
		atkScaling = false;
		armorPierce = true;
		enemyPierce = true;
		boomerang = true;
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, x, y, this.angle, i);	
		}
		this.angle += 360/shots;
	}

}
