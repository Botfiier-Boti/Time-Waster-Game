package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class ExplodePattern extends BulletPattern {

	public ExplodePattern() {
		fireSpeed = 0.1f;
		bulletSpeed = 60f;
		duration = 7000;
		shots = 12;
		spread=360/shots;
		mindamage = 100;
		atkScaling = false;
		this.armorPierce = true;
		this.enemyPierce = true;
		this.override = MainGame.getImage("signal");
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, owner.getLocation().getX(), owner.getLocation().getY(), angle, i);	
		}
	}

}
