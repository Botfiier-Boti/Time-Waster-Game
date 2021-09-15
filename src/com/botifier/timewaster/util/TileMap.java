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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.managers.EntityManager;

/**
 * Tile map class
 * @author Botifier
 *
 */
public class TileMap  {
	/**
	 * The entity manager 
	 */
	private EntityManager eM;
	/**
	 * Map containing defined tile types
	 */
	private HashMap<Character, String> tileTypes = new HashMap<Character, String>();
	/**
	 * Map containing the entities that are added upon map load
	 */
	private ArrayList<Entity> initialEntities = new ArrayList<Entity>();
	/**
	 * The tile in which the player is spawned
	 */
	private Vector2f spawnTile = new Vector2f(2,2);
	/**
	 * The center position of the map
	 */
	private Vector2f origin = new Vector2f(0,0);
	/**
	 * Tile characters that are considered walkab;e
	 */
	private String walkable = "";
	/**
	 * Tile array
	 */
	private char[][] tiles;
	/**
	 * Tile overlay array
	 */
	private char[][] tilesOverlay;
	/**
	 * Extra layer arrays
	 */
	private ArrayList<char[][]> layers = new ArrayList<char[][]>();
	/**
	 * Tile collider rectangles
	 */
	private Rectangle[][] tileCol;
	/**
	 * Width of the map in tiles
	 */
	private int width = 0;
	/**
	 * Height of the map in tiles
	 */
	private int height = 0;
	
