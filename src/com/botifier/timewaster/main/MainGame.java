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
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import com.botifier.timewaster.states.MapEditorState;
import com.botifier.timewaster.states.OverworldState;
import com.botifier.timewaster.util.Camera;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.GUI;
import com.botifier.timewaster.util.TileMap;
import com.botifier.timewaster.util.managers.EntityManager;

public class MainGame extends StateBasedGame {
	private static final String versionId = "0.0.8";
	private static boolean loaded = false;
	public static String startMap = "testmap.map";
	public static boolean debug = false;
	public static boolean displayHitboxes = false;
	public static float camRatio = 2f;
	public static ArrayList<String> s = new ArrayList<String>();
	public static AngelCodeFont ttf, ttfS, ttfB;
	public static MainGame mm; 
	public HashMap<String, Image> i = new HashMap<String, Image>();
	public HashMap<String, Sound> so = new HashMap<String, Sound>();
	public long tps = 0, ticks = 0, tticks = 0, delta = 0, natspawns = 0;
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
		i.put("slow", new Image("Images/Slow.png"));
		i.put("invulnerable", new Image("Images/Invulnerable.png"));
		i.put("pickblock", new Image("Images/Pickblock.png"));
		i.put("v", new Image("Images/V.png"));
		i.put("notv", new Image("Images/NotV.png"));
		i.put("patterntile", new Image("Images/PatternTile.png"));
		i.put("spearman", new Image("Images/SpearManIdle.png"));
		i.put("spearmanwalk", new Image("Images/SpearManWalk.png"));
		i.put("fly",new Image("Images/Fly.png"));
		i.put("weaponslot", new Image("Images/Inventory/WeaponSlot.png"));
		i.put("ringslot", new Image("Images/Inventory/RingSlot.png"));
		i.put("abilityslot", new Image("Images/Inventory/AbilitySlot.png"));
		i.put("armorslot", new Image("Images/Inventory/ArmorSlot.png"));
		i.put("genericsword", new Image("Images/GenericSword.png"));
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
		return ((OverworldState)mm.getState(1)).getEntityManager();
	}
	
	public static HashMap<String, Image> getAllImages() {
		return mm.i;
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
		return getEntityManager().getEntities();
	}

	public static void spawnTempText(String s, float x, float y, Color c) throws SlickException {
		getEntityManager().spawnTempText(s, x, y, c);
	}

	public static void spawnTempEffect(Shape s, long duration, Color c) throws SlickException {
		getEntityManager().spawnTempEffect(s, duration, c);
	}

	public static void main(String[] args) throws SlickException {
		mm = new MainGame();
		AppGameContainer gc = new AppGameContainer(mm, 640, 480, false);
		//gc.setTargetFrameRate(59);
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
	
	public static GUI getGUI() {
		return mm.getCurrentStateID() == MapEditorState.ID ? ((MapEditorState)mm.getState(MapEditorState.ID)).getGUI() : ((OverworldState)mm.getState(OverworldState.ID)).getGUI();
	}

	public static int getViewDistance() {
		return  mm.getCurrentStateID() == MapEditorState.ID ? ((mm.gc.getWidth())+(mm.gc.getHeight()))/2 : ((OverworldState)mm.getState(OverworldState.ID)).viewDistance;
	}

	public static Camera getCamera() {
		return mm.getCurrentStateID() == MapEditorState.ID ? ((MapEditorState)mm.getState(MapEditorState.ID)).c : ((OverworldState)mm.getState(OverworldState.ID)).c;
	}
	
	public static TileMap getCurrentMap() {
		return ((OverworldState)MainGame.mm.getState(OverworldState.ID)).m;
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
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
		EntityManager.loadAliases();
		EntityManager.loadEntities();
		ttf = new AngelCodeFont("PressStart2P-mid.fnt", getImage("fontmedium"));
		ttfB = new AngelCodeFont("Fonts/PressStart2P.fnt", getImage("font"));
		ttfS = new AngelCodeFont("PressStart2P-sml.fnt", getImage("fontsmall"));
		gc.getGraphics().setFont(ttf);
		addState(new OverworldState());
		addState(new MapEditorState());
		//this.enterState(2);
		loaded = true;
	}

}
