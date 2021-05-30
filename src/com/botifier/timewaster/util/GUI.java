package com.botifier.timewaster.util;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.util.gui.*;

public class GUI {
	LinkedList<Component> components = new LinkedList<Component>();
	Component focused;
	Player p;

	public GUI(Player p) {
		this.p = p;
	}
	
	public void update(GameContainer gc, int delta) {
		for (int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if (c == null)
				continue;
			if (c.isEnabled() == false)
				continue;
			c.update(delta);
		}
	}
	
	public void draw(GameContainer gc, Graphics g) {
		/*Old Code
		g.setColor(Color.gray);
		g.fillRect(gc.getWidth() * 0.75f, -1, gc.getWidth() * 0.25f, gc.getHeight() + 1);
		g.setColor(Color.black);
		g.drawRect(gc.getWidth() * 0.75f, -1, gc.getWidth() / 4, gc.getHeight() + 1);
		g.setColor(Color.red);
		g.fillRect(gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f, (gc.getWidth() / 4) - 20, 20);
		g.setColor(Color.green);
		g.fillRect(gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f,
				(p.health / p.maxhealth) * ((gc.getWidth() / 4) - 20), 20);
		g.setColor(Color.black);
		g.drawRect(gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f, (gc.getWidth() / 4) - 20, 20);
		g.setColor(Color.white);
		String pH = (int) p.health + "/" + (int) p.maxhealth;
		g.drawString(pH, gc.getWidth() * 0.75f + 10 + (((gc.getWidth() / 4) - 20 - g.getFont().getWidth(pH)) / 2),
				gc.getHeight() * 0.25f + g.getFont().getLineHeight() / 2);
		String pS = (int) p.atk + " Attack";
		g.drawString(pS, gc.getWidth() * 0.75f + 10 + (((gc.getWidth() / 4) - 20 - g.getFont().getWidth(pS)) / 2),
				gc.getHeight() * 0.05f + g.getFont().getLineHeight());
		pS = (int) p.dex + " Dexterity";
		g.drawString(pS, gc.getWidth() * 0.75f + 10 + (((gc.getWidth() / 4) - 20 - g.getFont().getWidth(pS)) / 2),
				gc.getHeight() * 0.05f + g.getFont().getLineHeight() * 2);
		pS = (int) p.def + " Defense";
		g.drawString(pS, gc.getWidth() * 0.75f + 10 + (((gc.getWidth() / 4) - 20 - g.getFont().getWidth(pS)) / 2),
				gc.getHeight() * 0.05f + g.getFont().getLineHeight() * 3);
		pS = (int) (p.vit) + " Vitality";
		g.drawString(pS, gc.getWidth() * 0.75f + 10 + (((gc.getWidth() / 4) - 20 - g.getFont().getWidth(pS)) / 2),
				gc.getHeight() * 0.05f + g.getFont().getLineHeight() * 4);
		pS = ((int) p.getController().speed) + " Speed";
		g.drawString(pS, gc.getWidth() * 0.75f + 10 + (((gc.getWidth() / 4) - 20 - g.getFont().getWidth(pS)) / 2),
				gc.getHeight() * 0.05f + g.getFont().getLineHeight() * 5);*/
		for (int i = components.size()-1; i > -1; i--) {
			Component c = components.get(i);
			if (c.isVisible())
				c.draw(g);
			g.setColor(Color.white);
		}
	}
	
	public void unfocus() {
		focused = null;
	}
	
	public void focusComponent(Component c) {
		focused = c;
	}
	
	public Component getFocused() {
		return focused;
	}
	
	public boolean hasComponentType(Class<?> c) {
		for (int i = components.size()-1; i > -1; i--) {
			Component co = components.get(i);
			if (co.getClass() == c)
				return true;
		}
		return false;
	}
	
	public ArrayList<Component> getComponentsOfType(Class<?> c) {
		ArrayList<Component> cl = new ArrayList<Component>();
		for (int i = components.size()-1; i > -1; i--) {
			Component co = components.get(i);
			if (co.getClass() == c)
				cl.add(co);
		}
		return cl;
	}
	
	public Component getComponent(int i) {
		return components.get(i);
	}
	
	public void addComponent(Component c) {
		components.addFirst(c);
	}
	
	public void removeComponent(Component c) {
		components.remove(c);
	}

}