	/**
	 * TileMap constructor
	 * @param tiles char[][] tile array
	 */
	public TileMap(char[][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.tileCol = new Rectangle[height][width];
		this.tilesOverlay = new char[height][width];
	}
	
	/**
	 * TileMap constructor
	 * @param s String location of map file
	 * @throws IOException
	 */
	public TileMap(String s) throws IOException {
		eM = new EntityManager(MainGame.mm);
		loadFromFile(s);
	}
	
	/**
	 * Initializes the entity manager
	 * @throws SlickException
	 */
	public void init() throws SlickException {
		if (eM == null)
			eM = new EntityManager(MainGame.mm);
		eM.init();
	}
	
	/**
	 * Checks whether or not specified tile is unwalkable
	 * @param tx int X position in array to check
	 * @param ty int Y position in array to check
	 * @return boolean Whether or not the tile is blocked
	 */
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
	
	/**
	 * Checks whether or not specified tile is unwalkable using rectangles
	 * @param r Rectangle To check
	 * @param tx int X position in array to check
	 * @param ty int Y position in array to check
	 * @return boolean Whether or not the tile is blocked
	 */
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
	
	/**
	 * Sets the player spawn point 
	 * @param x int 0 <= x <= width-1
	 * @param y int 0 <= y <= height-1
	 */
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
	
	/**
	 * Sets the tile at specified position
	 * @param x int 0 <= x <= width-1
	 * @param y int 0 <= y <= height-1
	 * @param tile char New tile 
	 */
	public void setTile(int x, int y, char tile) {
		if (x < 0)
			return;
		if (x > width-1)
			return;
		if (y < 0)
			return;
		if (y > height-1)
			return;
		getLayers().get(0)[y][x] = tile;
	}
	
	/**
	 * Sets the tile at specified position and layer
	 * @param x int 0 <= x <= width-1
	 * @param y int 0 <= y <= height-1
	 * @param tile char New layer
	 * @param layer int Layer to set
	 */
	public void setTile(int x, int y, char tile, int layer) {
		if (x < 0)
			return;
		if (x > width-1)
			return;
		if (y < 0)
			return;
		if (y > height-1)
			return;
		getLayers().get(layer)[y][x] = tile;
	}
	
	/**
	 * Returns the tile at specified position
	 * @param x int 0 <= x <= width-1
	 * @param y int 0 <= y <= height-1
	 * @return char Tile at position
	 */
	public char getTile(int x, int y) {
		if (x < 0)
			return ' ';
		if (x > width-1)
			return ' ';
		if (y < 0)
			return ' ';
		if (y > height-1)
			return ' ';
		return getLayers().get(0)[y][x];
	}
	
	/**
	 * Returns the tile at specified position in specified layer
	 * @param x int 0 <= x <= width-1
	 * @param y int 0 <= y <= height-1
	 * @param later int Layer to check
	 * @return char Tile at position
	 */
	public char getTile(int x, int y, int layer) {
		if (x < 0)
			return ' ';
		if (x > width-1)
			return ' ';
		if (y < 0)
			return ' ';
		if (y > height-1)
			return ' ';
		return getLayers().get(layer)[y][x];
	}
	
	/**
	 * Updates the TileMap
	 * @param gc GameContainer
	 * @param delta int Time since last update
	 * @throws SlickException
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		handleEntities(gc, delta);
	}
	
	/**
	 * Updates the Entity Manager
	 * @param gc GameContainer
	 * @param delta int Time since last update
	 * @throws SlickException
	 */
	public void handleEntities(GameContainer gc, int delta) throws SlickException {
		eM.update(gc, delta);
	}
	
	/**
	 * Resets tilemap to original state
	 */
	public void reset() {
		eM.getBullets().clear();
		eM.getOverlayedEffects().clear();
		eM.getEntities().clear();
		for (Entity e : getInitialEntities()) {
			eM.addEntity(e.copy());
		}
	}
	
	/**
	 * Draws the TileMap
	 * @param gc GameContainer
	 * @param g Graphics Renderer
	 */
	public void draw(GameContainer gc, Graphics g) {
		draw(gc,g,true);
	}
	
	/**
	 * Draws the TileMap
	 * @param gc GameContainer
	 * @param g Graphics Renderer
	 * @param vision boolean Vision toggle
	 */
	public void draw(GameContainer gc, Graphics g, boolean vision) {
		//The camera
		Camera c = MainGame.getCamera();
		for (int y = 0; y < getHeightInTiles(); y++) { 
			for (int x = 0; x < getWidthInTiles(); x++) {
				for (char[][] layer : getLayers()) {
					//Current tile to check
					char tile = layer[y][x];
					//Distance from the camera's center
					float dist = c.getCenter().distance(new Vector2f((x * 16)+8+origin.x*16, (y * 16)+8+origin.x*16));
					//Checks if distance is greater than the maxViewDistance and less than double the max view distance and if vision is true
					if (dist > MainGame.getViewDistance() && dist < MainGame.getViewDistance()*2 && vision == true) {
						//Checks if the tile exists
						if (tileTypes.containsKey(tile)) { 
							//Gets the tile image
							Image e = MainGame.getImage(tileTypes.get(tile));
							//Checks if the image exists
							if (e != null) {
								//Draws a faded out version of the image
								g.drawImage(e, x * 16+origin.x*16, y * 16+origin.y*16, new Color(255, 255, 255,0.5f));
							}
						} else if (tile == ' '){
							//Skips if the tile is space
							continue;
						} else {
							//Draws a gray rectangle if the tile does not exist and is not a spacebar character
							g.setColor(Color.darkGray);
							g.fillRect(x * 16+origin.x*16, y * 16+origin.y*16, 16, 16);
						}
						//Resets the color
						g.setColor(Color.white);
					} else if (dist <= MainGame.getViewDistance()){
						//Checks if the tile exists
						if (tileTypes.containsKey(tile)) {
							//Gets the tile image
							Image i = MainGame.getImage(tileTypes.get(tile));
							//Checks if the tile Image exists
							if (i != null)
								//Draws the tile image
								g.drawImage(i, x * 16+origin.x*16, y * 16+origin.y*16);
						}
						//Resets the color
						g.setColor(Color.white);
					}
				}
			}
		}
	}
	
	/**
	 * Saves the TileMap to a file
	 * @param name String File name
	 * @throws IOException
	 */
	public void saveToFile(String name) throws IOException {
		saveToFile(new File(name));
	}
	
	/**
	 * Saves the TileMap to a file
	 * @param f File File to write
	 * @throws IOException
	 */
	public void saveToFile(File f) throws IOException {
		//Creates a BufferedWriter
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		System.out.println("Writing mapfile...");
		//Writes the dimensions to the file
		System.out.println("Writing dimensions...");
		bw.write("width:"+width+'\n');
		bw.write("height:"+height+'\n');
		//Writes the spawn location to the file
		System.out.println("Writing spawn tile...");
		bw.write("spawntile:"+(int)spawnTile.x+","+(int)spawnTile.y+'\n');
		//Writes the tile types to the file
		System.out.println("Writing tile types...");
		for (Character c : tileTypes.keySet()) {
			if (walkable.contains(c+"")) {
				bw.write("dwalkable "+c+" "+tileTypes.get(c)+'\n');
			} else{
				bw.write("define "+c+" "+tileTypes.get(c)+'\n');
			}
		}
		//Writes the initial entities to the file
		System.out.println("Writing entities...");
		for (Entity e : initialEntities) {
			String name = e.getClass().getName();
			if (EntityManager.isAlias(e.getClass().getName()) == true)
				name = EntityManager.getAlias(e.getClass());
			String param = e.getParameters();
			bw.write("addentity 1, "+name+", "+param+"\n");
			System.out.println("Wrote entity: \""+name+"\" with parameters: "+param);
		}
		//Writes the map layout to the file
		System.out.println("Writing map layout...");
		bw.write("startmap\n");
		for (int y = 0; y < getHeightInTiles(); y++) {
			for (int x = 0; x < getWidthInTiles(); x++) {
				bw.write(tiles[y][x]);
			}
			bw.newLine();
		}
		//Tells the interpreter that the map is done
		bw.write("endmap\n");
		//Writes the remaining layers
		System.out.println("Writing layer map layout(s)...");
		for (int i = 1; i < getLayers().size(); i++) {
			char[][] layer = getLayers().get(i);
			bw.write("startlayer\n");
			for (int y = 0; y < getHeightInTiles(); y++) {
				for (int x = 0; x < getWidthInTiles(); x++) {
					bw.write(layer[y][x]);
				}
				bw.newLine();
			}
			bw.write("endlayer\n");
		}
		//Done!
		bw.close();
		System.out.println("Successfully saved map!");
	}
	
	/**
	 * Loads map from specified file
	 * @param f File To load
	 * @throws IOException
	 */
	public void loadFromFile(File f) throws IOException {
		loadFromFile(new FileInputStream(f));
	}
	
	/**
	 * Loads map from specified file
	 * @param name String File location
	 * @throws IOException
	 */
	public void loadFromFile(String name) throws IOException {
		InputStream fis = org.newdawn.slick.util.ResourceLoader
				.getResourceAsStream(name);
		loadFromFile(fis);
	}
	
	/**
	 * Loads map from specified file
	 * @param fis InputStream InputStream of the file
	 * @throws IOException
	 */
	public void loadFromFile(InputStream fis) throws IOException {
		//Creates a Reader using the InputStream
		Reader r = new InputStreamReader(fis);
		//Creates a BufferedReader using the Reader
		BufferedReader br = new BufferedReader(r);
		//Checks if the EntityManager already exists if not create and initialize it
		if (eM == null) {
			eM = new EntityManager(MainGame.mm);
			try {
				eM.init();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		//Clears the current layers
		getLayers().clear();
		//Clears all of the tile types
		tileTypes.clear();
		//Clears all of the initial entities
		initialEntities.clear();
		//Begin reading the mapfile
		System.out.println("Reading mapfile...");
		//Current line being read
		String line = "";
		//Check if the line exists
		while ((line = br.readLine()) != null) {
			//Removes characters from the line
			line = line.replaceAll("([ :])", "");
			//Converts all characters in the line to lower case so that it is case-insensitive
			line.toLowerCase();
			//Comment 
			if (line.startsWith("//"))
				continue;
			//Checks if the line starts with specified keywords
			if (line.startsWith("width")) { //Map Width
				//Removes keyword from the line
				line = line.replaceFirst("width", "");
				//Check if the remaining string is an integer
				if (Math2.isInteger(line)) {
					//Converts remaining String to an integer and sets width to the result
					width = Integer.parseInt(line);
					System.out.println("Map width set to: "+width);
				} else {
					//Sends error message and sets width to zero if the line is not an integer
					System.out.println("Invalid input for width: "+line+".");
					width = 0;
					continue;
				}
				continue;
			} else if (line.startsWith("height")) { //Map Height
				//Removes keyword from the line
				line = line.replaceFirst("height", "");
				//Checks if the remaining string is an integer
				if (Math2.isInteger(line)) {
					//Converts the string to an integer and sets height to the result
					height = Integer.parseInt(line);
					System.out.println("Map height set to: "+height);
				} else {
					//Sends error message and sets height to zero if the line is not an integer
					System.out.println("Invalid input for height: "+line+".");
					height = 0;
					continue;
				}
				continue;
			} else if (line.startsWith("define")) { //Defines a tile
				//Removes keyword from the line
				line = line.replaceFirst("define", "");
				//Gets the first remaining character
				char letter = line.charAt(0);
				//Removes said character from line to act as Image identifier
				line = line.substring(1);
				//Adds the character and image identifier to tileTypes
				tileTypes.put(letter, line);
				System.out.println("Added tile identifier: "+letter + " using "+line);
				continue;
			} else if (line.startsWith("dwalkable")) { //Defines a walkable tile
				//Removes keyword from the line
				line = line.replaceFirst("dwalkable", "");
				//Gets the first remaining character
				char letter = line.charAt(0);
				//Removes said character from the line to act as Image identifier
				line = line.substring(1);
				//Adds the character and Image identifier to tileTypes
				tileTypes.put(letter, line);
				//Adds the character to the list of walkable tiles
				walkable = walkable + letter;
				System.out.println("Added walkable tile identifier: "+letter+" using "+line);
				continue;
			} else if (line.startsWith("addentity")) { //Adds an initial entity
				//Removes keyword from the line
				line = line.replaceFirst("addentity", "");
				//Splits the line by commas
				String[]  s = line.split(",");
				//Makes sure the split didn't fail and that the split is larger than 2 arguments
				if (s != null && s.length > 2) {
					//Takes the first String in the split as the name of the Entity
					String eName = s[0];
					//Number of entities that will be added of this type
					int iterations = 1;
					//How many arguments to ignore
					int ignore = 1;
					//Checks if the first String in the split is an integer
					if (Math2.isInteger(s[0])) {
						//Changes the name to the second String
						eName = s[1];
						//Changes the number of iterations to the value of s[0]
						iterations = Integer.valueOf(s[0]);
						//Sets the ignore variable to 2
						ignore = 2;
					}
					//The remaining arguments
					Object[] args = new Object[s.length-ignore];
					//So that the program doesn't crash when the entity does not exist or if a argument is invalid
					try {
						//Checks if an entity of eName exists
						if (EntityManager.isAlias(eName) || Class.forName(eName) != null) {
							//Checks if there is the correct number of arguments
							if (args.length == EntityManager.getEntityInstArgs(eName).length) {
								//Iterates through the remaining arguments and adds them to args
								for (int i = ignore; i < s.length; i++) {
									//The current String
									String st = s[i];
									if (Math2.isInteger(st)) { //Checks if the string is an integer
										args[i-ignore] = Integer.parseInt(st);
									} else if (Math2.isLong(st)) { //Checks if the string is a long
										args[i-ignore] = Long.parseLong(st);
									} else if (Math2.isFloat(st)) { //Checks if the string is a float
										args[i-ignore] = Float.parseFloat(st);
									} else if (st.equalsIgnoreCase("true")) { //Checks if the string is a boolean of value true
										args[i-ignore] = true;
									} else if (st.equalsIgnoreCase("false")) { //Checks if the string is a boolean of value false
										args[i-ignore] = false;
									} else if (st.equalsIgnoreCase("null")) { //Checks if the string is "null"
										args[i-1] = null;
									} else { 
										//Sets the argument to the string directly
										args[i-ignore] = st;
									}
								}
								//Spawns the entity iterations number of times and adds them to the initial entity list
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
						System.out.println("Error occured when reading entity");
					}
					
				} else {
					System.out.println("Not enough arguments for an entity");
				}
				
			} else if (line.startsWith("spawntile")){ //Sets the spawn tile
				//Removes keyword from the line
				line = line.replaceFirst("spawntile", "");
				//Splits the string by commas
				String[]  s = line.split(",");
				//Checks if there are enough arguments
				if (s.length == 2) {
					if (Math2.isInteger(s[0]) && Math2.isInteger(s[1])) {
						spawnTile.x = Integer.parseInt(s[0]);
						spawnTile.y = Integer.parseInt(s[1]);
					} else {
						spawnTile.x = 2;
						spawnTile.y = 2;
						System.out.println("Invalid input for spawntile: "+line);
						continue;
					}
				} else {
					spawnTile.x = 2;
					spawnTile.y = 2;
					System.out.println("Wrong amount of arguments for spawntile");
					continue;
				}
				System.out.println("Set spawn tile to: ("+(int)spawnTile.x+","+(int)spawnTile.y+")");
			} else if (line.startsWith("startmap")) { //Begins map reading
				//Creates a new char array using width and height
				this.tiles = new char[height][width];
				//Adds the tile array to layers
				getLayers().add(0, tiles);
				//Counts the current line number
				int lineNo = 0;
				System.out.println("Reading map...");
				//Loops until the line becomes endmap, line becomes null, or the lineNumber is greater than or equal to height
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
			}else if (line.startsWith("startlayer")) { //Begins layer reading
				//Creates a new char array using width and height
				this.tilesOverlay = new char[height][width];
				//Counts the current line number
				int lineNo = 0;
				//Adds the overlay to layers
				getLayers().add(tilesOverlay);
				System.out.println("Reading layer map...");
				//Loops until the line becomes endlayer, line is null, or line is greater than or equal to height
				while ((line = br.readLine()) != "endlayer" && (line != null) && lineNo < height) {
					for (int i = 0; i < width; i++) {
						if (i < line.length()) {
							tilesOverlay[lineNo][i] = line.charAt(i);
						}
					}
					lineNo++;
				}
				System.out.println("Done!");
				continue;
			}
		}
		//Reset the tile colliders
		this.tileCol = new Rectangle[height][width];
		System.out.println("Successfully loaded map.");
	}
	
	/**
	 * Creates initial entity string
	 * @param name String Entity name
	 * @param args String[] Arguments
	 * @return
	 */
	protected String createInitialEntity(String name, String[] args) {
		String build = "addEntity "+name;
		for (int i = 0; i < args.length; i++) {
			build += ", "+args[i];
		}
		return build;
	}
	
	/**
	 * Resizes the map
	 * @param width int New width
	 * @param height int New height
	 */
	public void resize(int width, int height) {
		//Sets the width
		this.width = width;
		//Sets the height
		this.height = height;
		//Updates layers just in case
		ArrayList<char[][]> backup = new ArrayList<char[][]>();
		backup.addAll(getLayers());
		getLayers().clear();
		for (int i = 0; i < backup.size(); i++) {
			char[][] l = backup.get(i);
			char[][] newTiles = new char[height][width];
			for (int y = 0; y < (l.length > height ? height : l.length); y++) {
				for (int x = 0; x < (l[0].length > width ? width : l[0].length); x++) {
					newTiles[y][x] = l[y][x];
				}
			}
			if (i == 0)
				tiles = newTiles;
			getLayers().add(newTiles);
		}
		
		System.out.println("Resized map to: "+width+","+height);
	}
	
	/**
	 * Adds a tile type
	 * @param symbol char Tile symbol
	 * @param name String Image name
	 * @param walkable boolean Whether or not the tile can be walked on
	 */
	public void addTileType(char symbol, String name, boolean walkable) {
		if (!tileTypes.containsKey(symbol))
			tileTypes.put(symbol, name);
		if (walkable) 
			this.walkable += symbol+"";
	}
	
	/**
	 * Sets the origin of the map
	 * @param x float Origin x
	 * @param y float Origin y
	 */
	public void setOrigin(float x, float y) {
		origin.set(x, y);
	}
	
	/**
	 * Returns the character of tile with specified image name
	 * @param name String To check
	 * @return char Tile type
	 */
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
	
	/**
	 * Gets the image name of a tile char
	 * @param c char To check
	 * @return String Image name
	 */
	public String getTileName(char c) {
		return tileTypes.get(c);
	}
	
	/**
	 * Returns the Entity Manager
	 * @return EntityManager
	 */
	public EntityManager getEntityManager() {
		return eM;
	}
	
	/**
	 * Returns the tileTypes map 
	 * @return HashMap<Character, String> tileTypes map
	 */
	public HashMap<Character, String> getTileTypes() {
		return tileTypes;
	}
	
	/**
	 * Returns the initial entities
	 * @return ArrayList<Entity> initial entities
	 */
	public ArrayList<Entity> getInitialEntities() {
		return initialEntities;
	}
	
	/**
	 * Checks if tileTypes contains this String
	 * @param name String Image name
	 * @return boolean Whether or not it exists
	 */
	public boolean hasTileType(String name) {
		return tileTypes.containsValue(name);
	}
	
	/**
	 * Checks if tileTypes contains specified char
	 * @param c char To check
	 * @return boolean Whether or not it exists
	 */
	public boolean isTileTypeTaken(char c) {
		return tileTypes.containsKey(c);
	}

	/**
	 * Returns the height of the map
	 * @return int Height in tiles
	 */
	public int getHeightInTiles() {
		return height;
	}

	/**
	 * Returns the width of the map
	 * @return int Width in tiles
	 */
	public int getWidthInTiles() {
		return width;
	}
	
	/**
	 * Returns the tile location relative origin based on specified vector
	 * @param v Vector2f To use
	 * @return Vector2f Result
	 */
	public Vector2f getTileLocationRelativeToOrigin(Vector2f v) {
		float x = v.x+(origin.x*16-8);
		float y = v.y+(origin.y*16-8);
		return new Vector2f(x,y);
	}
	
	/**
	 * Returns the origin
	 * @return Vector2f Origin
	 */
	public Vector2f getOrigin() {
		return origin;
	}
	
	/**
	 * Returns the spawn point
	 * @return Vector2f Spawn point
	 */
	public Vector2f getSpawnPoint() {
		return new Vector2f(((spawnTile.x+origin.x)*16)-8, ((spawnTile.y+origin.y)*16)-8);
	}
	
	/**
	 * Returns the map center
	 * @return Vector2f Map center
	 */
	public Vector2f getCenter() {
		return new Vector2f(((width/2)*16+origin.x*16)+8, ((height/2)*16+origin.y*16)+8);
	}

	/**
	 * Returns the list of layers
	 * @return ArrayList<char[][]> List of layers
	 */
	public ArrayList<char[][]> getLayers() {
		return layers;
	}

}
