package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.projectile.Beehive;
import com.botifier.timewaster.util.Entity;

public class BeeHivePattern extends BulletPattern {
	
	public BeeHivePattern() {
		fireSpeed = 0.5f;
		//lob = true;
		targeted = true;
	}
	
	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		if (target == null) {
			Beehive bh = new Beehive(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(x,y), owner);
			owner.b.add(bh);	
		} else {
			Beehive bh = new Beehive(owner.getLocation().getX(), owner.getLocation().getY(), target.getLocation(), owner);
			owner.b.add(bh);	
		}
	}

}
