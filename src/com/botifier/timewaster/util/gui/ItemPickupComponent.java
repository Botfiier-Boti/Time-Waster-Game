package com.botifier.timewaster.util.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.states.OverworldState;
import com.botifier.timewaster.util.ConsumableItem;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.Inventory;
import com.botifier.timewaster.util.StackableItem;

public class ItemPickupComponent extends Component {
	Inventory held;
	Inventory origin = null;
	int slotOrigin = -1;
	long lastclick = 0;
	public static long doubleClickThreshold = 400;
	
	public ItemPickupComponent(GUI g) {
		super(g, Color.white, 0, 0, false);
		held = new Inventory(null, 1);
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		Input i = MainGame.mm.getInput();

		ArrayList<Component> c = getOwner().getComponentsOfType(InventoryComponent.class);
		boolean clicked = false;
		for (int a = 0; a < c.size(); a++) {
			Component com = c.get(a);
			if (com instanceof InventoryComponent) {
				InventoryComponent ic = (InventoryComponent) com;
				int slot = ic.slotAt(i.getAbsoluteMouseX(), i.getAbsoluteMouseY());
				if (slot >= 0) {
					if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						if ((lastclick <= doubleClickThreshold || i.isKeyDown(Input.KEY_LSHIFT))&& ic.getInventory().getItem(slot) != null && held.getItem(0) == null) {
							if (ic.getInventory().getItem(slot) instanceof ConsumableItem) {
								ConsumableItem it = (ConsumableItem) ic.getInventory().getItem(slot);
								it.onUse(((OverworldState)MainGame.mm.getState(OverworldState.ID)).getPlayer());
								if (it.getCurrentAmount() <= 0)
									ic.getInventory().removeItem(slot);
							}
						} else {
							if (held.getItem(0) != null) {
								if (ic.getInventory().getItem(slot) != null && held.getItem(0) instanceof StackableItem && ic.getInventory().getItem(slot).getClass() == held.getItem(0).getClass()) {
									StackableItem si1 = (StackableItem)held.getItem(0);
									StackableItem si2 = (StackableItem)ic.getInventory().getItem(slot);
									
									int org = si2.getCurrentAmount();
									
									si2.add(si1.getCurrentAmount());
									si1.subtract(si2.getCurrentAmount()-org);
									if (si1.getCurrentAmount() <= 0)
										held.removeItem(0);
									
								} else if (Inventory.transportItem(held, 0, ic.getInventory(), slot)) {
									if (held.getItem(0) != null) {
										Inventory.transportItem(held, 0, origin, slotOrigin);
										origin = ic.getInventory();	
										slotOrigin = slot;
									} else {
										origin = null;	
										slotOrigin = -1;
									}
								}
							}else
								if (Inventory.transportItem(ic.getInventory(), slot, held, 0)) {
									if (held.getItem(0) != null) {
										origin = ic.getInventory();	
										slotOrigin = slot;
									} else {
										origin = null;	
										slotOrigin = -1;
									}
								}
						}
						lastclick = 0;
						clicked = true;
						break;	
					} else if (i.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
						if (held.getItem(0) != null) {
							if (held.getItem(0) instanceof StackableItem && ic.getInventory().getItem(slot) == null) {
								
								
							}
						}
					}
				}
			}
		}
		if (clicked == false) {
			if (held.getItem(0) != null) {
				if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					if (Inventory.transportItem(held, 0, origin, slotOrigin)) {
						if (held.getItem(0) == null) {
							origin = null;	
							slotOrigin = -1;
						}
					}
				}
			}
		}
		if (lastclick < 1000)
			lastclick+=delta;
	}
	
	@Override
	public void draw(Graphics g) {
		Input i = MainGame.mm.getContainer().getInput();
		if (held != null && held.getItem(0) != null)
			held.getItem(0).getImage().draw(i.getAbsoluteMouseX()-16, i.getAbsoluteMouseY()-16, 32, 32);
	}

}
