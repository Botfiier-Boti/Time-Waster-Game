package com.botifier.timewaster.util;

import java.util.UUID;

import org.newdawn.slick.Image;

/**
 * Generic Item class
 * @author Botifier
 *
 */
public abstract class Item {
	
	/**
	 * SlotType enumerator 
	 * @author Botifier
	 *
	 */
	public static enum SlotType {
		EQUIP_NONE(0), //Item has no equip type
		EQUIP_WEAPON(1), //Designated slot for weapons
		EQUIP_ARMOR(2), //Designated slot for armor
		EQUIP_RING(3), //Designated slot for rings
		EQUIP_ABILITY(4), //Designated slot for abilities
		EQUIP_HAT(5), //Designated slot for hats
		EQUIP_CONSUMABLE(6); //Designated slot for consumables
		
		/**
		 * The slot ID
		 */
		int id;
		
		/**
		 * SlotType constructor
		 * @param i int the slot ID
		 */
		SlotType(int i) {
			id = i;
		}
		
		/**
		 * Returns the slot ID number
		 * @return int ID number
		 */
		public int getID() {
			return id;
		}
		
	}
	
	/**
	 * The item ID
	 */
	private int id;
	/**
	 * The name of the item
	 */
	private String name;
	/**
	 * The lore of the item
	 */
	private String lore = "This is an item.";
	/**
	 * The item's SlotType
	 */
	private SlotType slotType;
	/**
	 * The image that the item uses
	 */
	private Image image;
	/**
	 * The inventory that the item exists within
	 */
	private Inventory container;
	
	/**
	 * Item's uuid
	 */
	private UUID uuid;
	
	/**
	 * Item constructor
	 * @param id int Item ID
	 * @param name String Item name
	 * @param slotType SlotType Item SlotType
	 * @param image Image Item image
	 */
	public Item(int id, String name, SlotType slotType, Image image) {
		this.id = id;
		this.name = name;
		this.slotType = slotType;
		this.image = image;
		this.uuid = UUID.randomUUID();
	}
	
	/**
	 * Action taken when a user tries to use this item
	 * @param user
	 */
	public abstract void onUse(Entity user);
	
	/**
	 * Sets the lore of this item
	 * @param s String Lore to set
	 */
	public void setLore(String s) {
		this.lore = s;
	}
	
	/**
	 * Sets the inventory that this item is in
	 * @param container Inventory Within
	 */
	public void setContainer(Inventory container) {
		this.container = container;
	}
	
	/**
	 * Returns the Item ID
	 * @return int Item ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Return the Item's name
	 * @return String Item name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the Item's SlotType
	 * @return SlotType Item SlotType
	 */
	public SlotType getSlotType() {
		return slotType;
	}
	
	/**
	 * Returns the Item's Image
	 * @return Image Item Image
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Returns the Item's container (Inventory that it exists in)
	 * @return Inventory Item container
	 */
	public Inventory getContainer() {
		return container;
	}
	
	/**
	 * Returns the Item's lore
	 * @return String Item lore
	 */
	public String getLore() {
		return lore;
	}
	
	/**
	 * Returns the Item's UUID
	 * @return UUID Item UUID
	 */
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
