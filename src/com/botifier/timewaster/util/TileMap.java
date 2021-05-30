package com.botifier.timewaster.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.managers.EntityManager;

public class TileMap  {
	//ArrayList<Rectangle> r = new ArrayList<Rectangle>();
	public EntityManager eM;
	HashMap<Character, String> tileTypes = new HashMap<Character, String>();
	ArrayList<Entity> initialEntities = new ArrayList<Entity>();
	Vector2f spawnTile = new Vector2f(2,2);
	Vector2f origin = new Vector2f(0,0);
	String walkable = "";
	char[][] tiles;
	public Rectangle[][] tileCol;
	int width = 0;
	int height = 0;
	public Polygon big = new Polygon();
	public TileMap(char[][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.tileCol = new Rectangle[height][width];
	}
	
	public TileMap(String s) throws IOException {
		eM = new EntityManager(MainGame.mm);
		loadFromFile(s);
	}
	
	public void init() throws SlickException {
		if (eM == null)
			eM = new EntityManager(MainGame.mm);
		eM.init();
	}
	
	public boolean blocked( int tx, int ty) {
		int nx = (int) (tx - origin.x);
		int ny = (int) (ty - origin.y);
		if ((nx >= 0) && (ny >= 0) && (ny < tiles.length) && (nx < tiles[ny].length)) {
			char c = tiles[ny][nx];
			if (walkable.contains(c+"") == false) {
				return true;
			}
		}
		return false;
	}
	
	public boolean blocked(Rectangle r, int tx, int ty) {
		if (r == null)
			return blocked(tx,ty);
		if (tx > -1 && ty >-1 && ty < tiles.length && tx < tiles[ty].length ) {

			Rectangle b = tileCol[ty][tx];
			if (b != null) {
				if (b.intersects(r))
					return true;
			}
		}
		return false;
	}
	
	public void setSpawnPoint(int x, int y) {
		if (x < 0)
			return;
		if (x > width-1)
			return;
		if (y < 0)
			return;
		if (y > height-1)
			return;
		if (walkable.contains(""+tiles[y][x])) {
			spawnTile.x = x;
			spawnTile.y = y;	
		}
	}
	
	public void setTile(int x, int y, char tile) {
		if (x < 0)
			return;
		if (x > width-1)
			return;
		if (y < 0)
			return;
		if (y > height-1)
			return;
		tiles[y][x] = tile;
	}
	
	public char getTile(int x, int y) {
		if (x < 0)
			return ' ';
		if (x > width-1)
			return ' ';
		if (y < 0)
			return ' ';
		if (y > height-1)
			return ' ';
		return tiles[y][x];
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		handleEntities(gc, delta);
		/*Camera c = MainGame.getCamera();

		for (int y = 0; y < getHeightInTiles(); y++) {
			for (int x = 0; x < getWidthInTiles(); x++) {
				char tile = tiles[y][x];
				if (walkable.contains(tile+""))
					continue;
				Rectangle r = tileCol[y][x];
				float dist = c.getCenter().distance(new Vector2f((x * 16)+8+origin.x*16, (y * 16)+8+origin.x*16));
				if (dist > MainGame.getViewDistance()) {
					if (r != null) 
						r = null;
					continue;
				} else {
					if (r == null) {
						tileCol[y][x] = new Rectangle((x*16),(y*16),16,16);
					}
				}
			}
		}*/
		
	}
	
	public void handleEntities(GameContainer gc, int delta) throws SlickException {
		eM.update(gc, delta);
	}
	
	public void reset() {
		eM.getBullets().clear();
		eM.getOverlayedEffects().clear();
		eM.getEntities().clear();
		for (Entity e : getInitialEntities()) {
			eM.addEntity(e.copy());
		}
	}
	
	public void draw(GameContainer gc, Graphics g) {
		draw(gc,g,true);
	}
	
	public void draw(GameContainer gc, Graphics g, boolean vision) {
		Camera c = MainGame.getCamera();
		for (int y = 0; y < getHeightInTiles(); y++) {
			for (int x = 0; x < getWidthInTiles(); x++) {
				char tile = tiles[y][x];
				float dist = c.getCenter().distance(new Vector2f((x * 16)+8+origin.x*16, (y * 16)+8+origin.x*16));
				if (dist > MainGame.getViewDistance() && dist < MainGame.getViewDistance()*2 && vision == true) {
					if (tileTypes.containsKey(tile)) {
						Image e = MainGame.getImage(tileTypes.get(tile));
						if (e != null) {
							g.drawImage(e, x * 16+origin.x*16, y * 16+origin.y*16, new Color(255, 255, 255,0.5f));
						}
					} else if (tile == ' '){
						continue;
					} else {
						g.setColor(Color.darkGray);
						g.fillRect(x * 16+origin.x*16, y * 16+origin.y*16, 16, 16);
					}
					g.setColor(Color.white);
						
					/*switch (tile) {
					case '#':
						Image e = MainGame.getImage("BrickWall").copy();
						e.setImageColor(255, 255, 255,0.5f);
						g.drawImage(e, x * 16, y * 16);
						break;
					case 'F':
						Image i = MainGame.getImage("RockTile").copy();
						i.setImageColor(255, 255, 255,0.5f);
						g.drawImage(i, x * 16, y * 16);
						break;
					default:
						g.setColor(Color.darkGray);
						g.fillRect(x * 16, y * 16, 16, 16);
						break;
					}
					g.setColor(Color.white);*/
				} else if (dist <= MainGame.getViewDistance()){
					if (tileTypes.containsKey(tile)) {
						Image i = MainGame.getImage(tileTypes.get(tile));
						if (i != null)
							g.drawImage(i, x * 16+origin.x*16, y * 16+origin.y*16);
					} else if (tile == ' ') {
						continue;
					}else {
						g.setColor(Color.darkGray);
						g.fillRect(x * 16+origin.x*16, y * 16+origin.y*16, 16, 16);
						g.setColor(Color.black);
						g.drawRect(x*16+origin.x*16, y*16+origin.y*16, 16, 16);
					}
					g.setColor(Color.white);
					/*if (y < height && x < width && tileCol[y][x] != null) {
						g.draw(tileCol[y][x]);
					}*/
					/*switch (tile) {
					case '#':
						g.drawImage(MainGame.getImage("BrickWall"), x * 16, y * 16);
						break;
					case 'F':
						g.drawImage(MainGame.getImage("RockTile"), x * 16, y * 16);
						break;
					case 'U':
						g.drawImage(MainGame.getImage("TileDarkUp"), x * 16, y * 16);
						break;
					case 'B':
						g.drawImage(MainGame.getImage("BlankTile"), x * 16, y * 16);
						break;
					default:
						g.setColor(Color.darkGray);
						g.fillRect(x * 16, y * 16, 16, 16);
						g.setColor(Color.black);
						g.drawRect(x*16, y*16, 16, 16);
						break;
					}*/
				}

			}
		}
	}
	
	public void saveToFile(String name) throws IOException {
		saveToFile(new File(name));
	}
	
	public void saveToFile(File f) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		System.out.println("Writing mapfile...");
		System.out.println("Writing dimensions...");
		bw.write("width:"+width+'\n');
		bw.write("height:"+height+'\n');
		System.out.println("Writing spawn tile...");
		bw.write("spawntile:"+(int)spawnTile.x+","+(int)spawnTile.y+'\n');
		System.out.println("Writing tile types...");
		for (Character c : tileTypes.keySet()) {
			if (walkable.contains(c+"")) {
				bw.write("dwalkable "+c+" "+tileTypes.get(c)+'\n');
			} else{
				bw.write("define "+c+" "+tileTypes.get(c)+'\n');
			}
		}
		System.out.println("Writing entities...");
		for (Entity e : initialEntities) {
			String name = e.getClass().getName();
			if (EntityManager.isAlias(e.getClass().getName()) == true); 
				name = EntityManager.getAlias(e.getClass());
			String param = e.getParameters();
			bw.write("addentity "+name+", "+param+"\n");
			System.out.println("Wrote entity: \""+name+"\" with parameters: "+param);
		}
		System.out.println("Writing map layout...");
		bw.write("startmap\n");
		for (int y = 0; y < getHeightInTiles(); y++) {
			for (int x = 0; x < getWidthInTiles(); x++) {
				bw.write(tiles[y][x]);
			}
			bw.newLine();
		}
		bw.write("endmap\n");
		bw.close();
		System.out.println("Successfully saved map!");
	}
	
