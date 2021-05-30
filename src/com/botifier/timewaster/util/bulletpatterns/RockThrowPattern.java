package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.projectile.Rock;
import com.botifier.timewaster.util.Entity;

public class RockThrowPattern extends BulletPattern {
	float distance = 50;
	public boolean dir = false;

	public RockThrowPattern() {
		fireSpeed = 0.20f;
		//lob = true;
	}

	@Override
	public void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException {
		fire(owner,x,y,angle,distance);
	}
	
	public void fire(Entity owner, float x, float y, float angle, float distance) throws SlickException {
		if (dir) {
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x+distance,owner.getLocation().y+distance), owner));
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x-distance,owner.getLocation().y+distance), owner));
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x+distance,owner.getLocation().y-distance), owner));
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x-distance,owner.getLocation().y-distance), owner));
			dir = false;
		} else {
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x,owner.getLocation().y+distance*1.35f), owner));
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x-distance*1.35f,owner.getLocation().y), owner));
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x,owner.getLocation().y-distance*1.35f), owner));
			owner.b.add(new Rock(owner.getLocation().getX(), owner.getLocation().getY(), new Vector2f(owner.getLocation().x+distance*1.35f,owner.getLocation().y), owner));
			dir = true;
		}
	}

}
