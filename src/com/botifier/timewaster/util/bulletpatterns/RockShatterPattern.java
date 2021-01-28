package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class RockShatterPattern extends BulletPattern {

	public RockShatterPattern() {
		fireSpeed = 1f;
		bulletSpeed = 50f;
		duration = 1500;
		shots = 5;
		spread= 360/shots;
		mindamage = 75;
		armorPierce = true;
		atkScaling = false;
		hasShadow = true;
		override = MainGame.getImage("smallrock");
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, owner.getLocation().getX(), owner.getLocation().getY(), angle, i);	
		}
	}

}
