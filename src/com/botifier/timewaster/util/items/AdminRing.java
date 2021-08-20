package com.botifier.timewaster.util.items;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.EquippableItem;

public class AdminRing extends EquippableItem {

	public AdminRing() {
		super(0, "Admin Ring", SlotType.EQUIP_RING, MainGame.getImage("Invulnerable"), Float.MAX_VALUE, Float.MAX_VALUE, 1000, Float.MAX_VALUE, Float.MAX_VALUE, 100);
		setLore("A tiny shield that functions as a ring.");
	}

}
