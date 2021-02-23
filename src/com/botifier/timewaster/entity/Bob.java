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
		((Rectangle)hitbox).setWidth(16);
		((Rectangle)hitbox).setHeight(16);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(getController().src.x, getController().src.y, 16, 16);
		g.setColor(Color.white);
		if (p != null) {
			if ( e != null) {
				//g.draw(e);
				g.drawString("X: "+ getController().src.x + " Y: "+ getController().src.y + " Length: "+p.getLength() + " PPS: " + getController().getPPS(), 10, 460);
			}
		}
	}
	int m = 16;
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (p != null) {
			if (mod) {
				e = new org.newdawn.slick.geom.Path(p.getX(0)*m, p.getY(0)*m);
				for (int i = 1; i < p.getLength(); i ++) {
					e.lineTo(p.getX(i)*m, p.getY(i)*m);
				}
				mod = false;
			}
			if (pos < p.getLength()) {
				if (getController().dst == null) {
					getController().setDestination(p.getX(pos)*m, p.getY(pos)*m);
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
