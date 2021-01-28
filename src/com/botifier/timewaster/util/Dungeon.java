package com.botifier.timewaster.util;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;

public class Dungeon {
	public ArrayList<Room> hm = new ArrayList<Room>();
	public char[][] tiles;
	Random r = new Random();
	Polygon d;
	int num_rooms = 10;
	int min_size = 4;
	int max_size = 8;
	public Room spawnRoom;
	
	public Dungeon(int num_rooms, int min_size, int max_size) {
		this.num_rooms = num_rooms;
		this.min_size = min_size;
		this.max_size = max_size;
		tiles = new char[MainGame.mm.m.getWidthInTiles()][MainGame.mm.m.getHeightInTiles()];
		generate();
	}
	
	public Dungeon(int num_rooms, int min_size, int max_size, int seed) {
		this.num_rooms = num_rooms;
		this.min_size = min_size;
		this.max_size = max_size;
		tiles = new char[MainGame.mm.m.getWidthInTiles()][MainGame.mm.m.getHeightInTiles()];
		r.setSeed(seed);
		generate();
	}
	
	public Dungeon() {
		tiles = new char[MainGame.mm.m.getWidthInTiles()][MainGame.mm.m.getHeightInTiles()];
		generate();
	}
	
	public void generate() {
		hm.clear();
		Rectangle l = null;
		for (int i = 0; i < num_rooms; i++) {
			int size = min_size;
			if (max_size > min_size)
				size += r.nextInt(max_size-min_size);
			int x = 1+r.nextInt(MainGame.mm.m.getWidthInTiles()-size-1);
			int y = 1+r.nextInt(MainGame.mm.m.getHeightInTiles()-size-1);
			System.out.println(x+", "+y);
			Room ro = new Room(new Vector2f(x,y),size);
			boolean failed = false;
			for (Room re : hm) {
				if (re.r.intersects(ro.r)) {
					failed = true;
					i--;
					break;
				}
			}
			if (failed) {
				continue;
			}
			if (l != null) {
				hCorri((int)ro.getSpawn().x/16,(int)l.getCenterX()/16,(int)ro.getSpawn().y/16);
				vCorri((int)ro.getSpawn().y/16,(int)l.getCenterY()/16,(int)ro.getSpawn().x/16);
			}
			hm.add(ro);
			l = ro.r;
			if (spawnRoom == null) {
				spawnRoom = ro;
			}
		}
		for (Room r : hm) {
			for (int y = 0; y < r.size; y++) {
				for (int x = 0; x < r.size; x++) {
					tiles[(int) (r.position.y+y)][(int) (r.position.x+x)] = r.tiles[y][x];
				}
			}
		}
		MainGame.mm.m.tiles = tiles;
	}
	
	private void hCorri(int x1, int x2, int y) {
		for (int x = Math.min(x1, x2); x < Math.max(x1, x2); x++) {
			tiles[y][x] = '#';
		}
	}
	
	private void vCorri(int y1, int y2, int x) {
		for (int y = Math.min(y1,y2); y < Math.max(y1, y2); y++) {
			tiles[y][x] = '#';
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.drawString("Rooms:"+hm.size(), 1, 1);
		if (MainGame.debug)
		for (Room r : hm) {
			g.draw(r.r);
		}
	}
	
}
