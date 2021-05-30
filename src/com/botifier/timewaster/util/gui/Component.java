package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.GUI;

public abstract class Component {
	private boolean visible = true;
	private boolean enabled = true;
	private boolean outline;
	private Color c;
	private GUI g;
	private Vector2f pos;
	
	public Component(GUI g, Color c, float x, float y, boolean outline) {
		this.g = g;
		this.c = c;
		this.outline = outline;
		setPosition(x,y);
	}
	
	public void update(int delta) {}
	
	public abstract void draw(Graphics g);
	
	public void destroy() {
		enabled = false;
		unfocus();
		g.removeComponent(this);
	}
	
	public void focus() {
		g.focusComponent(this);
	}
	
	public void unfocus() {
		g.unfocus();
	}
	
	public void setEnabled(boolean b) {
		enabled = b;
	}
	
	public void setVisible(boolean b) {
		visible = b;
	}
	
	public void setPosition(float x, float y) {
		pos = new Vector2f(x,y);
	}
	
	public void setPosition(Vector2f v) {
		pos = v;
	}
	
	public void setColor(Color color) {
		c = color;
	}
	
	public Vector2f getPosition() {
		return pos;
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public GUI getOwner() {
		return g;
	}

	public Color getColor() {
		return c;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean hasOutline() {
		return outline;
	}
	
	public boolean isFocused() {
		return this == g.getFocused();
	}
}
