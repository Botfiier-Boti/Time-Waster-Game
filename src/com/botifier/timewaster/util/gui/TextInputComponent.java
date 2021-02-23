package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public class TextInputComponent extends InputComponent {
	boolean changed = false;
	int maxlength = 16;
	RectangleComponent r;
	RectangleComponent r2;
	TextComponent t;
	String textSave;

	public TextInputComponent(GUI g, Color c, float x, float y, float width, float height, String defaultText, boolean outline) {
		super(g, c, x, y, outline);
		r = new RectangleComponent(g, c, x, y, width, height, outline);
		r2 = new RectangleComponent(g, Color.black, x+2, y+(0.1f*height), width-4, height-(height*0.2f), false);
		t = new TextComponent(g, Color.white, defaultText, x+4, y+4+(0.10f*height), outline);
	}

	@Override
	public void draw(Graphics g) {
		r.draw(g);
		r2.draw(g);
		t.draw(g);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		if (t.getText().length() > maxlength) {
			t.setText(t.getText().substring(0, maxlength));
		}

		Input i = MainGame.mm.getContainer().getInput();
		if (this.isAcceptingInput() == false) {
			if (r.contains(i.getAbsoluteMouseX(), i.getAbsoluteMouseY())) {
				if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
					input = true;
					onSelect();
				}
			}
		} else {
			if (!getText().endsWith("|"))
				t.setText(t.getText()+"|");
			if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				if (!r.contains(i.getAbsoluteMouseX(), i.getAbsoluteMouseY())) {
					input = false;
					onDeselect();
				}
				i.consumeEvent();
				return;
			}
			
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (isSelected() ) {
			t.setText(t.getText().replace("|", ""));
			if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT || key == Input.KEY_LCONTROL || key == Input.KEY_RCONTROL
					|| key == Input.KEY_LALT || key == Input.KEY_RALT || key == Input.KEY_LWIN || key == Input.KEY_RWIN) {
				return;
			}else if (key == Input.KEY_BACK || key == Input.KEY_DELETE) {
				if (t.getText().length() > 0) {
					t.setText(t.getText().substring(0, t.getText().length()-1));
					changed = true;
				}
			} else if (key == Input.KEY_ENTER || key == Input.KEY_RETURN) {
				textSave = t.getText();
				onDeselect();
				input = false;
			} else if (t.getText().length() < maxlength) {
				t.setText(t.getText()+c);
				changed = true;
			}
		}
	}
	
	public void setText(String string) {
		t.setText(string);
	}
	
	public void onSelect() {
		textSave = t.getText();
		t.setText("");
	}
	
	public void onDeselect() {
		if (changed == false) {
			t.setText(textSave);
		} else {
			t.setText(t.getText().replace("|", ""));
			changed = false;
		}
	}
	
	public boolean isSelected() {
		return this.isAcceptingInput();
	}
	
	public void setMaxLength(int length) {
		this.maxlength = length;
	}
	
	public int getMaxLength(int length) {
		return maxlength;
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
