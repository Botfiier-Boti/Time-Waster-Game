package com.botifier.timewaster.util.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.AbilityItem;
import com.botifier.timewaster.util.ConsumableItem;
import com.botifier.timewaster.util.EquipmentInventory;
import com.botifier.timewaster.util.EquippableItem;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.Inventory;
import com.botifier.timewaster.util.Item;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.StackableItem;

public class InventoryComponent extends Component {
	Inventory target = null;
	ArrayList<RectangleComponent> slots = new ArrayList<RectangleComponent>();
	boolean quickClick = false;

	public InventoryComponent(GUI g, Color c, float x, float y, boolean outline, Inventory target) {
		super(g, c, x, y, outline);
		this.target = target;
		if (target != null) {
			int size = target.getCap();
			for (int i = 0; i < size; i++) {
				slots.add(new RectangleComponent(g, c, x+(i*32*MainGame.windowScale-((i/4)*128*MainGame.windowScale))+4+(i*1-((i/4)*4)), y+((i/4)*32*MainGame.windowScale)+(MainGame.windowScale*(i/4)), 32*MainGame.windowScale, 32*MainGame.windowScale, true));
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
		
		if (target != null) {
			if (quickClick) {
				int e = -1;
				//I wish I could use a switch statement here, but Slick2d...
				if (in.isKeyPressed(Input.KEY_1)) {
					e = 0;
				} else if (in.isKeyPressed(Input.KEY_2)) {
					e = 1;
				} else if (in.isKeyPressed(Input.KEY_3)) {
					e = 2;
				} else if (in.isKeyPressed(Input.KEY_4)) {
					e = 3;
				} else if (in.isKeyPressed(Input.KEY_5)) {
					e = 4;
				} else if (in.isKeyPressed(Input.KEY_6)) {
					e = 5;
				} else if (in.isKeyPressed(Input.KEY_7)) {
					e = 6;
				} else if (in.isKeyPressed(Input.KEY_8)) {
					e = 7;
				}
				Item i = target.getItem(e);
				if (i != null && i instanceof ConsumableItem) {
					i.onUse(target.getOwner());
				}
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
							MainGame.getImage("weaponslot").draw(r.getX(), r.getY(), r.getWidth(), r.getHeight());
							break;
						case EQUIP_ABILITY:
							MainGame.getImage("abilityslot").draw(r.getX(), r.getY(), r.getWidth(), r.getHeight());
							break;
						case EQUIP_ARMOR:
							MainGame.getImage("armorslot").draw(r.getX(), r.getY(), r.getWidth(), r.getHeight());
							break;
						case EQUIP_RING:
							MainGame.getImage("ringslot").draw(r.getX(), r.getY(), r.getWidth(), r.getHeight());
							break;
						default:
							break;
					}
				}
				continue;
			}
			if (it.getImage() != null) {
				it.getImage().draw(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				if (it instanceof StackableItem) {
					StackableItem si = (StackableItem)it;
					g.setColor(Color.white);
					g.drawString(si.getCurrentAmount()+"", r.getX()+32*MainGame.windowScale-(g.getFont().getWidth(si.getCurrentAmount()+"")), r.getY()+22*MainGame.windowScale);
				}
				if (it instanceof AbilityItem) {
					AbilityItem ai = (AbilityItem)it;
					long time = ai.getLastTimeUsed()-System.currentTimeMillis();
					if (Math.abs(time) < ai.getAbilityCooldown()) {
						Color transparentBlack = new Color(0, 0, 0, 0.4f);
						g.setColor(transparentBlack);
						g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
						g.setColor(Color.white);
						String timeLeft = ""+(time/1000+ai.getAbilityCooldown()/1000);
						g.drawString(timeLeft, r.getX()+(32*MainGame.windowScale-(g.getFont().getWidth(timeLeft)))/2, r.getY()+(32*MainGame.windowScale-(g.getFont().getHeight(timeLeft)))/2);
					}
				}
			}
		}
		
		for (int i = slots.size()-1; i >= 0; i--) {
			RectangleComponent r = slots.get(i);
			Item it = target.getItem(i);
			if (r.contains(in.getAbsoluteMouseX(), in.getAbsoluteMouseY())) {
				g.setColor(Color.white);
				g.drawRect(r.getX()+1, r.getY()+1, r.getWidth()-1, r.getHeight()-1);
				if (it != null) {
					String lore = it.getName()+'\n';
					lore += it.getLore()+'\n';
					if (it instanceof EquippableItem) {
						EquippableItem ei = (EquippableItem)it;
						lore += ei.getStatText()+'\n';
					} 
					lore += it.getSlotType().toString();
					float loreWidth = g.getFont().getWidth(lore);
					float loreHeight = g.getFont().getHeight(lore);
					g.setColor(Color.darkGray);
					g.fillRect(r.getX()-loreWidth-2, r.getY()-2, loreWidth+4, loreHeight+4);
					g.setColor(Color.gray);
					g.fillRect(r.getX()-loreWidth-2, r.getY()-2, loreWidth+4, g.getFont().getHeight("E"));
					g.drawRect(r.getX()-loreWidth-2, r.getY()-2, loreWidth+4, loreHeight+4);
					g.setColor(Color.white);
					g.drawString(lore, r.getX()-loreWidth, r.getY());
				}else {
					if (target instanceof EquipmentInventory) {
						EquipmentInventory ie = (EquipmentInventory)target;
						g.drawString(ie.getSlotType(i)+"", r.getX()-g.getFont().getWidth(ie.getSlotType(i)+"")+32, r.getY());
					}
				}
			}
		}
	}
	
	public void setUseInput(boolean b) {
		quickClick = b;
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
