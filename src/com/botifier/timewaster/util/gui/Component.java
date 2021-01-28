package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.GUI;

public abstract class Component {
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
	
	public abstract void draw(Graphics g);
	
	public void update(int delta) {}
	
	public void setPosition(float x, float y) {
		pos = new Vector2f(x,y);
	}
	
	public void setPosition(Vector2f v) {
		pos = v;
	}
	
	public Vector2f getPosition() {
		return pos;
	}
	
	public GUI getOwner() {
		return g;
	}

	public Color getColor() {
		return c;
	}
	
	public boolean hasOutline() {
		return outline;
	}
}
