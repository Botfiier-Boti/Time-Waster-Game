package com.botifier.timewaster.util.behaviors;

import java.util.LinkedList;

import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.Behavior;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;

public class FollowBehavior extends Behavior{
	Entity target;
	int maxDistance = 5;
	float speed;

	public FollowBehavior(Enemy owner) {
		super(owner);
		speed = owner.getController().speed;
	}

	@Override
	public void run() {
		if (target != null) {
			if (target.getController().isMoving() == false)
				return;
			LinkedList<Vector2f> ll = target.getController().path;
			LinkedList<Entity> f = target.followers;
			if (f.size() <= 0) {
				if (target.getLocation().distance(getOwner().getLocation()) < maxDistance) {
					getOwner().getController().stop();
				} else {	
					getOwner().getController().dash(target.getLocation().x, target.getLocation().y);	
				}
				return;
			}
			if (ll == null) {
				if (target.getLocation().distance(getOwner().getLocation()) < maxDistance) {
					getOwner().getController().stop();
				} else {	
					getOwner().getController().dash(target.getLocation().x, target.getLocation().y);	
				}
			} else {
				if (f.contains(getOwner())) {
					int pos = f.indexOf(getOwner());
					if (ll.size() > (pos+1)*maxDistance) {
						getOwner().getController().dash(ll.get((pos+1)*maxDistance).x, ll.get((pos+1)*maxDistance).y);	
						if (pos  == ll.size()) {
							ll.removeLast();
						}
					}  
				}
			}
		}
	}
	
	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	public int getMaxDistance() {
		return maxDistance;
	}
}
