package com.botifier.timewaster.util;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public abstract class Ability {

	public enum TargetType {
		TARGET_ENTITY,
		TARGET_USER,
		TARGET_LOCATION;
	}
	
	private String name; 
	private int cost;
	private long cooldown;
	private TargetType style;
	
	public Ability(String name, int cost, long cooldown, TargetType style) {
		this.name = name;
		this.cost = cost;
		this.cooldown = cooldown;
		this.style = style;
	}
	
	public boolean onAbilityUse(Entity user, Entity target, Vector2f location) throws SlickException {
		switch (style) {
			case TARGET_ENTITY:
				return onAbilityUse(user, target);
			case TARGET_USER:
				return onAbilityUse(user);
			case TARGET_LOCATION:
				return onAbilityUse(user, location);
		}
		return false;
	}
	
	public abstract boolean onAbilityUse(Entity user, Entity target) throws SlickException;
	
	public abstract boolean onAbilityUse(Entity user, Vector2f target) throws SlickException;
	
	public abstract boolean onAbilityUse(Entity user) throws SlickException;
	
	public TargetType getTargetingStyle() {
		return style;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCost() {
		return cost;
	}
	
	public long getCooldown() {
		return cooldown;
	}

}
