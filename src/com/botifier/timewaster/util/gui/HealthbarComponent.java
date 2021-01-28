package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.GUI;

public class HealthbarComponent extends Component {
	private Entity e;
	private RectangleComponent r1, r2;
	private TextComponent t;
	

	public HealthbarComponent(GUI g, Entity e, float x, float y, float width, float height, boolean outline) {
		super(g, Color.white, x, y, outline);
		this.e = e;
		r1 = new RectangleComponent(g, Color.red, x, y, width, height,false);
		r2 = new RectangleComponent(g, Color.green, x, y, width, height,true);
		String pH = (int)e.health+"/"+(int)e.maxhealth;
		t = new TextComponent(g, Color.white, pH, x, y, true);
	}
	
	@Override
	public void update(int delta) {
		t.setText((int) e.health + "/" + (int) e.maxhealth);
		r2.setWidth((e.health / e.maxhealth) * ((r1.getWidth())));
	}

	@Override
	public void draw(Graphics g) {
		r1.draw(g);
		r2.draw(g);
		t.draw(g);
		if (hasOutline()) {
			g.setColor(Color.black);
			g.drawRect(getPosition().x, getPosition().y, r1.getWidth(), r1.getHeight());
		}
	}

	public void setTarget(Entity e) {
		this.e = e;
	}
	
	public Entity getTarget() {
		return e;
	}
}
