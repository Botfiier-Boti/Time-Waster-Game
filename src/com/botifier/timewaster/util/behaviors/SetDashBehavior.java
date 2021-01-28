package com.botifier.timewaster.util.behaviors;

import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.Behavior;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Math2;

public class SetDashBehavior extends Behavior {
	public boolean dashing = false;
	Vector2f target = null;
	float angle = 0f;
	long distanceDashed = 0;
	long baseDashDistance = 80;
	
	public SetDashBehavior(Enemy owner) {
		super(owner);
	}

	@Override
	public void run() {
		if (distanceDashed < baseDashDistance) {
			dashing = true;
			float nx = (float)Math.cos(angle)*(getOwner().getController().getPPS()*2*getOwner().size);
			float ny = (float)Math.sin(angle)*(getOwner().getController().getPPS()*2*getOwner().size);
			Vector2f v = new Vector2f(getOwner().getLocation().x+nx, getOwner().getLocation().y+ny);
			getOwner().angle = angle;
			getOwner().getController().dash(v.x, v.y);
			distanceDashed++;
		} else {
			dashing = false;
			getOwner().getController().stop();
		}

	}

	public void setTarget(Vector2f target) {
		this.target = target;
		dashing = true;
		if (target != null)
			angle =  Math2.calcAngle(getOwner().getController().src, target);
		distanceDashed = 0;
	}
	
	public void setDashDistance(long distance) {
		baseDashDistance = distance;
	}
	
	public void endDash() {
		distanceDashed = baseDashDistance;
		target = null;
		dashing = false;
	}
	
	public float getAngle() {
		return angle;
	}
}
