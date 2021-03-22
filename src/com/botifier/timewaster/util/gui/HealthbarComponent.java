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
		String pH = (int)e.getStats().getCurrentHealth()+"/"+(int) e.getMaxHealth();
		t = new TextComponent(g, Color.white, pH, x+width/2, y+height/2, true);
		t.setCentered(true);
	}
	
	@Override
	public void update(int delta) {
		t.update(delta);
		t.setText((int) e.getStats().getCurrentHealth() + "/" + (int) e.getMaxHealth());
		r2.setWidth((e.getStats().getCurrentHealth() / e.getMaxHealth()) * ((r1.getWidth())));
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
