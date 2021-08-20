package com.botifier.timewaster.util;

import org.newdawn.slick.Image;

/**
 * Equippable Items
 * @author Botifier
 *
 */
public class EquippableItem extends Item {
	/**
	 * How much health this item grants when equipped
	 */
	protected float hp = 0;
	/**
	 * How much defense this item grants when equipped
	 */
	protected float def = 0;
	/**
	 * How much attack this item grants when equipped
	 */
	protected float atk = 0;
	/**
	 * How much vitality this item grants when equipped
	 */
	protected float vit = 0;
	/**
	 * How much dexterity this item grants when equipped
	 */
	protected float dex = 0;
	/**
	 * How much speed this item grants when equipped
	 */
	protected float spd = 0;
	/**
	 * The items owner
	 */
	protected Entity owner;

	/**
	 * 
	 * @param id
	 * @param name
	 * @param slotType
	 * @param image
	 * @param hp
	 * @param def
	 * @param atk
	 * @param vit
	 * @param dex
	 * @param spd
	 */
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
	

	/**
	 * Generates text for lore
	 * @return String Generated text
	 */
	public String getStatText() {
		String s = "";
		if (hp != 0)
			s += (hp > 0 ? "+"+hp : hp)+" HP\n";
		if (atk != 0)
			s += (atk > 0 ? "+"+atk : atk)+" ATK\n";
		if (def != 0)
			s += (def > 0 ? "+"+def : def)+" DEF\n";
		if (vit != 0)
			s += (vit > 0 ? "+"+vit : vit)+" VIT\n";
		if (dex != 0)
			s += (dex > 0 ? "+"+dex : dex)+" DEX\n";
		if (spd != 0)
			s += (spd > 0 ? "+"+spd : spd)+" SPD\n";
		return s;
	}
	
	/**
	 * Whether or not the item is equipped
	 * @return boolean Whether or not this the owner is null
	 */
	public boolean isEquipped() {
		return owner == null ? false : true;
	}
	
	/**
	 * Gets the owner of this item
	 * @return Entity owner
	 */
	public Entity getOwner() {
		return owner;
	}

}
