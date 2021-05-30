package com.botifier.timewaster.states;
import static com.botifier.timewaster.main.MainGame.camRatio;
import static com.botifier.timewaster.main.MainGame.debug;
import static com.botifier.timewaster.main.MainGame.displayHitboxes;
import static com.botifier.timewaster.main.MainGame.getCamera;
import static com.botifier.timewaster.main.MainGame.getImage;
import static com.botifier.timewaster.main.MainGame.mm;
import static com.botifier.timewaster.main.MainGame.s;
import static com.botifier.timewaster.main.MainGame.startMap;
import static com.botifier.timewaster.main.MainGame.ttf;
import static com.botifier.timewaster.main.MainGame.ttfB;

import java.io.IOException;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Camera;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.TileMap;
import com.botifier.timewaster.util.gui.ButtonComponent;
import com.botifier.timewaster.util.gui.HealthbarComponent;
import com.botifier.timewaster.util.gui.InventoryComponent;
import com.botifier.timewaster.util.gui.ItemPickupComponent;
import com.botifier.timewaster.util.gui.RectangleComponent;
import com.botifier.timewaster.util.items.AdminRing;
import com.botifier.timewaster.util.items.AdminRock;
import com.botifier.timewaster.util.items.DefenseTestSword;
import com.botifier.timewaster.util.managers.EntityManager;
import com.botifier.timewaster.util.movements.EntityController;

public class OverworldState extends BasicGameState {
	public static final int ID = 1;
	public char[][] tiles;
	public long tps = 0, ticks = 0, tticks = 0, delta = 0, natspawns = 0;
	public Entity mtrack;
	public Entity dead;
	public TileMap m;
	public Player p;
	public Entity targeted = null;
	public int viewDistance = 200;
	public Camera c;
	Random r = new Random();
	EntityManager eM;
	private GUI g;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		tiles = new char[32][32];
		s.clear();
		if (m == null) {
			m = new TileMap(tiles);
			try {
				m.loadFromFile(startMap);
			} catch (IOException e) {
				System.out.println("An Error occured loading the map.");
				e.printStackTrace();
			}
		}
		m.init();
		eM = m.getEntityManager();
		c = new Camera(gc.getWidth() * 0.75f / camRatio, gc.getHeight() / camRatio);
		if (p == null)
			p = new Player("George", 100, 100);
		else {
			p.heal(p.getMaxHealth());
		}
		dead = new Entity("The Dead", getImage("idlesheep"), new EntityController(100, 100, false), 0f) {
			// long wait = 0;
			@Override
			public void init() {
				super.init();
				visible = false;
				healthbarVisible = false;
				invulnerable = true;
				wOverride = 0;
				hOverride = 0;
				collisionbox.setWidth(0);
				collisionbox.setHeight(0);
				getStats().setSpeed(90);
			}

			/*
			 * @Override public void draw(Graphics g) { super.draw(g); if
			 * (getController().getDst() != null) g.drawLine(getLocation().x,
			 * getLocation().y, getController().getDst().x, getController().getDst().y);
			 * 
			 * }
			 * 
			 * @Override public void update(int delta) throws SlickException {
			 * super.update(delta);
			 * this.getController().setDestination(gc.getInput().getMouseX(),
			 * gc.getInput().getMouseY()); }
			 */
		};
		mtrack = new Entity("Mouse Tracker", null, new EntityController(0, 0, false), 0f) {
			@Override
			public void init() {
				super.init();
				healthbarVisible = false;
				solid = false;
				visible = false;
				invulnerable = true;
			}
		};
		getCamera().setCenterEntity(p);
		p.getController().teleport(m.getSpawnPoint());
		p.ei.purge();
		p.inv.purge();
		p.inv.addItem(new AdminRing(), 3);
		p.inv.addItem(new AdminRock(), 0);
		p.inv.addItem(new DefenseTestSword(), 1);
		
