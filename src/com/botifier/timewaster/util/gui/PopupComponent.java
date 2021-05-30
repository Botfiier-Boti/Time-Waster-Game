package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public abstract class PopupComponent extends Component {
	private PopupComponent pc;
	TextComponent t;
	RectangleComponent container;
	ButtonComponent confirm;
	ButtonComponent deny;
	
	public PopupComponent(GUI g, String title, Color c, float x, float y, float width, float height, boolean outline) {
		super(g, c, x, y, outline);
		container = new  RectangleComponent(g,c,x,y,width,height,outline);
		confirm = new ButtonComponent(g, "OK", c.darker(0.2f), c.brighter(0.2f), c, new Runnable() {
			@Override
			public void run() {
				unfocus();
				pc.destroy();
			}
			
		}, x+(width)-(width/8)-70, y+(height*0.9f)-25 , 70, 20, true);
		deny = new ButtonComponent(g, "CANCEL", c.darker(0.2f), c.brighter(0.2f), c, new Runnable() {
			@Override
			public void run() {
				unfocus();
				pc.destroy();
			}
			
		}, x+(width/8), y+(height*0.9f)-25 , 70, 20, true);
		confirm.toggle = true;
		pc = this;
		t = new TextComponent(g, Color.white,title, x+width/2,y+height*0.1f, true);
		t.setCentered(true);
	}

	@Override
	public void draw(Graphics g) {
		container.draw(g);
		confirm.draw(g);
		deny.draw(g);
		t.draw(g);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		focus();
		if (confirm.isEnabled())
			confirm.update(delta);
		if (deny.isEnabled())
			deny.update(delta);
		Input i = MainGame.mm.getContainer().getInput();
		if (i.isKeyPressed(Input.KEY_ESCAPE))
			destroy();
	}
	
	public abstract void runAction();
	
	@Override
	public void destroy() {
		super.destroy();
		if (confirm.pressedButton == true)
			runAction();
		deny.destroy();
		confirm.destroy();
		container.destroy();
		unfocus();
	}
	
	

}
