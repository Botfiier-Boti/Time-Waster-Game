package com.botifier.timewaster.main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.botifier.timewaster.entity.*;
import com.botifier.timewaster.entity.enemy.*;
import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.util.Camera;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.TileMap;
import com.botifier.timewaster.util.managers.EntityManager;
import com.botifier.timewaster.util.movements.EntityController;

public class MainGame extends BasicGame {
	private static final String versionId = "0.0.6";
	private static boolean loaded = false;
	protected static String startMap = "testmap.map";
	public static boolean debug = false;
	public static boolean displayHitboxes = false;
	public static float camRatio = 2f;
	public static ArrayList<String> s = new ArrayList<String>();
	public static AngelCodeFont ttf, ttfS, ttfB;
	public static Rectangle bect;
	public static MainGame mm; 
	public HashMap<String, Image> i = new HashMap<String, Image>();
	public HashMap<String, Sound> so = new HashMap<String, Sound>();
	public char[][] tiles;
	public long tps = 0, ticks = 0, tticks = 0, delta = 0, natspawns = 0;
	public EntityManager eM;
	public Entity mtrack;
	public Entity dead;
	public TileMap m;
	public Player p;
	private int viewDistance = 200;
	private Camera c;
	private GUI g;
	Entity targeted = null;
	Random r = new Random();
	GameContainer gc;
	Font f;
	Music mu;
	// Dungeon dg;
	// AStarPathFinder aspf;
	// long cooldown = 100;
	// long maxcooldown = 100000;

