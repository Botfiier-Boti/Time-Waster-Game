package com.botifier.timewaster.util.behaviors;

import java.sql.Time;

import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.statuseffect.effects.SpeedBoostEffect;
import com.botifier.timewaster.util.Behavior;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;

public class DashBehavior extends Behavior {
	Vector2f target;
	long cooldown = 80;
	long timer = 5;
	float targetingRadius = 80;
	int dashSpeedBonus = 15;
	boolean autoTarget = false;
	boolean dashing = false;
	

	public DashBehavior(Enemy owner) {
		super(owner);
	}

	@Override
	public void run() {
		if (dashing && dashSpeedBonus > 0 && getOwner().getStatusEffectManager() != null) {
			getOwner().getStatusEffectManager().addEffect(new SpeedBoostEffect(1, dashSpeedBonus));
		}
		if (timer <= 0) {
			if (autoTarget == true && getOwner().getController().isMoving() == false && getOwner().seen == true)
				findTarget();
			if (target != null) {
				getOwner().getController().dash(target);
				dashing = true;
				timer = cooldown;
			} else {
				timer = 10;
			}
		} else {
			if (getOwner().getController().isMoving() == false && target != null)
				target = null;
			if (target == null)
				dashing = false;
			timer--;
		}

	}
	
	public void setBonusSpeed(int i) {
		this.dashSpeedBonus = i;
	}
	
	public void setAutomatic(boolean autoTarget) {
		this.autoTarget = autoTarget;
	}
	
	public void setCoolodown(long cooldown) {
		this.cooldown = cooldown;
	}
	
	public void setTarget(Vector2f target) {
		this.target = target;
	}
	
	public boolean isDashing() {
		return dashing;
	}
	
	public int getBonusSpeed() {
		return dashSpeedBonus;
	}
	
	public Vector2f getTarget() {
		return target;
	}
	
	public Entity findTarget() {
		Entity closest = MainGame.getEntityManager().findClosestEnemy(getOwner(), targetingRadius);	
		/*float closestDist = targetingRadius;
		for (int i = MainGame.getEntityManager().getEntities().size()-1; i >= 0; i--) {
			Entity e = MainGame.getEntityManager().getEntities().get(i);
			if (e == null || e == getOwner())
				continue;
			float dist = e.getLocation().distance(getOwner().getLocation());
			if (e.team == getOwner().team )
				continue;
			if (dist > targetingRadius)
				continue;
			if (dist > closestDist)
				continue;
			if (e.destroy == true || e.visible == false || e.active == false)
				continue;
			closest = e;
			closestDist = dist;
		}*/
		if (closest != null) {
			setTarget(closest.getLocation());
		}
		return closest;
	}

}
