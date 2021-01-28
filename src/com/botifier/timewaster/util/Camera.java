package com.botifier.timewaster.util;


import static org.newdawn.slick.Input.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;

public class Camera {
	
	public Entity centerE;
	
	public Vector2f center;
	
	public Rectangle r;
	
	public float cx, cy;
	
	float rotation = 0;
	
	public Camera(float width, float height) {
		r = new Rectangle(0,0,width+20,height+20);
		center = new Vector2f();
	}
	
	public void update(GameContainer gc) {
		if (centerE != null) {
			r.setCenterX(centerE.getLocation().x-10);
			r.setCenterY(centerE.getLocation().y-10);
		}
		Input i = gc.getInput();
		if (i.isKeyDown(KEY_Q)) {
			rotation+=1f;
		}
		if (i.isKeyDown(KEY_E)) {
			rotation-=1f;
		}
	}
	
	public void draw(GameContainer gc, Graphics g) {
		Input in = gc.getInput();
		g.setDrawMode(Graphics.MODE_NORMAL);
		in.setScale(1/MainGame.camRatio, 1/MainGame.camRatio);
		g.scale(MainGame.camRatio, MainGame.camRatio);
		if (centerE == null) {
			g.translate(-center.getX(), -center.getY());
		} else {
			cx = getWidth()/2 - centerE.getLocation().getX() - centerE.hitbox.getWidth()*1.20f;
			cy = getHeight()/2 - centerE.getLocation().getY() - centerE.hitbox.getHeight();
			in.setOffset(-cx, -cy);
			g.translate(cx, cy);
		}
		//g.setWorldClip(r);
	}
	
	public void setCenterEntity(Entity e) {
		this.centerE = e;
	}

	public float getWidth() {
		return r.getWidth();
	}
	
	public float getHeight() {
		return r.getHeight();
	}
	
}
