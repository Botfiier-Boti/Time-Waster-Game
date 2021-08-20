package com.botifier.timewaster.util.items;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.WeaponItem;
import com.botifier.timewaster.util.bulletpatterns.GenericSwordPattern;

public class GenericSword extends WeaponItem {

	public GenericSword() {
		super(4, "Generic Sword", MainGame.getImage("GenericSword"), 0, 0, 0, 0, 0, 0, new GenericSwordPattern());
		setLore("Just a Generic Sword,\nnothing special.");
	}

}
