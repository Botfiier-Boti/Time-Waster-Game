package com.botifier.timewaster.util.abilities;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Ability;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.bulletpatterns.BeeHivePattern;

public class BeeThrowAbility extends Ability {
	BeeHivePattern bhp = new BeeHivePattern();

	public BeeThrowAbility() {
		super("Beehive Toss", 0, 5000l, TargetType.TARGET_LOCATION);
	}

	@Override
	public boolean onAbilityUse(Entity user, Entity target) throws SlickException {
		return onAbilityUse(user, target.getLocation());
	}

	@Override
	public boolean onAbilityUse(Entity user, Vector2f target) throws SlickException {
		if (user.getStats().getCurrentHealth() > getCost() && !MainGame.getCurrentMap().blocked((int)(target.x/16), (int)(target.y/16))) {
			bhp.fire(user, target.x, target.y, 0);
			user.getStats().setCurrentHealth(user.getStats().getCurrentHealth()-getCost());
			return true;
		}
		return false;
	}

	@Override
	public boolean onAbilityUse(Entity user) throws SlickException {
		return onAbilityUse(user, user.getLocation());
	}

}