	public void loadFromFile(File f) throws IOException {
		loadFromFile(new FileInputStream(f));
	}
	
	public void loadFromFile(String name) throws IOException {
		InputStream fis = org.newdawn.slick.util.ResourceLoader
				.getResourceAsStream(name);
		loadFromFile(fis);
	}
	
	public void loadFromFile(InputStream fis) throws IOException {
		Reader r = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(r);
		if (eM == null) {
			eM = new EntityManager(MainGame.mm);
			try {
				eM.init();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		tileTypes.clear();
		initialEntities.clear();
		System.out.println("Reading mapfile...");
		String line = "";
		while ((line = br.readLine()) != null) {
			line = line.replaceAll("([ :])", "");
			line.toLowerCase();
			if (line.startsWith("width")) {
				line = line.replaceFirst("width", "");
				if (Math2.isInteger(line)) {
					width = Integer.parseInt(line);
					System.out.println("Map width set to: "+width);
				} else {
					System.out.println("Invalid input for width: "+line+".");
					width = 0;
					continue;
				}
				continue;
			} else if (line.startsWith("height")) {
				line = line.replaceFirst("height", "");
				if (Math2.isInteger(line)) {
					height = Integer.parseInt(line);
					System.out.println("Map height set to: "+height);
				} else {
					System.out.println("Invalid input for height: "+line+".");
					height = 0;
					continue;
				}
				continue;
			} else if (line.startsWith("define")) {
				line = line.replaceFirst("define", "");
				char letter = line.charAt(0);
				line = line.replaceFirst(letter+"", "");
				tileTypes.put(letter, line);
				System.out.println("Added tile identifier: "+letter);
				continue;
			} else if (line.startsWith("dwalkable")) {
				line = line.replaceFirst("dwalkable", "");
				char letter = line.charAt(0);
				line = line.replaceFirst(letter+"", "");
				tileTypes.put(letter, line);
				walkable = walkable + letter;
				System.out.println("Added walkable tile identifier: "+letter);
				continue;
			} else if (line.startsWith("addentity")) {
				line = line.replaceFirst("addentity", "");
				String[]  s = line.split(",");
				if (s != null && s.length > 2) {
					String eName = s[0];
					int iterations = 1;
					int ignore = 1;
					if (Math2.isInteger(s[0])) {
						eName = s[1];
						iterations = Integer.valueOf(s[0]);
						ignore = 2;
					}
					Object[] args = new Object[s.length-ignore];
					try {
						if (EntityManager.isAlias(eName) || Class.forName(eName) != null) {
							if (args.length == EntityManager.getEntityInstArgs(eName).length) {
								for (int i = ignore; i < s.length; i++) {
									String st = s[i];
									if (Math2.isInteger(st)) {
										args[i-ignore] = Integer.parseInt(st);
									} else if (Math2.isLong(st)) {
										args[i-1] = Long.parseLong(st);
									} else if (Math2.isFloat(st)) {
										args[i-ignore] = Float.parseFloat(st);
									} else if (st.equalsIgnoreCase("true")) {
										args[i-ignore] = true;
									} else if (st.equalsIgnoreCase("false")) {
										args[i-ignore] = false;
									} else if (st.equalsIgnoreCase("null")) {
										args[i-1] = null;
									} else {
										args[i-ignore] = st;
									}
								}
								for (int i = 0; i < iterations; i++) {
									Entity e = EntityManager.createEntityOfType(eName, args);
									eM.addEntity(e);
									Entity e2 = EntityManager.createEntityOfType(eName, args);
									initialEntities.add(e2);
								}
								System.out.println("Spawned Entity "+eName+".");
							} else {
								System.out.println("Wrong number of arguments for "+eName);
							}
						} else {
							System.out.println("No such entity defined: "+eName);
						}
					} catch (NumberFormatException | ClassNotFoundException e) {
						System.out.println("No such entity defined: "+eName);
					}
					
				} else {
					System.out.println("Not enough arguments for an entity");
				}
				
			} else if (line.startsWith("spawntile")){ 
				line = line.replaceFirst("spawntile", "");
				String[]  s = line.split(",");
				if (s.length == 2) {
					if (Math2.isInteger(s[0]) && Math2.isInteger(s[1])) {
						spawnTile.x = Integer.parseInt(s[0]);
						spawnTile.y = Integer.parseInt(s[1]);
					} else {
						System.out.println("Invalid input for spawntile: "+line);
						spawnTile.x = 2;
						spawnTile.y = 2;
						continue;
					}
				}
				System.out.println("Set spawn tile to: ("+(int)spawnTile.x+","+(int)spawnTile.y+")");
			} else if (line.startsWith("startmap")) {
				this.tiles = new char[height][width];
				int lineNo = 0;
				System.out.println("Reading map...");
				while ((line = br.readLine()) != "endmap" && (line != null) && lineNo < height) {
					for (int i = 0; i < width; i++) {
						if (i < line.length()) {
							tiles[lineNo][i] = line.charAt(i);
						}
					}
					lineNo++;
				}
				System.out.println("Done!");
				continue;
			}
		}
		this.tileCol = new Rectangle[height][width];
		System.out.println("Successfully loaded map.");
	}
	
	String createInitialEntity(String name, String[] args) {
		String build = "addEntity "+name;
		for (int i = 0; i < args.length; i++) {
			build += ", "+args[i];
		}
		return build;
	}
	
	public void resize(int width, int height) {
		char[][] newTiles = new char[height][width];
		for (int y = 0; y < (tiles.length > height ? height : tiles.length); y++) {
			for (int x = 0; x < (tiles[0].length > width ? width : tiles[0].length); x++) {
				newTiles[y][x] = tiles[y][x];
			}
		}
		this.width = width;
		this.height = height;
		this.tiles = newTiles;
		System.out.println("Resized map to: "+width+","+height);
	}
	
	public void addTileType(char symbol, String name, boolean walkable) {
		if (!tileTypes.containsKey(symbol))
			tileTypes.put(symbol, name);
		if (walkable) 
			this.walkable += symbol+"";
	}
	
	public void setOrigin(float x, float y) {
		origin.set(x, y);
	}
	
	public char getTileChar(String name) {
		if (hasTileType(name)) {
			for (Entry<Character, String> e : tileTypes.entrySet()) {
				if (e.getValue().equalsIgnoreCase(name))
					return e.getKey();
			}
		}
		System.out.println("Cannot find tile: "+name);
		return 0;
	}
	
	public String getTileName(char c) {
		return tileTypes.get(c);
	}
	
	public EntityManager getEntityManager() {
		return eM;
	}
	
	public HashMap<Character, String> getTileTypes() {
		return tileTypes;
	}
	
	public ArrayList<Entity> getInitialEntities() {
		return initialEntities;
	}
	
	public boolean hasTileType(String name) {
		return tileTypes.containsValue(name);
	}
	
	public boolean isTileTypeTaken(char c) {
		return tileTypes.containsKey(c);
	}

	public int getHeightInTiles() {
		return height;
	}

	public int getWidthInTiles() {
		return width;
	}
	
	public Vector2f getTileLocationRelativeToOrigin(Vector2f v) {
		float x = v.x+(origin.x*16-8);
		float y = v.y+(origin.y*16-8);
		return new Vector2f(x,y);
	}
	
	public Vector2f getOrigin() {
		return origin;
	}
	
	public Vector2f getSpawnPoint() {
		return new Vector2f(((spawnTile.x+origin.x)*16)-8, ((spawnTile.y+origin.y)*16)-8);
	}
	
	public Vector2f getCenter() {
		return new Vector2f(((width/2)*16+origin.x*16)+8, ((height/2)*16+origin.y*16)+8);
	}

}
