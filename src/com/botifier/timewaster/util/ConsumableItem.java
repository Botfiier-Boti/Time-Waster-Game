package com.botifier.timewaster.util;

import org.newdawn.slick.Image;

/**
 * Consumable Item Base
 * @author Botifier
 *
 */
public abstract class ConsumableItem extends StackableItem {

	/**
	 * Consumable constructor
	 * @param id int Item ID
	 * @param name String Item name
	 * @param slotType SlotType The type of slot this item fits in
	 * @param image Image 
	 * @param maxStacks int How high this item can stack
	 */
	public ConsumableItem(int id, String name, SlotType slotType, Image image, int maxStacks) {
		super(id, name, slotType, image, maxStacks);
	}

	@Override
	public void onUse(Entity user) {
		if (user != null) {
			if (getCurrentAmount() > 0) {
				if (onConsume(user)) {
					subtract(1);
					if (getContainer() != null && getCurrentAmount() <= 0 )
						getContainer().removeItem(getContainer().getItemPos(this));
				}
			}
		}
	}
	
	/**
	 * Action taken when the item is consumed
	 * @param user Entity The entity using this item
	 * @return boolean Whether or not consumption succeeded.
	 */
	public abstract boolean onConsume(Entity user);

}
