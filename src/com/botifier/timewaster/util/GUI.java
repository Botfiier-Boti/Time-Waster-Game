package com.botifier.timewaster.util;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.util.gui.*;

public class GUI {
	LinkedList<Component> components = new LinkedList<Component>();
	Player p;

	public GUI(Player p) {
		this.p = p;
	}
	
	public void setup(GameContainer gc) {
		addComponent(new RectangleComponent(this, Color.gray, gc.getWidth() * 0.75f, -1, gc.getWidth() * 0.25f, gc.getHeight() + 1,true));
		addComponent(new HealthbarComponent(this, p, gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f, (gc.getWidth() / 4) - 20, 20, true));
		addComponent(new RectangleComponent(this, Color.darkGray, gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.05f,(gc.getWidth()/4)-20,(gc.getHeight()/5)-20,true));
		addComponent(new RectangleComponent(this, Color.darkGray, gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.335f,(gc.getWidth()/4)-20,(gc.getHeight()/2)+(gc.getHeight()/8),true));
	}
	
	public void update(GameContainer gc, int delta) {
		for (Component c : components) {
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
			c.draw(g);
			g.setColor(Color.white);
		}
	}
	
	public void addComponent(Component c) {
		components.addFirst(c);
	}

}
