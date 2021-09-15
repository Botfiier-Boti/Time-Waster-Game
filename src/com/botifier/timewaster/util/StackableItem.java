package com.botifier.timewaster.util;

import org.newdawn.slick.Image;

/**
 * Stackable Item class
 * @author Botifier
 *
 */
public abstract class StackableItem extends Item {
	/**
	 * The maximum amount of items in a stack
	 */
	private int maxStacks = 3;
	/**
	 * The current amount of items within the stack
	 */
	protected int currentAmount = 1;

	/**
	 * StackableItem constructor
	 * @param id int Item ID
	 * @param name String Item name
	 * @param slotType SlotType Item SlotType
	 * @param image Image Item image
	 * @param maxStacks int Maximum number of items in a stack
	 */
	public StackableItem(int id, String name, SlotType slotType, Image image, int maxStacks) {
		super(id, name, slotType, image);
		this.maxStacks = maxStacks;
	}
	
	/**
	 * Adds an item to the stack capping at maxStacks
	 * @param amount int To add
	 */
	public void add(int amount) {
		currentAmount = Math.min(currentAmount+amount, maxStacks);
	}
	
	/**
	 * Removes an item from the stack capping at 0
	 * @param amount int To subtract
	 */
	public void subtract(int amount) {
		currentAmount = Math.max(currentAmount-amount, 0);
	}
	
	/**
	 * Returns the current amount of items within the stack
	 * @return int Current amount of items in stack
	 */
	public int getCurrentAmount() {
		return currentAmount;
	}
	
	/**
	 * Returns the maximum stack size of this item
	 * @return int Max stack size
	 */
	public int getStackLimit() {
		return maxStacks;
	}

}
