package com.botifier.timewaster.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.movements.EntityController;

//Simple effect thing
public class Effect extends Entity {
	Color c;
	Shape s;
	long duration;
	long cdir = 0;
	boolean shrinks;
	
	public Effect(Shape s, long duration, Color c) {
		super("Effect", null, new EntityController(s.getCenterX(),s.getCenterY()), false);
		this.c = c;
		this.s = s;
		this.duration = duration;
		hasshadow = false;
		healthbarVisible = false;
		shrinks = true;
	}
	
	@Override
	public void update(int delta) {
		cdir++;
		if (s instanceof Circle && shrinks == true) {
			Circle c = (Circle) s;
			c.setRadius(c.getRadius()-4f/duration);
			c.setLocation(c.getX()+(4f/duration), c.getY()+(4f/duration));
			if (c.getRadius() <= 0)
				destroy = true;
		}
		if (cdir >= duration)
			destroy = true;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(c);
		g.fill(s);
		g.setColor(Color.white);
	}

}
