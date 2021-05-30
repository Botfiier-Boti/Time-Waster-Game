package com.botifier.timewaster.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Inventory {
	private Entity owner;
	protected Item[] items;
	
	public Inventory(Entity owner, int cap) {
		this.owner = owner;
		items = new Item[cap];
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		
	}
	
	public boolean addItem(Item i, int slot) {
		if (slot < 0 || slot >= getCap())
			return false;
		if (items[slot] != null) {
			Item save = i;
			i = items[slot];
			items[slot] = save;
			return true;
		}
		items[slot] = i;
		return true;
	}
	
	public Item overrideItem(Item i, int slot) {
		if (slot < 0 || slot >= getCap())
			return null;
		Item old = items[slot];
		removeItem(slot);
		addItem(i, slot);
		return old;
	}
	
	public boolean addItemAuto(Item i) {
		for (int e = 0; e < getCap(); e++) {
			if (items[e] == null) {
				items[e] = i;
				return true;
			}
		}
		return false;
	}
	
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
	
	public boolean removeItem(int i) {
		if (i < 0 || i >= getCap())
			return false;
		if (items[i] == null)
			return false;
		items[i] = null;
		return true;
	}
	
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
	
	public Item getItem(int i) {
		return items[i];
	}
	
	public Item[] getItems() {
		return items;
	}
	
	public int getCap() {
		return items.length;
	}
	
	public void purge() {
		for (int i = 0; i < getCap(); i++) {
			removeItem(i);
		}
	}
	
	public int getFirstOpenPos() {
		for (int i = 0; i < getCap(); i++) {
			if (items[i] == null)
				return i;
		}
		return -1;
	}
	
	public Entity getOwner() {
		return owner;
	}

}
