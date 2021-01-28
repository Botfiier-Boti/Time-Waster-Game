package com.botifier.timewaster.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.managers.EntityManager;

public class TileMap  {
	//ArrayList<Rectangle> r = new ArrayList<Rectangle>();
	HashMap<Character, String> tileTypes = new HashMap<Character, String>();
	Vector2f spawnTile = new Vector2f(2,2);
	String walkable = "";
	char[][] tiles;
	int width = 0;
	int height = 0;
	public Polygon big = new Polygon();
	public TileMap(char[][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
	}
	
	public TileMap(String s) throws IOException {
		loadFromFile(s);
	}
	
	public boolean blocked( int tx, int ty) {
		if ((tx > -1) && (ty >-1) && (ty < tiles.length) && (tx < tiles[ty].length)) {
			char c = tiles[ty][tx];
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
			char c = tiles[ty][tx];
			if ( walkable.contains(c+"") == false) {
				Rectangle b = new Rectangle((tx)*16,(ty)*16,16,16);
				//this.r.add(b);
				if (b.intersects(r))
					return true;
				
			}
		}
		return false;
	}
	
	public void draw(GameContainer gc, Graphics g) {
		Camera c = MainGame.getCamera();
		for (int y = 0; y < getHeightInTiles(); y++) {
			for (int x = 0; x < getWidthInTiles(); x++) {
				char tile = tiles[y][x];
				if (c.centerE.getLocation().distance(new Vector2f((x * 16)+8, (y * 16)+8)) >= MainGame.getViewDistance()) {
					if (tileTypes.containsKey(tile)) {
						Image e = MainGame.getImage(tileTypes.get(tile));
						if (e != null) {
							e = e.copy();
							e.setImageColor(255, 255, 255,0.5f);
							g.drawImage(e, x * 16, y * 16);
						}
					} else {
						g.setColor(Color.darkGray);
						g.fillRect(x * 16, y * 16, 16, 16);
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
				} else {
					if (tileTypes.containsKey(tile)) {
						Image i = MainGame.getImage(tileTypes.get(tile));
						if (i != null)
							g.drawImage(i, x * 16, y * 16);
					}else {
						g.setColor(Color.darkGray);
						g.fillRect(x * 16, y * 16, 16, 16);
						g.setColor(Color.black);
						g.drawRect(x*16, y*16, 16, 16);
					}
					g.setColor(Color.white);
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
	
	public void loadFromFile(String name) throws IOException {
		InputStream fis = org.newdawn.slick.util.ResourceLoader
				.getResourceAsStream(name);
		Reader r = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(r);
		tileTypes.clear();
		System.out.println("Reading mapfile "+name+"...");
		String line = "";
		while ((line = br.readLine()) != null) {
			line = line.replaceAll("([ :])", "");
			if (line.startsWith("width")) {
				line = line.replaceFirst("width", "");
				width = Integer.parseInt(line);
				System.out.println("Map width set to: "+width);
				continue;
			} else if (line.startsWith("height")) {
				line = line.replaceFirst("height", "");
				height = Integer.parseInt(line);
				System.out.println("Map height set to: "+height);
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
					Object[] args = new Object[s.length-1];
					String eName = s[0];
					if (EntityManager.hasAlias(eName)) {
						if (args.length == EntityManager.getEntityInstArgs(eName).length) {
							for (int i = 1; i < s.length; i++) {
								String st = s[i];
								if (Math2.isInteger(st)) {
									args[i-1] = Integer.parseInt(st);
								} else if (Math2.isLong(st)) {
									args[i-1] = Long.parseLong(st);
								} else if (st.equalsIgnoreCase("true")) {
									args[i-1] = true;
								} else if (st.equalsIgnoreCase("false")) {
									args[i-1] = false;
								} else if (st.equalsIgnoreCase("null")) {
									args[i-1] = null;
								} else {
									args[i-1] = st;
								}
							}
							MainGame.getEntityManager().addEntity(EntityManager.createEntityOfType(eName, args));
							System.out.println("Spawned Entity "+eName+".");
						} else {
							System.out.println("Wrong number of arguments for "+eName);
						}
					} else {
						System.out.println("No such entity defined: "+eName);
					}
					
				} else {
					System.out.println("Not enough arguments for an entity");
				}
				
			} else if (line.startsWith("spawntile")){ 
				line = line.replaceFirst("spawntile", "");
				String[]  s = line.split(",");
				if (s.length == 2) {
					spawnTile.x = Integer.parseInt(s[0]);
					spawnTile.y = Integer.parseInt(s[1]);
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
		
		System.out.println("Successfully loaded map.");
	}
	

	public int getHeightInTiles() {
		return height;
	}

	public int getWidthInTiles() {
		return width;
	}
	
	public Vector2f getSpawnPoint() {
		return new Vector2f((spawnTile.x*16)-8, (spawnTile.y*16)-8);
	}
	
	public Vector2f getCenter() {
		return new Vector2f((width*16)/2, (height*16)/2);
	}

}
