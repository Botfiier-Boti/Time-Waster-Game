package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class FlameStreamPattern extends BulletPattern {
	float amod = 0.05f;
	public FlameStreamPattern() {
		fireSpeed = 5f;
		bulletSpeed = 50f;
		duration = 400;
		shots = 2;
		spread= 5f;
		mindamage = 15;
		armorPierce = true;
		enemyPierce = true;
		atkScaling = false;
		override = MainGame.getImage("fireball");
		override.setImageColor(1, 1, 1, 0.75f);
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, owner.getLocation().getX(), owner.getLocation().getY(), angle+amod, i);	
		}
		amod = -amod;
	}

}
