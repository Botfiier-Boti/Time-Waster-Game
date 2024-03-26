package com.botifier.timewaster.states;

import static com.botifier.timewaster.main.MainGame.mm;
import static com.botifier.timewaster.main.MainGame.ttf;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.gui.ButtonComponent;
import com.botifier.timewaster.util.gui.PopupMessageComponent;

public class MainMenuState extends BasicGameState {
	public static final int ID = 0;
	private GUI gui;	

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gui = new GUI(null);
		gui.addComponent(new ButtonComponent(gui, "Play", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
				@Override
				public void run() {
					mm.enterState(OverworldState.ID);
				}
			},(gc.getWidth()/2)-((gc.getWidth() / 8) - 10), gc.getHeight() * 0.5f, (gc.getWidth() / 4) - 20, 20, true));
		gui.addComponent(new ButtonComponent(gui, "Options", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				PopupMessageComponent.createPopup(gui, "Error!", "There are no options yet\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nOk?", Color.lightGray, gc.getWidth()/2, gc.getHeight()/2, true);
			}
		},(gc.getWidth()/2)-((gc.getWidth() / 8) - 10), gc.getHeight() * 0.5f+25, (gc.getWidth() / 4) - 20, 20, true));
		gui.addComponent(new ButtonComponent(gui, "Exit", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				gc.exit();
			}
		},(gc.getWidth()/2)-((gc.getWidth() / 8) - 10), gc.getHeight() * 0.5f+50, (gc.getWidth() / 4) - 20, 20, true));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!g.getFont().equals(ttf))
			g.setFont(ttf);
		gui.draw(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		gui.update(gc, delta);
	}

	@Override
	public int getID() {
		return ID;
	}

}
