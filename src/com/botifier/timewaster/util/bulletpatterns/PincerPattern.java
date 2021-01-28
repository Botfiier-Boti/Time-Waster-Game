package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class PincerPattern extends BulletPattern {

	public PincerPattern() {
		fireSpeed = 1f;
		mindamage = 40;
		maxdamage = 60;
		bulletSpeed = 100;
		duration = 450;
		atkScaling = true;
		enemyPierce = true;
		shots = 2;
		offset = 1f;
		aOffset = 90;
		aMod = -9;
		hasShadow = false;
		override = MainGame.getImage("shiny");
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, x, y, angle, i);
			aOffset = -aOffset;
			aMod = -aMod;
		}
	}

}
