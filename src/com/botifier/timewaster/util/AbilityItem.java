package com.botifier.timewaster.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class AbilityItem extends EquippableItem {
	
	/**
	 * The Ability used
	 */
	private Ability a;
	
	/**
	 * Last time in milliseconds the ability was used
	 */
	private long lastTimeUsed = 0;
	
	
	/**
	 * Ability constructor
	 * @param id int Item id
	 * @param name String Item name
	 * @param image Image Item image
	 * @param a Ability Item ability
	 */
	public AbilityItem(int id, String name, Image image, float hp, float def, float atk, float vit,
			float dex, float spd, Ability a) {
		super(id, name, SlotType.EQUIP_ABILITY, image, hp, def, atk, vit, dex, spd);
		this.a = a;
	}
	
	public void onUseAbility(Entity user, Entity target, Vector2f location) {
		long currentTime = System.currentTimeMillis();
		if (currentTime >= lastTimeUsed + a.getCooldown()) {
			boolean success = false;
			try {
				success = a.onAbilityUse(user, target, location);
			} catch (SlickException e) {
				System.out.println("Failed to use ability: "+a.getName());
			} finally {
				if (success)
					lastTimeUsed = currentTime;
			}
		}
	}
	
	/**
	 * Returns the last time the ability was used
	 * @return long Last time used
	 */
	public long getLastTimeUsed() {
		return lastTimeUsed;
	}

	/**
	 * Returns the cooldown of the ability
	 * @return long Cooldown of ability
	 */
	public long getAbilityCooldown() {
		return a.getCooldown();
	}
}
