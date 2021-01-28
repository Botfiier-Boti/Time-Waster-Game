package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.util.GUI;

public class TextComponent extends Component {
	private String text = "";

	public TextComponent(GUI g, Color c, String s, float x, float y, boolean outline) {
		super(g, c, x, y, outline);
	}

	@Override
	public void draw(Graphics g) {
		if (hasOutline()) {
			g.setColor(Color.black);
			g.drawString(text, getPosition().x+g.getFont().getWidth(text)/2+1, getPosition().y+g.getFont().getHeight(text)/2+1);
		}
		g.setColor(getColor());
		g.drawString(text, getPosition().x+g.getFont().getWidth(text)/2, getPosition().y+g.getFont().getHeight(text)/2);
	}
	
	public void setText(String s) {
		text = s;
	}
	
	public String getText() {
		return text;
	}

}
