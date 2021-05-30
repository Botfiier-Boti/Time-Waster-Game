package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public class ButtonComponent extends Component {
	boolean pressedButton = false;
	boolean toggle = false;
	Runnable action;
	Color hover;
	Color pressed;
	RectangleComponent r;
	TextComponent t;
	Image symbol = null;

	public ButtonComponent(GUI g, String text, Color pressed, Color c, Color hover, Runnable action, float x, float y, float width, float height, boolean outline) {
		super(g, c, x, y, outline);
		this.r = new RectangleComponent(g, c, x, y, width, height, outline);
		t = new TextComponent(g, Color.white, text, x+width/2, y+height/2, true);
		t.setCentered(true);
		this.pressed = pressed;
		this.hover = hover;
		this.action = action;
	}

	@Override
	public void draw(Graphics g) {
		if (pressedButton == true)
			r.setColor(pressed);
		r.draw(g);
		if (symbol == null)
			t.draw(g);
		else {
			g.drawImage(symbol, t.getX()-symbol.getWidth()/2, t.getY()-symbol.getHeight()/2);
		}
	}
	
	@Override
	public void update(int delta) {
		Input i = MainGame.mm.getInput();
		if (isVisible() && r.contains(i.getAbsoluteMouseX(), i.getAbsoluteMouseY())) {
			r.setColor(hover);
			if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				if (toggle == true)
					pressedButton = !pressedButton;
				else
					pressedButton = true;
				if (action != null)
					action.run();
			} else if (toggle == false) {
				pressedButton = false;
			}
		} else { 
			if (toggle == false)
				pressedButton = false;
			r.setColor(getColor());
		}
		r.setPosition(this.getPosition());
	}
	
	public void setPressedVisual(boolean b) {
		pressedButton = b;
	}
	
	public void changeAction(Runnable action) {
		this.action = action;
	}
	
	public void setCustomSymbol(Image symbol) {
		this.symbol = symbol;
	}
	
	public void setTogglable(boolean toggle) {
		this.toggle = toggle;
	}
	
	public void setText(String s) {
		t.setText(s);
	}
	
	public Image getCustomSymbol() {
		return symbol;
	}
	
	public String getText() {
		return t.getText();
	}
	
	public float getWidth() {
		return r.getWidth();
	}
	
	public float getHeight() {
		return r.getHeight();
	}

}
