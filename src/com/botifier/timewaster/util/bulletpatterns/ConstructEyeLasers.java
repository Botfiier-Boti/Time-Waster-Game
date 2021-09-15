package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;

public class ConstructEyeLasers extends BulletPattern {

	public ConstructEyeLasers() {
		fireSpeed = 1f;
		bulletSpeed = 300f;
		duration = 600;
		shots = 2;
		spread= 0;
		mindamage = 40;
		armorPierce = true;
		atkScaling = false;
		hasShadow = false;
		predictive = false;
		obstaclePierce = true;
		targeted = true;
		wavy = true;
		amplitude = 2f;
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		for (int i = 0; i < shots; i++) {
			Vector2f loc = new Vector2f(owner.getLocation().x-(i % 2 == 0 ? -1.6666666f : 1.6666666f)*owner.getSize(), owner.getLocation().y-((owner.getHitbox().getHeight()-3.5f*owner.getSize())));
			Vector2f targetA = new Vector2f(x+(float)Math.cos(angle), y+(float)Math.sin(angle));
			if (target != null)
				targetA = target.getLocation();
			createBullet(owner, loc.x, loc.y, Math2.calcAngle(loc, targetA), i, target);
		}
	}

}
