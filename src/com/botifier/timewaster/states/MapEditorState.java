package com.botifier.timewaster.states;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Camera;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.TileMap;
import com.botifier.timewaster.util.gui.ButtonComponent;
import com.botifier.timewaster.util.gui.RectangleComponent;
import com.botifier.timewaster.util.gui.TextComponent;
import com.botifier.timewaster.util.gui.TextInputComponent;

public class MapEditorState extends BasicGameState {
	public static final int PAINT_MODE = 0;
	public static final int PICK_MODE = 1;
	public static final int SET_SPAWN_MODE = 2;
	public static final int SELECT_ENTITY_MODE = 3;
	public static final int MOVE_ENTITY_MODE = 4;
	
	boolean textFocused = false;
	boolean canDraw = true;
	int mode = 0;
	Camera c;
	TextInputComponent text;
	TextComponent cTile;
	GUI g;
	File toLoad = null;
	TileMap m;
	char currentTile = 'F';
	Entity selected = null;
	int x,y;
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		m = MainGame.getCurrentMap();
		g = new GUI(null);
		g.addComponent(new RectangleComponent(g, Color.gray, gc.getWidth() * 0.75f, -1f, gc.getWidth() * 0.25f, gc.getHeight() + 1f,true));
		g.addComponent(new ButtonComponent(g, "Open Map", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Open Map");
				jfc.setDialogType(JFileChooser.OPEN_DIALOG);
				jfc.setFileFilter(new FileNameExtensionFilter("Map File", "map"));
				int accept = jfc.showOpenDialog(null);
				if (accept == JFileChooser.APPROVE_OPTION) {
					File loadFile = jfc.getSelectedFile();
					toLoad = loadFile;
				}
				
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.05f, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Save Map", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save Map As");
				jfc.setDialogType(JFileChooser.SAVE_DIALOG);
				jfc.setFileFilter(new FileNameExtensionFilter("Map File", "map"));
				int accept = jfc.showSaveDialog(null);
				if (accept == JFileChooser.APPROVE_OPTION) {
					File saveFile = jfc.getSelectedFile();
					String name = saveFile.toString();
					if (!name.endsWith(".map"))
						name += ".map";
					try {
						m.saveToFile(name);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.05f+30, (gc.getWidth() / 4) - 20, 20, true));
		cTile = new TextComponent(g, Color.white, "Current Tile: "+currentTile, 4, 14, true);
		g.addComponent(cTile);
		g.addComponent(new ButtonComponent(g, "Change Tile", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (text.getText().length() > 0 && currentTile != text.getText().charAt(0))
					currentTile = text.getText().charAt(0);
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Paint", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (mode != PAINT_MODE)
					mode = PAINT_MODE;
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+30, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Pick Tile", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (mode != PICK_MODE)
					mode = PICK_MODE;
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+60, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Set Spawn", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (mode != SET_SPAWN_MODE)
					mode = SET_SPAWN_MODE;
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+90, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Select Entity", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (mode != SELECT_ENTITY_MODE)
					mode = SELECT_ENTITY_MODE;
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+120, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Move Entity", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				if (mode != MOVE_ENTITY_MODE)
					mode = MOVE_ENTITY_MODE;
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+150, (gc.getWidth() / 4) - 20, 20, true));
		
		text = new TextInputComponent(g, Color.gray, (int)(gc.getWidth() * 0.75)+10, gc.getHeight() * 0.25f-30, 140, 25, currentTile+"", false);
		text.setMaxLength(1);
		
		g.addComponent(text);
		c = new Camera(gc.getWidth()-(gc.getWidth()*0.25f), gc.getHeight());
		c.zoom = 1;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		c.draw(gc, g);
		g.setFont(MainGame.ttf);
		m.draw(gc, g,false);
		for (int i = m.getInitialEntities().size()-1; i >= 0; i--) {
			Entity e = m.getInitialEntities().get(i);
			if (e != null)
				e.draw(g);
		}
		if (selected != null)
			g.draw(selected.hitbox);
		g.drawString("S", m.getSpawnPoint().x+11, m.getSpawnPoint().y+11);
		switch (mode) {
			case PAINT_MODE:
				if (x >= 0 && x < m.getWidthInTiles() && y >= 0 && y < m.getHeightInTiles())
					g.drawRect(x * 16, y * 16, 16, 16);
				break;
			case PICK_MODE:
				g.drawImage(MainGame.getImage("pickblock"), gc.getInput().getMouseX()/16*16+10, gc.getInput().getMouseY()/16*16-10);
				break;
			case SET_SPAWN_MODE:
				g.drawString("S", gc.getInput().getMouseX()/16*16+3, gc.getInput().getMouseY()/16*16+3);
				break;
		}
		g.resetTransform();
		this.g.draw(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (toLoad != null) {
			try {
				m.loadFromFile(toLoad);
				for (Entity e : m.getInitialEntities()) {
					if (e != null)
						e.init();
				}
				selected = null;
				toLoad = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		g.update(gc, delta);
		c.update(gc);
		x = gc.getInput().getMouseX() / 16;
		y = gc.getInput().getMouseY() / 16;
		
		if (text.isSelected() == false) {
			if (gc.getInput().isKeyDown(Input.KEY_W)) {
				c.center.y -= 1;
			} else if (gc.getInput().isKeyDown(Input.KEY_S)) {
				c.center.y += 1;
			}
			if (gc.getInput().isKeyDown(Input.KEY_A)) {
				c.center.x -= 1;
			} else if (gc.getInput().isKeyDown(Input.KEY_D)) {
				c.center.x += 1;
			}	
		}
		for (int i = m.getInitialEntities().size()-1; i >= 0; i--) {
			Entity e = m.getInitialEntities().get(i);
			if (e != null)
				e.updateHitboxes();
		}
		if (x >= 0 && x < m.getWidthInTiles() && y >= 0 && y < m.getHeightInTiles()) {
			if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				switch (mode) {
					case PAINT_MODE:
						if (m.getTile(x, y) != currentTile && canDraw)
							m.setTile(x, y, currentTile);
						break;
					case PICK_MODE:
						currentTile = m.getTile(x, y);
						text.setText(currentTile+"");
						break;
					case SET_SPAWN_MODE:
						m.setSpawnPoint(x, y);
						break;
					case SELECT_ENTITY_MODE:
						for (int i = m.getInitialEntities().size()-1; i >= 0; i--) {
							Entity e = m.getInitialEntities().get(i);
							if (e.hitbox.contains(gc.getInput().getMouseX(), gc.getInput().getMouseY())) {
								selected = e;
							}
						}
						break;
					case MOVE_ENTITY_MODE:
						if (selected != null) {
							selected.getController().teleport(gc.getInput().getMouseX(), gc.getInput().getMouseY()+selected.hitbox.getHeight()/2);
						}
						break;
					default:
						break;
				}
			} else if (gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				switch (mode) {
					case PAINT_MODE:
						if (m.getTile(x, y) != ' ' && canDraw == true)
							m.setTile(x, y, ' ');
						break;
					
				}
			} 
		}
		textFocused = text.isSelected();
		if (selected != null)
			cTile.setText("Current Tile: "+currentTile+"\nSelected Entity: "+selected.getName()+"\nMode: "+mode);
		else 
			cTile.setText("Current Tile: "+currentTile+"\nMode: "+mode);
	}

	@Override
	public int getID() {
		return 2;
	}

}
