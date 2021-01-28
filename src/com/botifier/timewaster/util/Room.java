package com.botifier.timewaster.util;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Room {
	ArrayList<Entity> p = new ArrayList<Entity>();
	public int size;
	public Vector2f position;
	public char[][] tiles;
	public Rectangle r;
	
	public Room(Vector2f pos, int size) {
		this.position = pos;
		this.size = size;
		init();
	}
	
	public void init() {
		tiles = new char[this.size][this.size];
		generateWalls();
		r = new Rectangle(0, 0, size*16, size*16);
		r.setLocation(position.getX()*16, position.getY()*16);
	}
	
	public Vector2f getSpawn() {
		return new Vector2f(r.getCenterX(),r.getCenterY());
	}
	
	public void generateWalls() {
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				tiles[y][x] = '#';
			}
		}
	}

}
