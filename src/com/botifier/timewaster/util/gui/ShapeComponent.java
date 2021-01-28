package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

import com.botifier.timewaster.util.GUI;

public class ShapeComponent extends Component {
	Shape s;

	public ShapeComponent(GUI g, Color c, Shape s, boolean outline) {
		super(g, c, s.getX(), s.getY(), outline);
		this.s = s;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		g.draw(s);
	}
	
	public Shape getShape() {
		return s;
	}

}
