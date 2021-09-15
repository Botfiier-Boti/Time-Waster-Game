package com.botifier.timewaster.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.movements.SimpleEntityController;

//Text
public class PopupText extends Entity {
	String text;
	Color c;
	int y = 0;
	boolean inf = false;
	
	public PopupText(String text, float x, float y, Color c) throws SlickException {
		super("Text", null, new SimpleEntityController(x, y));
		getController().setDestination(x, y-30);
		getController().setCollision(false);
		getStats().setSpeed(15);
		this.text = text;
		this.c = c;
		solid = false;
		healthbarVisible = false;
		invincible = true;
		getController().allyCollision = false;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		//Destroy after a bit
		if (inf == false && y >= getLocation().distance(getController().getDst()))
			destroy = true;
		//Float up
		y++;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.setFont(MainGame.ttfS);
		g.drawString(text, getLocation().x+1, getLocation().y+1-y);
		g.setColor(c);
		g.drawString(text, getLocation().x, getLocation().y-y);
		g.setFont(MainGame.ttf);
		g.setColor(Color.white);
	}
}
