package com.botifier.timewaster.util.items;


import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.WeaponItem;
import com.botifier.timewaster.util.bulletpatterns.ConstructEyeLasers;

public class Gun extends WeaponItem {

	public Gun() {
		super(3, "Gun", MainGame.getImage("Construct"), 0, 0, 0, 0, 0, 0, new ConstructEyeLasers());
		setLore("For his neutral special he wields a GUN.");
	}

}
