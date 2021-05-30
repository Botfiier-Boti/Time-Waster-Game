package com.botifier.timewaster.util;

import org.newdawn.slick.Image;

public abstract class Item {
	
	public static enum SlotType {
		EQUIP_NONE(0),
		EQUIP_WEAPON(1),
		EQUIP_ARMOR(2),
		EQUIP_RING(3),
		EQUIP_ABILITY(4),
		EQUIP_HAT(5);
		
		int id;
		SlotType(int i) {
			id = i;
		}
		
		public int getID() {
			return id;
		}
		
	}
	
	private int id;
	private String name;
	private SlotType slotType;
	private Image image;
	
	public Item(int id, String name, SlotType slotType, Image image) {
		this.id = id;
		this.name = name;
		this.slotType = slotType;
		this.image = image;
	}
	
	public abstract void onUse(Entity user);
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public SlotType getSlotType() {
		return slotType;
	}
	
	public Image getImage() {
		return image;
	}
	
	
	@Override
	public String toString() {
		return name;
	}

}
