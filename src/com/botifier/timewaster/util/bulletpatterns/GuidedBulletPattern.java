package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.statuseffect.effects.SlowEffect;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Entity;

public class GuidedBulletPattern extends BulletPattern {

	public GuidedBulletPattern() {
		fireSpeed = 1f;
		atkScaling = true;
		bulletSpeed = 100;
		duration = 1000;
		mindamage = 15;
		shots = 1;
		targeted = true;
		armorPierce = true;
		override = MainGame.getImage("Torpedo");
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		this.target = target;
		for (int i = 0; i < shots; i++) {
			Bullet b = createBullet(owner, x, y, angle,i);
			b.effect = new SlowEffect(5000,5, 5);
		}
	}

}
