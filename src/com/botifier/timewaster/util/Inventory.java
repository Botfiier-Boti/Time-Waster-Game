package com.botifier.timewaster.util;

import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * Generic Inventory class
 * @author Botifier
 *
 */
public class Inventory {
	/**
	 * The owner of this inventory
	 */
	private Entity owner;
	/**
	 * Array containing the items within the inventory
	 */
	protected Item[] items;
	
	/**
	 * Inventory constructor
	 * @param owner Entity The owner of this inventory
	 * @param cap int The max number of items within this inventory
	 */
	public Inventory(Entity owner, int cap) {
		this.owner = owner;
		items = new Item[cap];
	}
	
	/**
	 * Inventory update function. unused
	 * @param gc GameContainer The GameContainer
	 * @param delta int Time since last update
	 * @throws SlickException
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}
	
	/**
	 * Adds specified item to the inventory at the specified slot
	 * @param i Item To add
	 * @param slot int Position
	 * @return boolean Whether the function succeeded
	 */
	public boolean addItem(Item i, int slot) {
		if (slot < 0 || slot >= getCap())
			return false;
		if (i != null && i.getContainer() != this)
			i.setContainer(this);
		if (items[slot] != null) {
			Item save = i;
			i = items[slot];
			items[slot] = save;
			return true;
		}
		items[slot] = i;
		return true;
	}
	
	/**
	 * Overrides the item in the specified slot
	 * @param i Item To Add
	 * @param slot int To override
	 * @return Item The item overridden
	 */
	public Item overrideItem(Item i, int slot) {
		if (slot < 0 || slot >= getCap())
			return null;
		Item old = items[slot];
		removeItem(slot);
		addItem(i, slot);
		return old;
	}
	
	/**
	 * Automatically places specified item in a open slot
	 * @param i Item To add
	 * @return boolean Whether the function succeeded 
	 */
	public boolean addItemAuto(Item i) {
		for (int e = 0; e < getCap(); e++) {
			if (items[e] == null) {
				items[e] = i;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Moves an item from a slot to another potentially swapping items in the process
	 * @param from int Origin
	 * @param to int Destination
	 * @return boolean Whether the function succeeded
	 */
	public boolean moveItem(int from, int to) {
		if (from < 0 || from >= getCap())
			return false;
		if (to < 0 || to >= getCap())
			return false;
		Item hold = items[to];
		items[to] = items[from];
		items[from] = hold;
		return true;
	}
	
	/**
	 * Removes an item from the specified position
	 * @param i int To remove
	 * @return boolean Whether or not the function succeeded
	 */
	public boolean removeItem(int i) {
		if (i < 0 || i >= getCap())
			return false;
		if (items[i] == null)
			return false;
		items[i] = null;
		return true;
	}
	
	/**
	 * Transfers an item from one inventory to another potentially swapping items in the process
	 * @param from Inventory Origin
	 * @param fromPos int Origin position
	 * @param to Inventory Destination
	 * @param toPos int Destination position
	 * @return boolean Whether or not the function succeeded
	 */
	public static boolean transportItem(Inventory from, int fromPos, Inventory to, int toPos) {
		if (fromPos < 0 || fromPos >= from.getCap())
			return false;
		if (from.getItem(fromPos) == null)
			return false;
		Item i = to.getItem(toPos);
		if (to.addItem(from.getItem(fromPos), toPos)) {
			from.overrideItem(i, fromPos);
			return true;
		}
		return false;
	}
	
	/**
	 * Get the position of the specified item if applicable
	 * @param i Item To search 
	 * @return int The position of the item -1 if it does not exist
	 */
	public int getItemPos(Item i) {
		List<Item> l = Arrays.asList(items);
		if (l.contains(i)) {
			return l.indexOf(i);
		}
		return -1;
	}
	
	/**
	 * Gets the item at the specified position
	 * @param i int Position
	 * @return Item At position
	 */
	public Item getItem(int i) {
		if (i < 0 || i > items.length-1)
			return null;
		return items[i];
	}
	
	/**
	 * Returns the item array
	 * @return Item[] Item array
	 */
	public Item[] getItems() {
		return items;
	}
	
	/**
	 * Returns the length of the item array
	 * @return int Array length
	 */
	public int getCap() {
		return items.length;
	}
	
	/**
	 * Removes all items from the inventory
	 */
	public void purge() {
		for (int i = 0; i < getCap(); i++) {
			removeItem(i);
		}
	}
	
	/**
	 * Returns the first open position of the item array
	 * @return int First position that is null
	 */
	public int getFirstOpenPos() {
		for (int i = 0; i < getCap(); i++) {
			if (items[i] == null)
				return i;
		}
		return -1;
	}
	
	/**
	 * Returns the owner of the inventory
	 * @return Entity The owner
	 */
	public Entity getOwner() {
		return owner;
	}

}
