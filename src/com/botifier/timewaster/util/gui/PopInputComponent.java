package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import com.botifier.timewaster.util.GUI;

public class PopInputComponent extends PopupComponent {
	public static final int ALL_CHARACTERS = 0;
	public static final int ALL_ALPHABETICAL = 1;
	public static final int ALL_NUMERIC = 2;
	public static final int ALL_ALPHANUMERIC = 3;
	public static final int ALL_SPECIAL = 4;
	public static final int NO_ALPHABETICAL = 5;
	public static final int NO_NUMERIC = 6;
	public static final int NO_SPECIAL = 7;
	TextInputComponent target;
	TextInputComponent tic;
	Runnable customAction;

	private PopInputComponent(GUI g, Color c, String title, String defaultText, TextInputComponent target, float x, float y, float width, float height, boolean outline, int state) {
		super(g, title, c, x, y, width, height, outline);
		tic = new TextInputComponent(g, c, x+(width*0.1f), y+19, width*(0.80f),30, defaultText, false);
		tic.maxlength = (int) ((width*(0.80f))/11);
		tic.resetOnClick = true;
		tic.setParent(this);
		switch (state) {
			case ALL_CHARACTERS:
				tic.allowAlphabet = true;
				tic.allowNumbers = true;
				tic.allowSymbols = true;
				break;
			case ALL_ALPHABETICAL: 
				tic.allowAlphabet = true;
				tic.allowNumbers = false;
				tic.allowSymbols = false;
				break;
			case ALL_NUMERIC:
				tic.allowAlphabet = false;
				tic.allowNumbers = true;
				tic.allowSymbols = false;
				break;
			case ALL_ALPHANUMERIC:
				tic.allowAlphabet = true;
				tic.allowNumbers = true;
				tic.allowSymbols = false;
				break;
			case ALL_SPECIAL:
				tic.allowAlphabet = false;
				tic.allowNumbers = false;
				tic.allowSymbols = true;
				break;
			case NO_ALPHABETICAL:
				tic.allowAlphabet = false;
				tic.allowNumbers = true;
				tic.allowSymbols = true;
				break;
			case NO_NUMERIC:
				tic.allowAlphabet = true;
				tic.allowNumbers = false;
				tic.allowSymbols = true;
				break;
			case NO_SPECIAL:
				tic.allowAlphabet = true;
				tic.allowNumbers = true;
				tic.allowSymbols = false;
				break;
		}
		this.target = target;
	}
	
	public TextInputComponent getTarget() {
		return target;
	}
	
	public void changeTarget(TextInputComponent t) {
		target = t;
	}

	public void setCustomAction(Runnable customAction) {
		this.customAction = customAction;
	}

	@Override
	public void runAction() {
		if (target != null)  {
			target.setText(tic.getText().replaceAll("|", ""));
		}
		if (customAction != null)
			customAction.run();
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		tic.draw(g);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		tic.update(delta);
	}
	
	public void setMaxLength(int i) {
		tic.setMaxLength(i);
	}
	
	public int getMaxLength() {
		return tic.getMaxLength();
	}
	
	public String getText() {
		return tic.getText();
	}
	
	public Runnable getCustomAction() {
		return customAction;
	}


	public static PopInputComponent createPopup(GUI g, Color c, String title, String defaultText, TextInputComponent target, float x, float y, float width, float height, boolean outline, int state) {
		PopInputComponent pic = new PopInputComponent(g, c, title, defaultText, target, x, y, width, height, outline, state);
		if (g.hasComponentType(PopupComponent.class)) {
			return null;
		}
		g.addComponent(pic);
		return pic;
	}
}
