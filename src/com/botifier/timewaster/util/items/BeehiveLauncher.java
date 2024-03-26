package com.botifier.timewaster.util.items;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.AbilityItem;
import com.botifier.timewaster.util.abilities.BeeThrowAbility;

public class BeehiveLauncher extends AbilityItem {

	public BeehiveLauncher() {
		super(6, "Beehive Launcher", MainGame.getImage("Beehive"), 0, 0, 0, 0, 0, 0, new BeeThrowAbility());
		setLore("Chucks a friendly beehive at your enemies!");
	}

}
