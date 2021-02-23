package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

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
	
	public void contains(Vector2f pos) {
		contains(pos.x, pos.y);
	}
	
	public boolean contains(float x, float y) {
		float x1 = getPosition().x+width;
		float y1 = getPosition().y;
		float x2 = getPosition().x;
		float y2 = getPosition().y+height;
		return  (x < x1 &&  x > x2 && y > y1 && y < y2) ? true : false;
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
