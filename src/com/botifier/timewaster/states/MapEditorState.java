package com.botifier.timewaster.states;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Camera;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.TileMap;
import com.botifier.timewaster.util.gui.ButtonComponent;
import com.botifier.timewaster.util.gui.DropDownComponent;
import com.botifier.timewaster.util.gui.PopInputComponent;
import com.botifier.timewaster.util.gui.PopupComponent;
import com.botifier.timewaster.util.gui.RectangleComponent;
import com.botifier.timewaster.util.gui.TextComponent;
import com.botifier.timewaster.util.gui.TextInputComponent;

public class MapEditorState extends BasicGameState {
	public static final int ID = 2;
	public static final int PAINT_MODE = 0;
	public static final int PICK_MODE = 1;
	public static final int SET_SPAWN_MODE = 2;
	public static final int SELECT_ENTITY_MODE = 3;
	public static final int MOVE_ENTITY_MODE = 4;
	public Camera c;
	long clickCooldown = 0;
	boolean acceptingInput = true;
	boolean canDraw = true;
	int mode = 0;
	DropDownComponent text;
	TextComponent cTile;
	TextInputComponent mapWidth;
	TextInputComponent mapHeight;
	GUI g;
	File toLoad = null;
	TileMap m;
	char currentTile = 'F';
	Entity selected = null;
	int x,y;
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		m = MainGame.getCurrentMap();
		for (Entity e : m.getInitialEntities()) {
			if (e != null) {
				e.init();
				e.updateHitboxes();
			}
		}
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
				char c = m.getTileChar(text.getText());
				if (currentTile != c)
					currentTile = c;
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
		g.addComponent(new ButtonComponent(g, "Reset Camera", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				c.center = m.getCenter();
				c.zoom = 1;
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+180, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Change Width", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				PopInputComponent pic = PopInputComponent.createPopup(g, Color.gray, "Change Width", m.getWidthInTiles()+"", mapWidth, 200, 200, 200, 80, true, PopInputComponent.ALL_NUMERIC);
				pic.setMaxLength(3);
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+210, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new ButtonComponent(g, "Change Height", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				PopInputComponent pic = PopInputComponent.createPopup(g, Color.gray, "Change Height", m.getHeightInTiles()+"", mapHeight, 200, 200, 200, 80, true, PopInputComponent.ALL_NUMERIC);
				pic.setMaxLength(3);
			}
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f+240, (gc.getWidth() / 4) - 20, 20, true));
		
		g.addComponent(new ButtonComponent(g, "Test Map", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
			@Override
			public void run() {
				try {
					OverworldState os = ((OverworldState)MainGame.mm.getState(OverworldState.ID));
					os.m = m;
					os.reset(gc);
					MainGame.mm.enterState(OverworldState.ID);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			} 
		},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.75f+30, (gc.getWidth() / 4) - 20, 20, true));
		
		text = new DropDownComponent(g, Color.gray, new ArrayList<String>(), (gc.getWidth() / 4) - 20, 20, (int)(gc.getWidth() * 0.75)+10, gc.getHeight() * 0.25f-30, true);
		for (Entry<Character, String> e : m.getTileTypes().entrySet()) {
			text.addOption(e.getValue());
		}
		g.addComponent(text);
		
		mapWidth = new TextInputComponent(g, null, 0, 0, 0, 0, m.getWidthInTiles()+"", false);
		mapHeight = new TextInputComponent(g, null, 0, 13, 0, 0, m.getHeightInTiles()+"", false);
		mapWidth.setCanEdit(false);
		mapHeight.setCanEdit(false);
		
		c = new Camera(gc.getWidth()-(gc.getWidth()*0.25f), gc.getHeight());
		c.center = m.getCenter();
		c.zoom = 1;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		c.draw(gc, g);
		g.setFont(MainGame.ttf);
		m.draw(gc, g,true);
		g.drawRect(m.getOrigin().x, m.getOrigin().y, m.getWidthInTiles()*16, m.getHeightInTiles()*16);
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
					if (e != null) {
						e.init();
						e.updateHitboxes();
					}
				}
				mapWidth.setText(m.getWidthInTiles()+"");
				mapHeight.setText(m.getHeightInTiles()+"");
				selected = null;
				toLoad = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		c.centerE = null;
		mapWidth.update(delta);
		mapHeight.update(delta);
		g.update(gc, delta);
		c.update(gc);
		x = gc.getInput().getMouseX() / 16;
		y = gc.getInput().getMouseY() / 16;
		
		if (g.getFocused() != null) {
			clickCooldown = 10;
			acceptingInput = false;
		} else
			acceptingInput = true;
		
		if (acceptingInput == true) {
			if (gc.getInput().isKeyDown(Input.KEY_W)) {
				c.center.y -= 2/c.zoom;
			} else if (gc.getInput().isKeyDown(Input.KEY_S)) {
				c.center.y += 2/c.zoom;
			}
			if (gc.getInput().isKeyDown(Input.KEY_A)) {
				c.center.x -= 2/c.zoom;
			} else if (gc.getInput().isKeyDown(Input.KEY_D)) {
				c.center.x += 2/c.zoom;
			}	
			
			if (gc.getInput().isKeyDown(Input.KEY_R)) {
				c.zoom += 0.01f;
			} else if (gc.getInput().isKeyDown(Input.KEY_F)) {
				c.zoom -= 0.01f;
			}
			if (x >= 0 && x < m.getWidthInTiles() && y >= 0 && y < m.getHeightInTiles()) {
				if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && clickCooldown <= 0) {
					clickCooldown = 3;
					switch (mode) {
						case PAINT_MODE:
							if (m.getTile(x, y) != currentTile && canDraw)
								m.setTile(x, y, currentTile);
							break;
						case PICK_MODE:
							currentTile = m.getTile(x, y);
							text.setText(m.getTileName(currentTile));
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

			if (Math2.isInteger(mapWidth.getText()) && Integer.valueOf(mapWidth.getText()) != m.getWidthInTiles())
				m.resize(Integer.valueOf(mapWidth.getText()), m.getHeightInTiles());
			if (Math2.isInteger(mapHeight.getText()) && Integer.valueOf(mapHeight.getText()) != m.getHeightInTiles())
				m.resize(m.getWidthInTiles(), Integer.valueOf(mapHeight.getText()));
		}
		for (int i = m.getInitialEntities().size()-1; i >= 0; i--) {
			Entity e = m.getInitialEntities().get(i);
			if (e != null)
				e.updateHitboxes();
		}
		if (selected != null)
			cTile.setText("Current Tile: "+currentTile+"\nSelected Entity: "+selected.getName()+"\nMode: "+mode);
		else 
			cTile.setText("Current Tile: "+currentTile+"\nMode: "+mode);
		clickCooldown--;
	}

	@Override
	public int getID() {
		return ID;
	}

}
