package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.util.GUI;

public class TextComponent extends Component {
	private String text = "";
	private boolean centered = false;

	public TextComponent(GUI g, Color c, String s, float x, float y, boolean outline) {
		super(g, c, x, y, outline);
		setText(s);
	}

	@Override
	public void draw(Graphics g) {
		float x = getPosition().x;
		float y = getPosition().y;
		if (isCentered() == true) {
			x-= g.getFont().getWidth(text)/2;
			y-= g.getFont().getHeight(text)/2;
		}
		if (hasOutline() == true) {
			g.setColor(Color.black);
			g.drawString(text, x+1, y+1);
		}
		g.setColor(getColor());
		g.drawString(text, x, y);
	}
	
	public void setText(String s) {
		text = s;
	}
	
	public String getText() {
		return text;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
	public boolean isCentered() {
		return centered;
	}
}
