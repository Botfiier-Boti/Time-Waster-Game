package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public class ButtonComponent extends Component {
	boolean pressedButton = false;
	Runnable action;
	Color hover;
	Color pressed;
	RectangleComponent r;
	TextComponent t;

	public ButtonComponent(GUI g, String text, Color pressed, Color c, Color hover, Runnable action, float x, float y, float width, float height, boolean outline) {
		super(g, c, x, y, outline);
		this.r = new RectangleComponent(g, c, x, y, width, height, outline);
		t = new TextComponent(g, Color.white, text, x-(MainGame.mm.getContainer().getGraphics().getFont().getWidth(text))+width/2, y, true);
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
		t.draw(g);
	}
	
	@Override
	public void update(int delta) {
		Input i = MainGame.mm.getContainer().getInput();
		if (r.contains(i.getAbsoluteMouseX(), i.getAbsoluteMouseY())) {
			r.setColor(hover);
			if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				pressedButton = true;
				action.run();
			} else {
				pressedButton = false;
			}
		} else { 
			pressedButton = false;
			r.setColor(getColor());
		}
		r.setPosition(this.getPosition());
	}
	
	public float getWidth() {
		return r.getWidth();
	}
	
	public float getHeight() {
		return r.getHeight();
	}

}
