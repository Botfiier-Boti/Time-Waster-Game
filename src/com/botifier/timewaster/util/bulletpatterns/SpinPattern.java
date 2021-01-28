package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class SpinPattern extends BulletPattern {

	public SpinPattern() {
		fireSpeed = 1f;
		bulletSpeed = 40f;
		duration = 2500;
		mindamage = 25;
		enemyPierce = true;
		armorPierce = true;
		atkScaling = false;
		boomerang = true;
		override = MainGame.getImage("fireball");
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		createBullet(owner, x, y, angle, 0);
		createBullet(owner, x, y, angle-(float)(Math.PI), 0);
		createBullet(owner, x, y, angle+(float)(Math.PI/2), 0);
		createBullet(owner, x, y, angle-(float)(Math.PI/2), 0);
	}

}
