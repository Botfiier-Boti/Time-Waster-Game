package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;

public class ShotgunPattern extends BulletPattern {
	public ShotgunPattern() {
		fireSpeed = 1f;
		bulletSpeed = 150f;
		duration = 500;
		shots = 5;
		spread=6.5f;
		mindamage = 55;
		maxdamage = 55;
		enemyPierce = true;
		obstaclePierce = false;
		boomerang = false;
		atkScaling = false;
		hasShadow = true;
		override = MainGame.getImage("Boomerang");
	}
	
	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		if (shots == 1){
			createBullet(owner, owner.getLocation().getX(), owner.getLocation().getY(), angle, 0);	
		} else if (shots % 2 != 0) {
			generateBullets(owner, owner.getLocation().getX(), owner.getLocation().getY(), angle, false);
		} else {
			generateBullets(owner, owner.getLocation().getX(), owner.getLocation().getY(), angle, true);
		}
	}
	
	private void generateBullets(Entity owner, float x, float y, float angle, boolean e) throws SlickException {
		for (int i = 0; i < shots; i++) {
			createBullet(owner, x, y, angle, i);	
		}
	}

}
