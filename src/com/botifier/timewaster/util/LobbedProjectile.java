package com.botifier.timewaster.util;


import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.movements.LobbedController;

/**
 * Generic Lobbed projectile class
 * @author Botifier
 *
 */
public abstract class LobbedProjectile extends Entity {
	/**
	 * How much the projectile image rotates
	 */
	private float rotate = 5f;
	/**
	 * The maxmimum height that the projectile reaches
	 */
	protected float maxHeight = 50;
	/**
	 * Curve for the projectile to follow visually
	 */
	private Curve c;
	/**
	 * The origin postion of the projectile
	 */
	private Vector2f start;
	
	/*Old unused variables
	private float cThrow = 0;
	private long time = 0;
	private long lifeTime = 0;*/
	
	/**
	 * Unused multiplier
	 */
	protected float mult = 1f;
	
	/**
	 * Lobbed projectile constructor
	 * @param name String Name of the Entity
	 * @param i Image Projectile Image
	 * @param controller LobbedController The entity controller
	 * @param dst Vector2f The projectile's destination
	 * @param o Entity The projectile's owner
	 * @param heightMod float The maximum height that the projectile can reach
	 */
	public LobbedProjectile(String name, Image i, LobbedController controller, Vector2f dst, Entity o, float heightMod) {
		super(name, i, controller);
		this.o = o;
		controller.setCollision(false);
		controller.dst = dst.copy();
		setMaxHealth(1,false);
		healthbarVisible = false;
		invulnerable = true;
		start = controller.getLoc().copy();
		//tdist = (int) controller.getLoc().distance(dst);	
		Vector2f p1 = start.copy();
		p1.x += (start.x-dst.x)/controller.getDuration()*0.25f;
		p1.y += (start.y-dst.y)/controller.getDuration()*0.25f-heightMod;
		Vector2f p2 = dst.copy();
		p2.x += (start.x-dst.x)/controller.getDuration()*0.75f;
		p2.y += (start.y-dst.y)/controller.getDuration()*0.75f-heightMod;
		c = new Curve(start, p1, p2, controller.dst, (int) controller.getDuration());
		maxHeight = heightMod;
		if (o.team != this.team)
			this.team = o.team;
	}

	/**
	 * Action taken when the projectile lands
	 * @throws SlickException
	 */
	public abstract void onLand() throws SlickException;
	
	/**
	 * Action taken when the projectile is destroyed
	 */
	@Override
	public void onDeath() throws SlickException {
		super.onDeath();
		onLand();
	}
	
	/**
	 * Updates the projectile rotating it and modifying the visual position based on the curve 
	 */
	@Override
	public void update(int delta) throws SlickException {
		getController().move(delta);
		rotation+=getRotation();
		if (rotation > 360)
			rotation = rotation-360;
		if (getController().getDst() != null) {
			posMod.y = getLocation().y-c.pointAt((float)getController().getTimeElapsed()/getController().getDuration()).y;
			if (posMod.y < 0)
				posMod.y = 0;
			/*if (getController().getTimeLeft() > getController().getDuration()/2) {
				posMod.y -= 1;
			} else {
				posMod.y += 1;
			}*/
		}
	}
	
	/**
	 * Draws the projectile
	 */
	@Override
	public void draw(Graphics g) {
		if (image != null && getController().getDst() != null) {
			if (size != lSize) {
				image = image.getScaledCopy((int)(image.getWidth()*size), (int)(image.getHeight()*size));
				init();
				lSize = size;
			} 
			Image y = image.getFlippedCopy(dir, flip);
			Vector2f iLoc = getLocation().copy();
			iLoc.y -= posMod.y;
			y.setCenterOfRotation(y.getWidth()/2, y.getHeight()/2);
			y.setRotation(rotation);
			g.drawImage(y, iLoc.getX()-y.getWidth()/2, iLoc.getY()-y.getHeight());
		}
	}
	
	/**
	 * Overrides addBullet to add bullets to the owner instead of the projectile
	 */
	@Override
	public void addBullet(Entity e) {
		o.addBullet(e);
	}
	
	/**
	 * Returns the LobbedController
	 * @return LobbedController Entity controller
	 */
	@Override
	public LobbedController getController() {
		return (LobbedController)super.getController();
	}

	/**
	 * Returns the current rotation amount
	 * @return float Current rotation amount
	 */
	public float getRotation() {
		return rotate;
	}

	/**
	 * Sets the current rotation amount
	 * @param rotate float Rotation amount
	 */
	public void setRotation(float rotate) {
		this.rotate = rotate;
	}
}
