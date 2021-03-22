package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.util.GUI;

public abstract class PopupComponent extends Component {
	private PopupComponent pc;
	TextComponent t;
	RectangleComponent container;
	ButtonComponent confirm;
	
	public PopupComponent(GUI g, String title, Color c, float x, float y, float width, float height, boolean outline) {
		super(g, c, x, y, outline);
		container = new  RectangleComponent(g,c,x,y,width,height,outline);
		confirm = new ButtonComponent(g, "OK", c.darker(0.2f), c.brighter(0.2f), c, new Runnable() {
			@Override
			public void run() {
				unfocus();
				pc.destroy();
			}
			
		}, x+(width/2)-20, y+(height*0.7f) , 40, 20, true);
		confirm.toggle = true;
		pc = this;
		t = new TextComponent(g, Color.white,title, x+width/2,y+height*0.2f, true);
		t.setCentered(true);
	}

	@Override
	public void draw(Graphics g) {
		container.draw(g);
		confirm.draw(g);
		t.draw(g);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		focus();
		confirm.update(delta);
		t.update(delta);
	}
	
	public abstract void runAction();
	
	@Override
	public void destroy() {
		super.destroy();
		if (confirm.pressedButton == true)
			runAction();
		confirm.destroy();
		container.destroy();
	}
	
	

}