		eM.addEntity(p);
		g = new GUI(p);
		g.addComponent(new RectangleComponent(g, Color.gray, gc.getWidth() * 0.75f, -1, gc.getWidth() * 0.25f, gc.getHeight() + 1,true));
		g.addComponent(new HealthbarComponent(g, p, gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.25f, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new RectangleComponent(g, Color.darkGray, gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.05f,(gc.getWidth()/4)-20,(gc.getHeight()/5)-20,true));
		g.addComponent(new RectangleComponent(g, Color.darkGray, gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.335f,(gc.getWidth()/4)-20,(gc.getHeight()/2)+(gc.getHeight()/8),true));
		if (MainGame.debug)
			g.addComponent(new ButtonComponent(g, "Edit Map", Color.darkGray, Color.lightGray, Color.gray, new Runnable() {
				@Override
				public void run() {
					m.eM.getBullets().clear();
					mm.enterState(MapEditorState.ID);
				}
			},gc.getWidth() * 0.75f + 10, gc.getHeight() * 0.05f, (gc.getWidth() / 4) - 20, 20, true));
		g.addComponent(new InventoryComponent(g, Color.lightGray,  gc.getWidth() * 0.75f + 10f, gc.getHeight()*0.335f+4, true, p.ei));
		g.addComponent(new InventoryComponent(g, Color.lightGray,  gc.getWidth() * 0.75f + 10f, gc.getHeight()*0.335f+40, true, p.inv));
		g.addComponent(new ItemPickupComponent(g));
		
		/*System.out.println((gc.getWidth()/4)-20);
		g.addComponent(new RectangleComponent(g, Color.lightGray, gc.getWidth() * 0.75f + 12f, gc.getHeight() * 0.335f,32,32,true));
		g.addComponent(new RectangleComponent(g, Color.lightGray, gc.getWidth() * 0.75f + 44f, gc.getHeight() * 0.335f,32,32,true));
		g.addComponent(new RectangleComponent(g, Color.lightGray, gc.getWidth() * 0.75f + 76f, gc.getHeight() * 0.335f,32,32,true));
		g.addComponent(new RectangleComponent(g, Color.lightGray, gc.getWidth() * 0.75f + 108f, gc.getHeight() * 0.335f,32,32,true));*/
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (g.isAntiAlias()) {
			g.setAntiAlias(false);
		}
		if (!g.getFont().equals(ttf))
			g.setFont(ttf);
		if (p != null && p.getStats().getCurrentHealth() <= 0) {	
			g.setBackground(Color.black);
			String died = "YOU DIED";
			String con = "Press Space to Respawn.";
			g.setFont(ttfB);
			g.drawString(died, (gc.getWidth() / 2) - ttfB.getWidth(died) / 2,
					(gc.getHeight() / 2) - ttfB.getHeight(died));
			g.setFont(ttf);
			g.drawString(con, (gc.getWidth() / 2) - ttf.getWidth(con) / 2, (gc.getHeight() / 2) + ttf.getHeight(con));
			return;
		}
		getCamera().draw(gc, g);
		m.draw(gc, g);
		m.getEntityManager().draw(gc, g);
		/*
		 * for (int i = 0; i < s.size(); i++) { for (int y = 0; y < s.get(0).length(); y
		 * ++) { if (s.get(i).length() > 0) if (s.get(i).charAt(y) == '#') {
		 * g.setColor(Color.darkGray); g.fillRect(y*16, i*16, 16, 16); } else if
		 * (s.get(i).charAt(y) == ' ') { g.setColor(Color.lightGray); g.fillRect(y*16,
		 * i*16, 16, 16); } } }
		 */
		if (p != null && p.build) {
			int x = gc.getInput().getMouseX() / 16;
			int y = gc.getInput().getMouseY() / 16;
			if (x < 0)
				x = 0;
			if (x > m.getWidthInTiles() - 1)
				x = m.getWidthInTiles() - 1;
			if (y < 0)
				y = 0;
			if (y > m.getHeightInTiles() - 1)
				y = m.getHeightInTiles() - 1;
			g.drawRect(x * 16, y * 16, 16, 16);
		}
		g.setColor(Color.white);
		if (targeted != null && targeted.image != null) {
			g.draw(targeted.hitbox);
		}
		if (m.big.getPoints().length > 0)
			g.draw(m.big);
		dead.draw(g);
		g.resetTransform();
		if (!g.getFont().equals(ttf))
			g.setFont(ttf);
		this.g.draw(gc, g);
		g.drawString("WASD to move", 10, 12);
		g.drawString("Press R to restart", 10, 24);
		if (debug == true) {
			g.drawString("FPS: " + gc.getFPS(), 10, 38);
			g.drawString("TPS: " + tps, 10, 52);
			g.drawString("Bullets: " + eM.getBullets().size(), 10, 66);
			g.drawString("Entities: " + eM.getEntities().size(), 10, 80);
			if (getCamera().centerE == mtrack)
				mtrack.getLocation().set(gc.getInput().getAbsoluteMouseX(), gc.getInput().getAbsoluteMouseY());
			else
				mtrack.getLocation().set(gc.getInput().getMouseX(), gc.getInput().getMouseY());

			g.drawString("Mouse Pos: " + mtrack.getLocation().x + "," + mtrack.getLocation().y, 10, 94);
			if (targeted != null) {
				g.drawString("Targeted: " + targeted.getName(), 10, 108);
				g.drawString("Targeted Pos: " + (int) targeted.getLocation().x + "," + (int) targeted.getLocation().y, 10,
						122);
				g.drawString("Targeted Health: " + targeted.getStats().getCurrentHealth() + "/" + targeted.getStats().getMaxHealth()+targeted.getStats().getHealthMod(), 10, 136);
				g.drawString("Targeted Atk: " + targeted.getAttack(), 10, 150);
				g.drawString("Targeted Def: " + targeted.getDefense(), 10, 164);
				g.drawString("Influence: " + (int) targeted.influence, 10, 178);
			}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

		ticks++;
		tticks += delta;
		dead.update(delta);
		
		if (c.centerE == null)
			c.setCenterEntity(p);

		if (targeted != null && targeted.destroy) {
			targeted = null;
		}

		Input i = gc.getInput();
		if (debug) {
			if (i.isKeyPressed(Input.KEY_H)) {
				displayHitboxes = !displayHitboxes;
			}
		}
		if (i.isKeyPressed(Input.KEY_R)) {
			p.getStats().heal(p.getMaxHealth());
			p.getController().getLoc().set(100, 100);
			p.active = true;
			reset(gc);
			return;
		}
		if (c.centerE == p && p != null && p.getStats().getCurrentHealth() <= 0) {
			if (i.isKeyPressed(Input.KEY_SPACE)) {
				p.getStats().heal(p.getMaxHealth());
				p.getController().getLoc().set(100, 100);
				p.active = true;
				reset(gc);
			}
			return;
		}
		
		if (p != null &&  p.getStats().getCurrentHealth() > 0 || getCamera().centerE == mtrack) {
			g.update(gc, delta);
			getCamera().update(gc);
			m.update(gc, delta);
			//handleEntities(gc, delta);
			for (int ie = eM.getEntities().size() - 1; ie >= 0; ie--) {
				Entity en = eM.getEntities().get(ie);
				if (getCamera().centerE != null && getCamera().centerE == mtrack) {
					if (en.hitbox.contains(gc.getInput().getMouseX(), gc.getInput().getMouseY())) {
						if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
							targeted = en;
						}
					}
				}
			}

			if (p != null && p.build) {
				if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
					int x = Math.max(0, i.getMouseX() / 16);
					int y = Math.max(0, i.getMouseY() / 16);

					if (y < tiles.length && y >= 0 && x < tiles[0].length && x >= 1) {
						tiles[y][x] = ' ';
					}
				} else if (i.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
					int x = i.getMouseX() / 16;
					int y = i.getMouseY() / 16;
					if (y < tiles.length && y >= 0 && x < tiles[0].length && x >= 1) {
						tiles[y][x] = '#';
					}
				}
			}
		}
		/*
		 * if (cooldown <= 0) { for (int y = 0; y < m.getHeightInTiles(); y++) { for
		 * (int x = 0; x < m.getWidthInTiles(); x++) { if (tiles[y][x] == 'E' &&
		 * natspawns < 60) { BigGoblin bg = new BigGoblin(x*16,y*16); e.add(bg);
		 * natspawns++; } } } cooldown = maxcooldown; } cooldown-=delta;
		 */

		if (tticks >= 1000) {
			tps = ticks;
			ticks = 0;
			tticks = 0;
		}

		if (targeted != null) {
			if (gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				targeted = null;
			}
		}
		

		this.delta = delta;
		i.clearKeyPressedRecord();
		
	}
	
	public void reset(GameContainer gc) throws SlickException {
		m.reset();
		p.b.clear();
		m.eM.clearBullets();
		init(gc, mm);
	}
	
	public EntityManager getEntityManager() {
		return eM;
	}
	
	public GUI getGUI() {
		return g;
	}
	
	@Override
	public int getID() {
		return ID;
	}

}
