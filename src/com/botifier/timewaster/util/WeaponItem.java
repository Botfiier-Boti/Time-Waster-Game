package com.botifier.timewaster.util;

import org.newdawn.slick.Image;

import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.util.bulletpatterns.BulletPattern;

public class WeaponItem extends EquippableItem {
	BulletPattern p = null;
	
	public WeaponItem(int id, String name, Image image, float hp, float def, float atk, float vit,
			float dex, float spd, BulletPattern p) {
		super(id, name, SlotType.EQUIP_WEAPON, image, hp, def, atk, vit, dex, spd);
		this.p = p;
	}
	
	@Override
	public void onUse(Entity user) {
		if (owner == null) {
			owner = user;
			if (owner instanceof Player) {
				Player p = (Player) user;
				p.p = this.p;
			}
			owner.getStats().setAllMods(owner.getStats().getHealthMod()+hp, owner.getStats().getDefMod()+def, owner.getStats().getAtkMod()+atk, owner.getStats().getVitMod()+vit, owner.getStats().getDexMod()+dex, owner.getStats().getSpdMod()+spd);
		} else if (owner == user) {
			if (owner instanceof Player) {
				Player p = (Player) user;
				p.p = null;
			}
			owner.getStats().setAllMods(owner.getStats().getHealthMod()-hp, owner.getStats().getDefMod()-def, owner.getStats().getAtkMod()-atk, owner.getStats().getVitMod()-vit, owner.getStats().getDexMod()-dex, owner.getStats().getSpdMod()-spd);
			owner = null;
		}
	}

}
