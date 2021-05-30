package com.botifier.timewaster.util;

import org.newdawn.slick.Image;

public class EquippableItem extends Item {
	protected float hp = 0;
	protected float def = 0;
	protected float atk = 0;
	protected float vit = 0;
	protected float dex = 0;
	protected float spd = 0;
	
	protected Entity owner;

	public EquippableItem(int id, String name, SlotType slotType, Image image, float hp, float def, float atk, float vit, float dex, float spd) {
		super(id, name, slotType, image);
		this.hp = hp;
		this.def = def;
		this.atk = atk;
		this.vit = vit;
		this.dex = dex;
		this.spd = spd;
	}

	@Override
	public void onUse(Entity user) {
		if (owner == null) {
			owner = user;
			owner.getStats().setAllMods(owner.getStats().getHealthMod()+hp, owner.getStats().getDefMod()+def, owner.getStats().getAtkMod()+atk, owner.getStats().getVitMod()+vit, owner.getStats().getDexMod()+dex, owner.getStats().getSpdMod()+spd);
		} else if (owner == user) {
			owner.getStats().setAllMods(owner.getStats().getHealthMod()-hp, owner.getStats().getDefMod()-def, owner.getStats().getAtkMod()-atk, owner.getStats().getVitMod()-vit, owner.getStats().getDexMod()-dex, owner.getStats().getSpdMod()-spd);
			owner = null;
		}
	}
	
	public boolean isEquipped() {
		return owner == null ? false : true;
	}
	
	public Entity getOwner() {
		return owner;
	}

}
