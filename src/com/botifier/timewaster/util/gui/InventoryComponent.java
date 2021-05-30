package com.botifier.timewaster.util.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.EquipmentInventory;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.Inventory;
import com.botifier.timewaster.util.Item;

public class InventoryComponent extends Component {
	Inventory target = null;
	ArrayList<RectangleComponent> slots = new ArrayList<RectangleComponent>();

	public InventoryComponent(GUI g, Color c, float x, float y, boolean outline, Inventory target) {
		super(g, c, x, y, outline);
		this.target = target;
		if (target != null) {
			int size = target.getCap();
			for (int i = 0; i < size; i++) {
				slots.add(new RectangleComponent(g, c, x+(i*32-((i/4)*128))+4+(i*1-((i/4)*4)), y+((i/4)*32)+(1*(i/4)), 32, 32, true));
			}
		}
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);

		Input in = MainGame.mm.getContainer().getInput();
		
		if (isFocused())
			unfocus();
		for (int i = slots.size()-1; i >= 0; i--) {
			RectangleComponent r = slots.get(i);
			if (r.contains(in.getAbsoluteMouseX(), in.getAbsoluteMouseY())) {
				focus();
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		Input in = MainGame.mm.getContainer().getInput();
		for (int i = slots.size()-1; i >= 0; i--) {
			RectangleComponent r = slots.get(i);
			r.draw(g);
			Item it = target.getItem(i);
			if (it == null) {
				if (target instanceof EquipmentInventory) {
					EquipmentInventory ei = (EquipmentInventory)target;
					switch (ei.getSlotType(i)) {
						case EQUIP_WEAPON:
							MainGame.getImage("weaponslot").draw(r.getX(), r.getY(), 32, 32);
							break;
						case EQUIP_ABILITY:
							MainGame.getImage("abilityslot").draw(r.getX(), r.getY(), 32, 32);
							break;
						case EQUIP_ARMOR:
							MainGame.getImage("armorslot").draw(r.getX(), r.getY(), 32, 32);
							break;
						case EQUIP_RING:
							MainGame.getImage("ringslot").draw(r.getX(), r.getY(), 32, 32);
							break;
						default:
							break;
					}
				}
				continue;
			}
			if (it.getImage() != null) {
				it.getImage().draw(r.getX(), r.getY(), 32, 32);
			}
		}

		for (int i = slots.size()-1; i >= 0; i--) {
			RectangleComponent r = slots.get(i);
			Item it = target.getItem(i);
			if (r.contains(in.getAbsoluteMouseX(), in.getAbsoluteMouseY())) {
				g.setColor(Color.white);
				g.drawRect(r.getX()+1, r.getY()+1, r.getWidth()-1, r.getHeight()-1);
				if (it != null) {
					g.drawString(it.getName(), r.getX()-g.getFont().getWidth(it.getName()), r.getY());
					g.drawString(it.getSlotType()+"", r.getX()-g.getFont().getWidth(it.getSlotType()+""), r.getY()+16);
				}else {
					if (target instanceof EquipmentInventory) {
						EquipmentInventory ie = (EquipmentInventory)target;
						g.drawString(ie.getSlotType(i)+"", r.getX()-g.getFont().getWidth(ie.getSlotType(i)+"")+32, r.getY());
					}
				}
			}
		}
	}

	public int slotAt(float x, float y) {
		if (target == null)
			return -1;
		for (int i = slots.size()-1; i >= 0; i--) {
			RectangleComponent r = slots.get(i);
			if (r.contains(x, y)) {
				return i;
			}
		}
		return -1;
	}
	
	public Inventory getInventory() {
		return target;
	}
}
