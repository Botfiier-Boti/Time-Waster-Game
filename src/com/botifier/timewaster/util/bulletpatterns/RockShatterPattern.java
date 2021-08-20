package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class RockShatterPattern extends BulletPattern {

	public RockShatterPattern() {
		fireSpeed = 1f;
		bulletSpeed = 25f;
		duration = 2500;
		shots = 5;
		spread= 360/shots;
		mindamage = 55;
		armorPierce = true;
		atkScaling = false;
		hasShadow = false;
		override = MainGame.getImage("smallrock");
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, owner.getLocation().getX(), owner.getLocation().getY(), angle, i);	
		}
	}

}
