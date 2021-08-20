package com.botifier.timewaster.util.items;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.ConsumableItem;
import com.botifier.timewaster.util.Entity;

public class Bandage extends ConsumableItem {

	public Bandage(int amount) {
		super(5, "Bandage", SlotType.EQUIP_CONSUMABLE, MainGame.getImage("Bandage"), 6);
		currentAmount = Math.min(amount, getStackLimit());
		setLore("Heals 100 HP");
	}

	@Override
	public boolean onConsume(Entity user) {
		if (user == null)
			return false;
		if (user.getStats().getCurrentHealth() >= user.getMaxHealth())
			return false;
		user.heal(100);
		return true;
	}

}
