package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.projectile.Hopper;
import com.botifier.timewaster.util.Entity;

public class HopperPattern extends BulletPattern {

	public HopperPattern() {
		fireSpeed = 3f;
		shots = 180;
		spread = 1;
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			double na = 0;
			double mod = 0;
			if (shots % 2 != 0) {
				mod = (((shots/2)-i)*(spread));
			} else if (shots % 2 == 0 && shots != 0) {
				mod = ((shots/2-i-0.5)*(spread));
			}
			na = Math.toDegrees(angle)-mod;
			Hopper h = new Hopper(x, y, new Vector2f(x+(float)(Math.cos(Math.toRadians(na))*(30)),y+(float)(Math.sin(Math.toRadians(na))*(30))), owner);
			if (i % 2 == 1)
				h.setRotation(-h.getRotation());
			owner.b.add(h);
		}

	}

}
