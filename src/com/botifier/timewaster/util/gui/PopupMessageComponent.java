package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public class PopupMessageComponent extends PopupComponent {
	
	private TextComponent message;

	private PopupMessageComponent(GUI g, String title, String message, Color c, float x, float y, float width, float height) {
		super(g, title, c, x, y, width, height, true);
		this.message = new TextComponent(g, Color.white, message, x+width/2, y+(height/2), true);
		this.message.setCentered(true);
	}

	@Override
	public void runAction() {}
	
	@Override
	public void update(int delta) {
		super.update(delta);
	}
	
	@Override 
	public void draw(Graphics g) {
		super.draw(g);
		message.draw(g);
	}
	
	public String getMessage() {
		return message.getText();
	}
	
	public static PopupMessageComponent createPopup(GUI g, String title, String message, Color c, float x, float y, boolean centered) {
		GameContainer gc = MainGame.mm.getContainer();
		Graphics gr = gc.getGraphics();
		float width = gr.getFont().getWidth(message)+20;
		float height = gr.getFont().getHeight(message)+60;
		if (centered) {
			x -= width/2;
			y -= height/2;
		}
		PopupMessageComponent pmc = new PopupMessageComponent(g, title, message, c, x, y, width, height);
		if (g.hasComponentType(PopupComponent.class)) {
			return null;
		}
		g.addComponent(pmc);
		return pmc;
	}

}
