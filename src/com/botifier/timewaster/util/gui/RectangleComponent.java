package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.util.GUI;

public class RectangleComponent extends Component {
	private float width= 0,height = 0;

	public RectangleComponent(GUI g, Color c, float x, float y, float width, float height, boolean outline) {
		super(g, c, x, y, outline);
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		g.fillRect(getPosition().x, getPosition().y, width, height);
		if (hasOutline()) {
			g.setColor(Color.black);
			g.drawRect(getPosition().x, getPosition().y, width, height);
		}

	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
