package com.botifier.timewaster.entity;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.Path;

import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.movements.EntityController;

//Old code
public class Bob extends Entity {
	public Path p;
	public boolean mod = true;
	org.newdawn.slick.geom.Path e;
	
	int pos = 0;
	
	public Bob() {
		super("bob", null, new EntityController(32, 32));
		getStats().setSpeed(75);
	}
	
	public Bob(float x, float y) {
		super("bob", null, new EntityController(x, y));
		getStats().setSpeed(75);
	}
	
	@Override
	public void init() {
		super.init();
		healthbarVisible = false;
		((Rectangle)hitbox).setWidth(16);
		((Rectangle)hitbox).setHeight(16);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(getLocation().x-8, getLocation().y-8, 16, 16);
		g.setColor(Color.white);
		if (p != null) {
			if ( e != null) {
				//g.draw(e);
				g.drawString("X: "+ getLocation().x + " Y: "+ getLocation().y + " Length: "+p.getLength() + " PPS: " + getController().getPPS(), 10, 460);
			}
		}
	}
	int m = 16;
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (p != null) {
			if (mod) {
				e = new org.newdawn.slick.geom.Path(p.getX(0)*m+8, p.getY(0)*m+8);
				for (int i = 1; i < p.getLength(); i ++) {
					e.lineTo(p.getX(i)*m, p.getY(i)*m);
				}
				mod = false;
			}
			if (pos < p.getLength()) {
				if (getController().dst == null) {
					getController().setDestination(p.getX(pos)*m+8, p.getY(pos)*m+8);
					pos+=1;	
				}
			}
			else {
				p = null;
				pos = 0;
			}
		}
	}
}