	public MainGame() {
		super("Time Waster v" + versionId);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// e.add(new Bob());
		try {
			f = Font.createFont(Font.TRUETYPE_FONT,
					org.newdawn.slick.util.ResourceLoader.getResourceAsStream("PressStart2P.ttf"));
		} catch (FontFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		loadImages();
		loadSounds();
		eM = new EntityManager(this);
		ttf = new AngelCodeFont("PressStart2P-mid.fnt", getImage("fontmedium"));
		ttfB = new AngelCodeFont("Fonts/PressStart2P.fnt", getImage("font"));
		ttfS = new AngelCodeFont("PressStart2P-sml.fnt", getImage("fontsmall"));
		gc.getGraphics().setFont(ttf);
		tiles = new char[32][32];
		s.clear();
		/*for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				if (x != 0 && y != 0 && x != 1 && y != 1 && x < tiles[y].length - 1 && x < tiles[y].length - 2
						&& y < tiles.length - 1 && y < tiles.length - 2) {
					/*
					 * if ( x > 5 && x < tiles[y].length-5 && y > 5 && y < tiles.length-5 &&
					 * r.nextInt(50) == 2) { tiles[y][x] = 'E'; } else
					 *
					if (y == 2) 
						tiles[y][x] = '#';
					else if (y == 3)
						tiles[y][x] = 'U';
					else
						tiles[y][x] = 'F';
				} else {
					tiles[y][x] = 'B';
				}
			}*/
		m = new TileMap(tiles);
		try {
			m.loadFromFile(startMap);
		} catch (IOException e) {
			System.out.println("An Error occured loading the map.");
			e.printStackTrace();
		}
		c = new Camera(gc.getWidth() * 0.75f / camRatio, gc.getHeight() / camRatio);
		p = new Player("George", 100, 100);
		dead = new Entity("The Dead", getImage("idlesheep"), new EntityController(100, 100, 90, false), 0f) {
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
		mtrack = new Entity("Mouse Tracker", null, new EntityController(0, 0, 0, false), 0f) {
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
		for (int i = 0; i < 0; i++) {
			BigGoblin bg = new BigGoblin(200, 300);
			eM.addEntity(bg);
		}
		for (int i = 0; i < 0; i++) {
			eM.addEntity(new TestChest((m.getWidthInTiles() * 16) / 2, (m.getHeightInTiles() * 16) / 2));

		}
		for (int i = 0; i < 0; i++) {
			// e.add(new IceSphereClone(240,240));
			eM.addEntity(new BulletSpawner((m.getWidthInTiles()/2)*16, (m.getHeightInTiles()/2)*16,750));
		}
		p.getController().teleport(m.getSpawnPoint());
		eM.addEntity(p);
		loaded = true;
		g = new GUI(p);
		g.setup(gc);
		mu = new Music("testmusic.wav");
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (g.isAntiAlias()) {
			g.setAntiAlias(false);
		}
		if (!g.getFont().equals(ttf))
			g.setFont(ttf);
		if (p != null && p.health <= 0) {	
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
		eM.draw(gc, g);
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
		if (bect != null)
			g.draw(bect);
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
				g.drawString("Targeted Health: " + targeted.health + "/" + targeted.maxhealth, 10, 136);
				g.drawString("Targeted Atk: " + targeted.atk, 10, 150);
				g.drawString("Targeted Def: " + targeted.def, 10, 164);
				g.drawString("Influence: " + (int) targeted.influence, 10, 178);
			}
		}
		
	}

	public void handleEntities(GameContainer gc, int delta) throws SlickException {
		eM.update(gc, delta);
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
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

		ticks++;
		tticks += delta;
		dead.update(delta);

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
			p.invulPeriod = 100;
			p.health = p.maxhealth;
			p.getController().getLoc().set(100, 100);
			p.active = true;
			reset(gc);
			return;
		}
		if (c.centerE == p && p != null && p.health <= 0) {
			if (i.isKeyPressed(Input.KEY_SPACE)) {
				p.invulPeriod = 100;
				p.health = p.maxhealth;
				p.getController().getLoc().set(100, 100);
				p.active = true;
				reset(gc);
			}
			return;
		}
		
		if (p != null && p.health > 0 || getCamera().centerE == mtrack) {
			g.update(gc, delta);
			getCamera().update(gc);
			handleEntities(gc, delta);
			if (getCamera().centerE == mtrack) {

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
		eM.getBullets().clear();
		eM.getOverlayedEffects().clear();
		eM.getEntities().clear();
		init(gc);
	}

	public void loadImages() throws SlickException {
		i.put("biggobboidle", new Image("Images/BigGobboIdle.png"));
		i.put("biggobbowalk", new Image("Images/BigGobboWalk.png"));
		i.put("biggobboattack", new Image("Images/BigGobboAttack.png"));
		i.put("fakeice", new Image("Images/FakeIce.png"));
		i.put("defaultshot", new Image("Images/Shots.png"));
		i.put("debugman", new Image("Images/Testy.png"));
		i.put("purplebag", new Image("Images/Purplebag.png"));
		i.put("whitebag", new Image("Images/Whitebag.png"));
		i.put("testchest", new Image("Images/Testchest.png"));
		i.put("boomerang", new Image("Images/Boomerang.png"));
		i.put("bee", new Image("Images/Bee.png"));
		i.put("beehive", new Image("Images/Beehive.png"));
		i.put("sheep", new Image("Images/Sheep.png"));
		i.put("idlesheep", new Image("Images/Sheep-Idle.png"));
		i.put("fireball", new Image("Images/Fire.png"));
		i.put("rock", new Image("Images/Rock.png"));
		i.put("smallrock", new Image("Images/Rock-Small.png"));
		i.put("rocktile", new Image("Images/Tile.png"));
		i.put("floatingwine", new Image("Images/FloatingWine.png"));
		i.put("wineattack", new Image("Images/WineAttack.png"));
		i.put("wineglass", new Image("Images/WineGlass.png"));
		i.put("reddrop", new Image("Images/RedDrop.png"));
		i.put("whitearrow", new Image("Images/WhiteArrow.png"));
		i.put("fontsmall", new Image("Images/PressStart2P-sml.png"));
		i.put("fontmedium", new Image("Images/PressStart2P-mid.png"));
		i.put("font", new Image("Fonts/PressStart2P.png"));
		i.put("head", new Image("Images/Head.png"));
		i.put("body", new Image("Images/Body.png"));
		i.put("brickwall", new Image("Images/WallTile.png"));
		i.put("blanktile", new Image("Images/BlankTile.png"));
		i.put("tiledarkup", new Image("Images/TileDarkUp.png"));
		i.put("playeridle", new Image("Images/PlayerIdle.png"));
		i.put("playerwalk", new Image("Images/PlayerWalk.png"));
		i.put("shadow", new Image("Images/Shadow.png"));
		i.put("torpedo", new Image("Images/Torpedo.png"));
		i.put("shiny", new Image("Images/Shiny.png"));
		i.put("signal", new Image("Images/Signal.png"));
		Iterator<Entry<String, Image>> it = i.entrySet().iterator();
		while (it.hasNext()) {
			Image i = it.next().getValue();
			i.setFilter(Image.FILTER_NEAREST);
		}
	}
	
	public void addImage(String name, String s) throws SlickException {
		i.put(name, new Image(s,false, Image.FILTER_NEAREST));
	}

	public void loadSounds() throws SlickException {
	}

	public Input getInput() {
		return gc.getInput();
	}

	public static EntityManager getEntityManager() {
		return mm.eM;
	}

	public static Image getImage(String name) {
		return mm.i.get(name.toLowerCase());
	}

	public static Sound getSound(String name) {
		return mm.so.get(name.toLowerCase());
	}
	
	

	public static boolean isLoaded() {
		return loaded;
	}

	public static ArrayList<Entity> getEntities() {
		return mm.eM.getEntities();
	}

	public static void spawnTempText(String s, float x, float y, Color c) throws SlickException {
		mm.eM.spawnTempText(s, x, y, c);
	}

	public static void spawnTempEffect(Shape s, long duration, Color c) throws SlickException {
		mm.eM.spawnTempEffect(s, duration, c);
	}

	public static void main(String[] args) throws SlickException {
		mm = new MainGame();
		AppGameContainer gc = new AppGameContainer(mm, 640, 480, false);
		gc.setTargetFrameRate(59);
		gc.setMinimumLogicUpdateInterval(16);
		gc.setMaximumLogicUpdateInterval(16);
		gc.setShowFPS(false);
		gc.setAlwaysRender(true);
		GameContainer.enableSharedContext();
		mm.gc = gc;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i].toLowerCase();
			if (arg.equals("-debug")) {
				MainGame.debug = true;
				System.out.println("Debug mode enabled.");
			} else if (arg.startsWith("-map:")) {
				arg = arg.replaceFirst("-map:", "");
				startMap = arg;
			}
		}
		gc.start();
	}

	public static int getViewDistance() {
		return mm.viewDistance;
	}

	public static Camera getCamera() {
		return mm.c;
	}

}
