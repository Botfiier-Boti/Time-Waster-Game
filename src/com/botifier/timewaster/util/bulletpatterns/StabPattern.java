package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Entity;

public class StabPattern extends BulletPattern {
	public int shotMultiplier = 1;
	public StabPattern() {
		fireSpeed = 0.75f;
		bulletSpeed = 35;
		duration = 2700;
		momentumDelay = 0;
		atkScaling = false;
		mindamage = 125;
		shots = 1;
		override = MainGame.getImage("WhiteArrow");
		hasShadow = true;
		boomerang = true;
		enemyPierce = true;
		
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int e = 0; e < shotMultiplier; e++) {
			for (int i = 0; i < shots; i++) {
				float mult = (float) ((Math.PI*2)/shotMultiplier);
				Bullet b = createBullet(owner, owner.getLocation().x, owner.getLocation().y, angle, i, target);
				/*Bullet b = Bullet.createBullet("Dave", owner.getLocation().x, owner.getLocation().y, (bulletSpeed+(bulletSpeed*i)), angle+mult*e, duration, 125-(25*i), 0, owner, obstaclePierce, enemyPierce, boomerang, hasShadow, atkScaling);
				if (override != null)
					b.setImage(override);
				if (e % 2 == 1) {
					b.getController().frequency = -b.getController().frequency;
				}*/
				b.getBaseDamage()[0] = mindamage-(25*i);
				b.getController().angleMod = mult*e;
				b.getController().speed = (bulletSpeed+(bulletSpeed*i));
				owner.b.add(b);
			}
		}
	}
}
