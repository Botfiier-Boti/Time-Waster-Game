package com.botifier.timewaster.util.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.Inventory;

public class ItemPickupComponent extends Component {
	Inventory held;
	
	public ItemPickupComponent(GUI g) {
		super(g, Color.white, 0, 0, false);
		held = new Inventory(null, 1);
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		Input i = MainGame.mm.getInput();

		ArrayList<Component> c = getOwner().getComponentsOfType(InventoryComponent.class);
		for (int a = 0; a < c.size(); a++) {
			Component com = c.get(a);
			if (com instanceof InventoryComponent) {
				InventoryComponent ic = (InventoryComponent) com;
				int slot = ic.slotAt(i.getAbsoluteMouseX(), i.getAbsoluteMouseY());
				if (slot >= 0) {
					if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						if (held.getItem(0) != null)
							Inventory.transportItem(held, 0, ic.getInventory(), slot);
						else
							Inventory.transportItem(ic.getInventory(), slot, held, 0);
						break;	
					}
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		Input i = MainGame.mm.getContainer().getInput();
		if (held != null && held.getItem(0) != null)
			held.getItem(0).getImage().draw(i.getAbsoluteMouseX()-16, i.getAbsoluteMouseY()-16, 32, 32);
	}

}
