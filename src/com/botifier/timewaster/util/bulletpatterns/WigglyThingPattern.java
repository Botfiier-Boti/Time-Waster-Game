package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.util.Entity;

public class WigglyThingPattern extends BulletPattern {
	int origin = 0;
	
	public WigglyThingPattern() {
		fireSpeed = 0.1f;
		bulletSpeed = 30f;
		duration = 25000;
		shots = 10;
		spread = 0;
		mindamage = 200; 
		atkScaling = false;
		armorPierce = true;
		amplitude = 0.5f;
		frequency = 4f;
		enemyPierce = true;
		wavy = true;
		itShots = false;
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		origin = (int) (Math.random()*shots);
		for (int i = 0; i < shots; i++) {
			float fireLoc = 15*i;
			if (i > origin+1 || i < origin-1)
				createBullet(owner, x-fireLoc, y, angle, i);
		}
		for (int i = 0; i < shots; i++) {
			float fireLoc = 15*i;
			if (i > origin+1 || i < origin-1)
				createBullet(owner, x+fireLoc, y, angle, i);
		}
		shotIt++;
	}

}
