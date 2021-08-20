package com.botifier.timewaster.util;


import static org.newdawn.slick.Input.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;

/**
 * Camera class
 * @author Botifier
 *
 */
public class Camera {
	
	/**
	 * The Entity the camera is centered on
	 */
	private Entity centerE;
	
	/**
	 * The position that the camera is centered on
	 */
	private Vector2f center;
	
	/**
	 * Rectangle representing the camera view
	 */
	private Rectangle r;
	
	/**
	 * The zoom of the camera
	 */
	private float zoom = 1;
	
	/**
	 * The center position of the camera after modifications
	 */
	private float cx, cy;
	
	/**
	 * The rotation of the camera unused
	 */
	@SuppressWarnings("unused")
	private float rotation = 0;
	
	/**
	 * Camera constructor
	 * @param width float The camera's width
	 * @param height float The camera's height
	 */ 
	public Camera(float width, float height) {
		r = new Rectangle(0,0,width+20,height+20);
		setCenter(new Vector2f());
		zoom = MainGame.camRatio;
	}
	
	/**
	 * Updates the camera every tick
	 * @param gc GameContainer
	 */
	public void update(GameContainer gc) {
		//Modifies the rectangle if centerEntity is not null
		if (getCenterEntity() != null) {
			r.setCenterX(getCenterEntity().getLocation().x-10);
			r.setCenterY(getCenterEntity().getLocation().y-10);
		}
		//Obtains input
		Input i = gc.getInput();
		
		/*if (i.isKeyDown(KEY_K))
			zoom += 0.05f;
		if (i.isKeyDown(KEY_M))
			zoom -= 0.05f;*/
		
		//Caps zoom from 1/4 of camRatio to 2 times the camRatio
		if (zoom < MainGame.camRatio/4)
			zoom = MainGame.camRatio/4;
		if (zoom > MainGame.camRatio*2)
			zoom = MainGame.camRatio*2;
		
		//Rotates the camera
		if (i.isKeyDown(KEY_Q)) {
			rotation+=1f;
		}
		if (i.isKeyDown(KEY_E)) {
			rotation-=1f;
		}
	}
	
	/**
	 * Offsets and scales graphics
	 * @param gc GameContainer
	 * @param g Graphics
	 */
	public void draw(GameContainer gc, Graphics g) {
		Input in = gc.getInput();
		g.setDrawMode(Graphics.MODE_NORMAL);
		in.setScale(1/zoom, 1/zoom);
		g.scale(zoom, zoom);
		if (getCenterEntity() == null) {
			cx = (getWidth()/2) - center.getX();
			cy = (getHeight()/2) - center.getY();
			in.setOffset(-cx, -cy);
			g.translate(cx, cy);
			//g.translate(-center.getX(), -center.getY());
			//in.setOffset(center.getX(), center.getY());
			//if (MainGame.mm.getCurrentStateID() == 2)
			//	g.rotate(r.getCenterX(), r.getCenterY(), rotation);
		} else {
			center.set(getCenterEntity().hitbox.getCenterX(), getCenterEntity().hitbox.getMaxY());
			cx = (1/zoom*getWidth())*MainGame.windowScale - center.getX();
			cy = (1/zoom*getHeight())*MainGame.windowScale - center.getY();
			in.setOffset(-cx, -cy);
			g.translate(cx, cy);
			//if (MainGame.mm.getCurrentStateID() == 2)
			//	g.rotate(centerE.getLocation().getX(), centerE.getLocation().getY(), rotation);
		}
		//g.scale(zoom, zoom);
		//g.setWorldClip(r);
	}
	
	/**
	 * Sets the centered entity
	 * @param e Entity
	 */
	public void setCenterEntity(Entity e) {
		this.centerE = e;
	}
	
	/**
	 * Gets the center position of the camera
	 * @return
	 */
	public Vector2f getCenter() {
		if (getCenterEntity() != null)
			return getCenterEntity().getLocation();
		return center;
	}

	/**
	 * Returns the camera's width
	 * @return float Rectangle Width
	 */
	public float getWidth() {
		return r.getWidth();
	}
	
	/**
	 * Returns the camera's height
	 * @return float Rectangle Height
	 */
	public float getHeight() {
		return r.getHeight();
	}
	
	/**
	 * Gets the camera's current zoom
	 * @return float
	 */
	public float getZoom() {
		return zoom;
	}

	/**
	 * Sets the camera's current zoom
	 * @param zoom float
	 */
	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	
	/**
	 * Sets the center position of the camera
	 * @param center Vector2f
	 */
	public void setCenter(Vector2f center) {
		this.center = center;
	}

	/**
	 * Gets the entity that the camera is centered on
	 * @return Entity
	 */
	public Entity getCenterEntity() {
		return centerE;
	}
	
}
