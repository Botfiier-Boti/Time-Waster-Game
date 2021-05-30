package com.botifier.timewaster.util.items;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.WeaponItem;
import com.botifier.timewaster.util.bulletpatterns.RockThrowPattern;

public class AdminRock extends WeaponItem {

	public AdminRock() {
		super(1, "Admin Rock", MainGame.getImage("Rock"), 0, 0, 0, 0, 0, 0, new RockThrowPattern());
	}

}
