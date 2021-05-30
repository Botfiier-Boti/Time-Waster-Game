package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public class TileBuilderComponent extends PopupComponent {
	Runnable onEnd;
	DropDownComponent ddc;
	TextInputComponent tic;
	ButtonComponent walkable;

	private TileBuilderComponent(GUI g, Color c, float x, float y, float width, float height, boolean outline) {
		super(g, "Tile Builder", c, x, y, width, height, outline);
		ddc = new DropDownComponent(g, c, x+(width/2)-(width*0.4f), y+(height/2)-35, width*(0.80f),30, false);
		for (String s : MainGame.getAllImages().keySet()) {
			ddc.addOption(s);
		}
		tic = new TextInputComponent(g, c, x+(width/2)-(width*0.4f), y+(height/2), width*(0.80f),30, "Enter a Char", false);
		tic.resetOnClick = true;
		tic.setMaxLength(1);
		walkable = new ButtonComponent(g, "Walkable", c.darker(0.2f), c.brighter(0.2f), c, null, x+(width/2)-(width*0.4f), y+(height/2)-70, width*(0.80f),25, true);
		walkable.setTogglable(true);
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		tic.update(delta);
		walkable.update(delta);
		ddc.update(delta);
		if (ddc.holder.isVisible()) {
			confirm.setEnabled(false);
			deny.setEnabled(false);
		} else {
			confirm.setEnabled(true);
			deny.setEnabled(true);
		}
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		tic.draw(g);
		walkable.draw(g);
		ddc.draw(g);
	}
	
	@Override
	public void runAction() {
		if (tic.getText().length() <= 0) {
			System.out.println("Nothing Inputted as Identifier.");
			return;
		}
		char c = tic.getText().charAt(0);
		if (MainGame.getCurrentMap().isTileTypeTaken(c) == false) {
			MainGame.getCurrentMap().addTileType(c, ddc.getText(), walkable.pressedButton);
		} else {
			System.out.println("Cannot assign tile to "+c);
		}
		if (onEnd != null)
			onEnd.run();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		tic.destroy();
		ddc.destroy();
		walkable.destroy();
		unfocus();
	}
	
	public void setCloseAction(Runnable r) {
		onEnd = r;
	}
	
	public static TileBuilderComponent createPopup(GUI g, Color c, float x, float y, boolean outline) {
		TileBuilderComponent tbc = new TileBuilderComponent(g, c, x, y,  200, 200, outline);
		if (g.hasComponentType(InputComponent.class)) {
			return null;
		}
		g.addComponent(tbc);
		return tbc;
	}

}
