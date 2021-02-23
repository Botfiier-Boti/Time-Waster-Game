package com.botifier.timewaster.util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Math2 {
	//TODO: find a way to do this with different outputs without copy/pasting
	public static float greatestNumber(float x, float y) {
		return x > y ? x : y > x ? y : x;
	}
	public static float calcAngle(Vector2f src2,Vector2f dst2) {
		return (float)Math.atan2(dst2.getY()-src2.getY(),dst2.getX()-src2.getX());
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
	
	
	public static float round(float f, int dec) {
		String d = "#";
		if (dec > 0) {
			d += ".";
		}
		for (int i = 0; i < dec; i++) {
			d += "#";
		}
		try {
			DecimalFormat df = new DecimalFormat(d);
			return Float.valueOf(df.format(f));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
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
	
	public static ArrayList<Vector2f> getIntersected(Shape s1, Shape s2) {
	
		return null;
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
