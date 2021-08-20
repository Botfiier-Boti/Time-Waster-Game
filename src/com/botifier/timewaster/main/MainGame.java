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
import org.newdawn.slick.SpriteSheet;
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
	public static float windowScale = 1.5f;
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
		addImage("biggobboidle", "Images/BigGobboIdle.png");
		addImage("biggobbowalk", "Images/BigGobboWalk.png");
		addImage("biggobboattack", "Images/BigGobboAttack.png");
		addImage("fakeice", "Images/FakeIce.png");
		addImage("defaultshot", "Images/Shots.png");
		addImage("debugman", "Images/Testy.png");
		addImage("purplebag", "Images/Purplebag.png");
		addImage("whitebag", "Images/Whitebag.png");
		addImage("testchest", "Images/Testchest.png");
		addImage("boomerang", "Images/Boomerang.png");
		addImage("bee", "Images/Bee.png");
		addImage("beehive", "Images/Beehive.png");
		addImage("sheep", "Images/Sheep.png");
		addImage("idlesheep", "Images/Sheep-Idle.png");
		addImage("fireball", "Images/Fire.png");
		addImage("rock", "Images/Rock.png");
		addImage("smallrock", "Images/Rock-Small.png");
		addImage("rocktile", "Images/Tile.png");
		addImage("floatingwine", "Images/FloatingWine.png");
		addImage("wineattack", "Images/WineAttack.png");
		addImage("wineglass", "Images/WineGlass.png");
		addImage("reddrop", "Images/RedDrop.png");
		addImage("whitearrow", "Images/WhiteArrow.png");
		addImage("fontsmall", "Images/PressStart2P-sml.png");
		addImage("fontmedium", "Images/PressStart2P-mid.png");
		addImage("font", "Fonts/PressStart2P.png");
		addImage("head", "Images/Head.png");
		addImage("body", "Images/Body.png");
		addImage("brickwall", "Images/WallTile.png");
		addImage("blanktile", "Images/BlankTile.png");
		addImage("tiledarkup", "Images/TileDarkUp.png");
		addImage("playeridle", "Images/PlayerIdle.png");
		addImage("playerwalk", "Images/PlayerWalk.png");
		addImage("shadow", "Images/Shadow.png");
		addImage("torpedo", "Images/Torpedo.png");
		addImage("shiny", "Images/Shiny.png");
		addImage("signal", "Images/Signal.png");
		addImage("slow", "Images/Slow.png");
		addImage("invulnerable", "Images/Invulnerable.png");
		addImage("pickblock", "Images/Pickblock.png");
		addImage("v", "Images/V.png");
		addImage("notv", "Images/NotV.png");
		addImage("patterntile", "Images/PatternTile.png");
		addImage("spearman", "Images/SpearmanIdle.png");
		addImage("spearmanwalk", "Images/SpearmanWalk.png");
		addImage("fly", "Images/Fly.png");
		addImage("weaponslot", "Images/Inventory/WeaponSlot.png");
		addImage("ringslot", "Images/Inventory/RingSlot.png");
		addImage("abilityslot", "Images/Inventory/AbilitySlot.png");
		addImage("armorslot", "Images/Inventory/ArmorSlot.png");
		addImage("genericsword", "Images/GenericSword.png");
		addImage("hoppy", "Images/Hoppy.png");
		addImage("hoppyidle", "Images/HoppyTPose.png");
		addImage("construct", "Images/Construct.png");
		addImage("constructactivate", "Images/ConstructActivate.png");
		addImage("constructwalk", "Images/ConstructWalk.png");
		addImage("holetile", "Images/HoleTile.png");
		addImage("rockywall", "Images/RockyWall.png");
		addImage("rockywallns", "Images/RockyWallNoShadow.png");
		addImage("bandage", "Images/Bandage.png");
		addInSpriteSheet("crackedrock", "Images/CrackedRockSheet.png", 16, 16);
		addInSpriteSheet("outline16", "Images/16x16Outline.png", 16, 16);
		Iterator<Entry<String, Image>> it = i.entrySet().iterator();
		while (it.hasNext()) {
			Image i = it.next().getValue();
			i.setFilter(Image.FILTER_NEAREST);
		}
	}
	
	public void addImage(String name, String s) throws SlickException {
		i.put(name.toLowerCase(), new Image(s,false, Image.FILTER_NEAREST));
	}
	
	public void addInSpriteSheet(String name, String s, int width, int height) throws SlickException {
		SpriteSheet sh = new SpriteSheet(s, width, height);
		for (int y = 0; y < sh.getHeight()/height; y++) {
			for (int x = 0; x < sh.getWidth()/width; x++) {
				Image im = sh.getSprite(x, y);
				im.setFilter(Image.FILTER_NEAREST);
				i.put(name.toLowerCase()+x+"x"+y, im);
				System.out.println(name.toLowerCase()+x+"x"+y);
			}
		}
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
			} else if (arg.startsWith("-small")) {
				windowScale = 1f;
			}
		}
		camRatio *= windowScale;
		gc.setDisplayMode((int)(640*windowScale), (int)(480*windowScale), false);
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
