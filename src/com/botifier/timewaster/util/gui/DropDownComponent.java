package com.botifier.timewaster.util.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public class DropDownComponent extends Component {
	int display = 4;
	int pos = 0;
	ArrayList<String> options;
	RectangleComponent holder;
	RectangleComponent container;
	TextInputComponent tic;
	ButtonComponent button;
	ArrayList<ButtonComponent> buttons = new ArrayList<ButtonComponent>();
	Object selected = null;

	public DropDownComponent(GUI g, Color c, ArrayList<String> options, float width, float height, float x, float y, boolean outline) {
		super(g, c, x, y, outline);
		this.options = options;
		this.container = new RectangleComponent(g, c, x, y, width, height, outline);
		this.tic = new TextInputComponent(g, c, x, y, width*0.85f, height, "", outline);
		tic.setMaxLength(16);
		tic.setCanEdit(false);
		this.button = new ButtonComponent(g, "v", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				holder.setVisible(!holder.isVisible());
			}
		},x+(width*0.85f), y, width*0.15f, height, true);
		button.setTogglable(true);
		changeDisplaySize(display);
	}
	
	@Override
	public void update(int delta) {
		container.update(delta);
		tic.update(delta);
		button.update(delta);
		if (holder != null && holder.isVisible() == true)
			for (ButtonComponent b : buttons) {
				if (b != null)
					b.update(delta);
			}
		updateButtons();
	}

	@Override
	public void draw(Graphics g) {
		container.draw(g);
		tic.draw(g);
		if (holder != null && holder.isVisible() == true) {
			holder.draw(g);
			for (ButtonComponent b : buttons) {
				if (b.isVisible())
					b.draw(g);
			}
		} else {
			button.setPressedVisual(false);
		}
		button.draw(g);
	}
	
	public void generateButtons() {
		buttons.clear();
		Image arrow = MainGame.getImage("notv");
		Image arrow2 =  MainGame.getImage("v");
		ButtonComponent b1 = new ButtonComponent(getOwner(), "^", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (pos > 0)
					pos--;
			}
		},holder.getX()+(holder.getWidth()-20), holder.getY(), 20, 20, true);
		b1.setCustomSymbol(arrow);
		buttons.add(b1);
		ButtonComponent b2 = new ButtonComponent(getOwner(), "v", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (pos < options.size()-display)
					pos++;
			}
		},holder.getX()+(holder.getWidth()-20), holder.getY()+holder.getHeight()-20, 20, 20, true);
		b2.setCustomSymbol(arrow2);
		buttons.add(b2);
		for (int i = 0; i < display; i++) {
			ButtonComponent b = new ButtonComponent(getOwner(), "{PLACEHOLDER}", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
				@Override
				public void run() {
				}
			},holder.getX(), holder.getY()+(20*i), holder.getWidth()-20, 20, true);
			b.changeAction(new Runnable() {

				@Override
				public void run() {
					selected = b;
					setText(b.getText());
					holder.setVisible(false);
					unfocus();
				}
				
			});
			buttons.add(b);
		}
	}
	
	public void updateButtons() {
		int e = 0;
		for (int i = 2; i < buttons.size(); i++) {
			ButtonComponent b = buttons.get(i);
			if (e >= display || e < 0)
				e = 0;
			if (pos+e < options.size() && pos+e > -1) {
				b.setVisible(true);
				b.setText(options.get(pos+e));
				e++;
				continue;
			} else {
				b.setVisible(false);
				continue;
			}
		}
	}
	
	public void resetOptions() {
		options.clear();
	}
	
	public void setText(String s) {
		tic.setText(s);
	}
	
	public void addOption(String s) {
		options.add(s);
	}
	
	public void changeDisplaySize(int size) {
		display = size;
		this.holder = new RectangleComponent(getOwner(), Color.lightGray, getX(), getY()+container.getHeight(), container.getWidth(), 40+(20*(display-2)), true);
		this.holder.setVisible(false);
		generateButtons();
	}
	
	public String getOption(int i) {
		return options.get(i);
	}
	
	public String getOptionOnDisplay(int i) {
		return options.get(pos+i);
	}
	
	public String getText() {
		return tic.getText();
	}
}
