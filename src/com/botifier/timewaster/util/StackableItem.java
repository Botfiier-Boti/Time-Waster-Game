package com.botifier.timewaster.util;

import org.newdawn.slick.Image;

public abstract class StackableItem extends Item {
	private int maxStacks = 3;
	private int currentAmount = 1;

	public StackableItem(int id, String name, SlotType slotType, Image image, int maxStacks) {
		super(id, name, slotType, image);
		this.maxStacks = maxStacks;
	}
	
	public void add(int amount) {
		currentAmount = Math.min(currentAmount+amount, maxStacks);
	}
	
	public void subtract(int amount) {
		currentAmount = Math.max(currentAmount-amount, 0);
	}
	
	public int getCurrentAmount() {
		return currentAmount;
	}
	
	public int getStackLimit() {
		return maxStacks;
	}

}
