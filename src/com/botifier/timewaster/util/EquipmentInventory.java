package com.botifier.timewaster.util;

import com.botifier.timewaster.util.Item.SlotType;

/**
 * Equipment Inventory
 * Handles equipped items
 * @author Botifier
 *
 */
public class EquipmentInventory extends Inventory {
	/**
	 * The type of items that can go in slots
	 */
	SlotType[] slots;
	
	/**
	 * Equipment Inventory contructor
	 * @param owner Entity Owner of this inventory
	 * @param cap int The amount of slots in the inventory
	 */
	public EquipmentInventory(Entity owner, int cap) {
		super(owner, cap);
		slots = new SlotType[cap];
		for (int i = 0; i < cap; i++) {
			slots[i] = SlotType.EQUIP_NONE;
		}
	}
	
	@Override
	public boolean addItem(Item i, int slot) {
		if (slot < 0 || slot >= getCap())
			return false;
		if (i != null && slots[slot] != i.getSlotType())
			return false;
		if (i != null && i.getContainer() != this)
			i.setContainer(this);
		if (items[slot] != null) {
			Item save = i;
			overrideItem(save, slot);
			return true;
		}
		items[slot] = i;
		if (i != null) {
			getItem(slot).onUse(getOwner());
		}
		return true;
	}
	
	@Override
	public boolean addItemAuto(Item i) {
		for (int e = 0; e < getCap(); e++) {
			if (getItem(e) == null && slots[e] == i.getSlotType()) {
				if (i != null && i.getContainer() != this)
					i.setContainer(this);
				items[e] = i;
				if (getItem(e) != null) {
					getItem(e).onUse(getOwner());
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean moveItem(int from, int to) {
		if (from < 0 || from >= getCap())
			return false;
		if (to < 0 || to >= getCap())
			return false;
		if (items[from].getSlotType() != slots[to])
			return false;
		Item hold = items[to];
		items[to] = items[from];
		items[from] = hold;
		if (items[to] != null) {
			Item ei = items[to];
			ei.onUse(getOwner());
		}
		if (items[from] != null) {
			Item ei = items[from];
			ei.onUse(getOwner());
		}
		return true;
	}
	
	/**
	 * Removes an item from the inventory
	 * @param i int Slot to remove item from
	 * @return boolean Whether or not the removal succeeded
	 */
	public boolean removeItem(int i) {
		if (i < 0 || i >= getCap())
			return false;
		if (items[i] == null)
			return false;
		Item ei = items[i];
		ei.onUse(getOwner());
		items[i] = null;
		return true;
	}
	
	/**
	 * Sets the SlotType at position
	 * @param slotType The new SlotType
	 * @param pos int Position in which to modify
	 * @return boolean Whether or not the SlotType was changed
	 */
	public boolean setSlotType(SlotType slotType, int pos) {
		if (pos < 0 || pos >= getCap())
			return false;
		slots[pos] = slotType;
		return true;
	}

	/**
	 * Gets the SlotType at position
	 * @param pos int The position to check
	 * @return SlotType The SlotType at position
	 */
	public SlotType getSlotType(int pos) {
		if (pos < 0 || pos >= getCap())
			return SlotType.EQUIP_NONE;
		return slots[pos];
	}
	
}
