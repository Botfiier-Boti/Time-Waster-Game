package com.botifier.timewaster.util.items;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.WeaponItem;
import com.botifier.timewaster.util.bulletpatterns.DefenseTestPattern;

public class DefenseTestSword extends WeaponItem {

	public DefenseTestSword() {
		super(2, "Defense Test Sword", MainGame.getImage("GenericSword"), 200, 0, 0, 0, 0, 0, new DefenseTestPattern());
	}

}
