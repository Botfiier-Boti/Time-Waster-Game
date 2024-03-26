package com.botifier.timewaster.util;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Math functions that do not already exist in this form
 * @author Botifier
 *
 */
public class Math2 {
	//TODO: find a way to do this with different outputs without copy/pasting
	public static float greatestNumber(float x, float y) {
		return x > y ? x : y > x ? y : x;
	}
	public static float calcAngle(Vector2f src2,Vector2f dst2) {
		return (float)Math.atan2(dst2.getY()-src2.getY(), dst2.getX()-src2.getX());
	}
	
	public static float lowestNumber(float x, float y) {
		return x > y ? y : y > x ? x : x;
	}
	
	public static boolean greaterThan(float x, float y) {
		return Math.max(x, y) == x ? true : false;
	}
	
	public static boolean lessThan(float x, float y) {
		return Math.min(x,y) == x ? true : false;
	}
	
	public static float lerp(float a, float b, float c) {
		return a+c*(b-a);
	}
	
	public static float getCloser(float a, float b, float c) {
		return (Math.abs(c-a) < Math.abs(c-b)) ? a : b;
	}
	
	public static float round(float f, int dec) {
		return (((int)(f * (dec * 10)))/(dec * 10f));
	}
	
	public static Vector2f truncate(Vector2f a, float max) {
		Vector2f newV = a.copy();
		if (newV.length() > max) {
			newV.normalise();
			newV.x *= max;
			newV.y *= max;
		}
		return newV;
	}
	
	public static boolean rectContains(float x, float y, float width, float height, float px, float py) {
		float x1 = x+width;
		float y1 = y;
		float x2 = x;
		float y2 = y+height;
		return (px < x1 &&  px > x2 && py > y1 && py < y2) ? true : false;
	}
	
	public static boolean overlaps(Rectangle r, Rectangle r2, Vector2f move) {
		r.setCenterX(r.getX()+move.x);
		r.setCenterY(r.getY()+move.y);
		if (r.getMinX() == r2.getMinX() || r.getMaxX() == r2.getMaxX() || r.getMinY() == r2.getMinY() || r.getMaxY() == r2.getMaxY())
			return true;
		if (r.getMinX() <= r2.getMinX() || r2.getMaxX() <= r.getMaxX())
			return false;
		if (r.getMinY() <= r2.getMinY() || r2.getMaxY() <= r.getMaxY())
			return false;
		return true;
	}
	
	public static Vector2f rectDist(Rectangle r, Rectangle r2, Vector2f move) {
		Rectangle r3 = new Rectangle(r.getMinX()+move.x, r.getMinY()+move.y, r.getWidth(), r.getHeight());
		if (r3.intersects(r2)) {
			float minX = Math.min(r.getCenterX(), r3.getCenterX());
			float maxX = Math.max(r.getCenterX(), r3.getCenterX());
			float minY = Math.min(r.getCenterY(), r3.getCenterY());
			float maxY = Math.max(r.getCenterY(), r3.getCenterY());
			
			float dist1 = (float) Math.sqrt(Math.pow(maxX-minX, 2)+Math.pow(maxY-minY, 2));
			
			float minX2 = Math.min(r.getCenterX(), r2.getCenterX());
			float maxX2 = Math.max(r.getCenterX(), r2.getCenterX());
			float minY2 = Math.min(r.getCenterY(), r2.getCenterY());
			float maxY2 = Math.max(r.getCenterY(), r2.getCenterY());
			
			float dist2 = (float) Math.sqrt(Math.pow(maxX2-minX2, 2)+Math.pow(maxY2-minY2, 2));
			
			float dist3 = Math.max(dist2, dist1)-Math.min(dist2, dist1);
			
			Vector2f newMove = move.copy();
			newMove.x = newMove.x*(dist1/dist3 != 0 ? 0 : 1);
			newMove.y = newMove.y*(dist1/dist3 != 0 ? 0 : 1);
			return newMove;
		}
		return move;
	}
	
	public static float distance(Entity e, Entity e2) {
		return e.getLocation().distance(e2.getLocation());
	}
	
	public static ArrayList<Vector2f> getIntersected(Shape s1, Shape s2) {
	
		return null;
	}
	
	public static float lerpAngle(float a, float b, float weight) {
		return a + shortAngleDist(a,b) * weight;
	}
	
	public static float shortAngleDist(float a, float b) {
		float max = (float) (2*Math.PI);
		float dif = fmod(a-b, max);
		return fmod(2*dif, max)-dif;
	}
	

	public static float fmod(float a, float b) {
		int result = (int) Math.floor(a/b);
		return a-result*b;
	}
	
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);	
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);	
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean isLong(String s) {
		try {
			Long.parseLong(s);	
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
