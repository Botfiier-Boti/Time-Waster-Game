package com.botifier.timewaster.util.behaviors;

import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.statuseffect.effects.SpeedBoostEffect;
import com.botifier.timewaster.util.Behavior;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Math2;

public class SetDashBehavior extends Behavior {
	public boolean dashing = false;
	Vector2f target = null;
	float angle = 0f;
	long distanceDashed = 0;
	long baseDashDistance = 150;
	int dashSpeedBonus = 0;
	
	public SetDashBehavior(Enemy owner) {
		super(owner);
	}

	@Override
	public void run() {
		if (dashing && dashSpeedBonus > 0 && getOwner().getStatusEffectManager() != null) {
			getOwner().getStatusEffectManager().addEffect(new SpeedBoostEffect(1, dashSpeedBonus));
		}
		if (distanceDashed < baseDashDistance && dashing) {
			dashing = true;
			float nx = (float)Math.cos(angle)*(getOwner().getController().getPPS()*2*getOwner().getSize());
			float ny = (float)Math.sin(angle)*(getOwner().getController().getPPS()*2*getOwner().getSize());
			Vector2f v = new Vector2f(getOwner().getLocation().x+nx, getOwner().getLocation().y+ny);
			getOwner().setAngle(angle);
			getOwner().getController().dash(v.x, v.y);
			distanceDashed+=getOwner().getController().getPPS();
		} else {
			stop();
		}

	}
	
	public void stop() {
		dashing = false;
		distanceDashed = 0;
		getOwner().getController().stop();
	}
	
	public void setAngle(float angle) {
		dashing = true;
		this.angle = angle;
		distanceDashed = 0;
	}
	
	public Vector2f getTarget() {
		return this.target;
	}

	public void setTarget(Vector2f target) {
		this.target = target;
		dashing = true;
		if (target != null)
			angle =  Math2.calcAngle(getOwner().getLocation(), target);
		distanceDashed = 0;
	}
	
	public void setDashDistance(long distance) {
		baseDashDistance = distance;
	}
	
	public void setBonusSpeed(int i) {
		this.dashSpeedBonus = i;
	}
	
	public int getBonusSpeed() {
		return dashSpeedBonus;
	}
	
	public void endDash() {
		distanceDashed = baseDashDistance;
		target = null;
		dashing = false;
	}
	
	public boolean isDashing() {
		return dashing;
	}
	
	public float getAngle() {
		return angle;
	}
}
