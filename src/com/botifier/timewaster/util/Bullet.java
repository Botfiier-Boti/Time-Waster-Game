package com.botifier.timewaster.util;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.statuseffect.StatusEffect;
import com.botifier.timewaster.util.movements.BulletController;
import com.botifier.timewaster.util.movements.EntityController;

/**
 * The Bullet class
 * @author Botifier
 *
 */
public class Bullet extends Entity {
	/**
	 * The placeholder image for bullets
	 */
	public static Image placehold = null;
	/**
	 * The base damages of this bullet
	 * array of size 2 that contains mindamage and maxdamage
	 */
	private int[] basedamage;
	/**
	 * The status effect that this bullet afflicts
	 */
	private StatusEffect effect = null;
	/**
	 * Whether or not the bullet obeys DEF
	 */
	private boolean ignoresArmor = false;
	/**
	 * Whether or not bullet damage scales with ATK
	 */
	private boolean atkScaling = true;
	/**
	 * The shot spread of the bullet
	 */
	private float spread = 0;
	/**
	 * Tracks which shot this bullet was when fired
	 * Used for movement
	 */
	private long shot = 0;
	/**
	 * Tracks how many other shots were associated with this bullet
	 */
	private int shots = 0;
	
	/**
	 * Bullet Constructor
	 * @param name String The Name of the Bullet
	 * @param x float
	 * @param y float
	 * @param speed float The speed in which this bullet moves
	 * @param angle float The angle in radians.
	 * @param lifeTime long How long this bullet lasts
	 * @param minDmg int The minimum damage used in the damage formula
	 * @param maxDmg int The maximum damage used in the damage formula
	 * @param origin Entity Where This bullet originated from
	 * @throws SlickException
	 */
	public Bullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin) throws SlickException {
		super(name, MainGame.getImage("DefaultShot"), new BulletController(x, y, speed, lifeTime, angle, origin),0);
		rotation = (float) Math.toDegrees(angle);
		setBaseDamage(new int[] {minDmg, maxDmg});
		wOverride = 6;
		hOverride = 6;
		healthbarVisible = false;
		hasshadow = false;
	}
	
	/**
	 * Bullet Constructor
	 * @param name String The Name of the Bullet
	 * @param x float
	 * @param y float
	 * @param speed float The speed in which this bullet moves
	 * @param angle float The angle in radians.
	 * @param lifeTime long How long this bullet lasts
	 * @param minDmg int The minimum damage used in the damage formula
	 * @param maxDmg int The maximum damage used in the damage formula
	 * @param origin Entity Where This bullet originated from
	 * @param pierceObstacles boolean Whether or not this bullet pierces obstacles
	 * @param pierceEnemies boolean Whether or not this bullet pierces enemies
	 * @param boomerang boolean Whether or not this bullet boomerangs
	 * @throws SlickException
	 */
	public Bullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin, boolean pierceObstacles, boolean pierceEnemies, boolean boomerang) throws SlickException {
		super(name, MainGame.getImage("DefaultShot"), new BulletController(x, y, speed, lifeTime, angle, origin, pierceObstacles, pierceEnemies, boomerang),0);
		rotation = (float) Math.toDegrees(angle);
		setBaseDamage(new int[] {minDmg, maxDmg});
		wOverride = 6;
		hOverride = 6;
		healthbarVisible = false;
		hasshadow = false;
	}
	
	/**
	 * Bullet Constructor
	 * @param i Image
	 * @param name String The Name of the Bullet
	 * @param x float
	 * @param y float
	 * @param speed float The speed in which this bullet moves
	 * @param angle float The angle in radians.
	 * @param lifeTime long How long this bullet lasts
	 * @param minDmg int The minimum damage used in the damage formula
	 * @param maxDmg int The maximum damage used in the damage formula
	 * @param origin Entity Where This bullet originated from
	 * @param pierceObstacles boolean Whether or not this bullet pierces obstacles
	 * @param pierceEnemies boolean Whether or not this bullet pierces enemies
	 * @param boomerang boolean Whether or not this bullet boomerangs
	 * @throws SlickException
	 */
	public Bullet(Image i, String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin, boolean pierceObstacles, boolean pierceEnemies, boolean boomerang) throws SlickException {
		super(name, i, new BulletController(x, y, speed, lifeTime, angle, origin, pierceObstacles, pierceEnemies, boomerang),0);
		rotation = (float) Math.toDegrees(angle);
		setBaseDamage(new int[] {minDmg, maxDmg});
		wOverride = 6;
		hOverride = 6;
		healthbarVisible = false;
		hasshadow = false;
	}

	/**
	 * Bullet Constructor
	 * @param name String
	 * @param i Image
	 * @param controller EntityController
	 */
	public Bullet(String name, Image i, EntityController controller) {
		super(name,i,controller);
		rotation = 0;
		setBaseDamage(new int[] {0, 0});
		healthbarVisible = false;
		hasshadow = false;
	}

	@Override
	public void update(int delta) throws SlickException {
		setAngle(getController().visualAngle);
		if (!overrideMove && getController() != null)
			getController().move(delta);
		if (getController().getOrigin() != null && getController().getOrigin().getTeam() != getTeam())
			setTeam(getController().getOrigin().getTeam());
		if (hitbox != null) {
			hitbox.setCenterX(getLocation().getX());
			hitbox.setY(getLocation().getY()-posMod.y-(hitbox.getHeight()));	
		}
		if (collisionbox != null) {
			collisionbox.setCenterX(getLocation().getX());
			collisionbox.setY(getLocation().getY()-posMod.y-(collisionbox.getHeight()));	
		}
		rotation = (float) Math.toDegrees(getController().visualAngle);
		if (getController().isMoving() == false) {
			onDestroy();
			destroy = true;
			return;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (visible == true && active == true) {
			//Checks if the image is null
			if (image != null) {
				//Modifies the image to match parameters
				Image y = null;
				if ((getSize() > 1 || getSize() < 1) && getSize() != lSize) {
					y = image.getScaledCopy(getSize());
					lSize = getSize();
				}
				else 
					y = image;
				Vector2f iLoc = getLocation().copy();
				y.setCenterOfRotation(y.getWidth()/2, y.getHeight()/2);
				y.setRotation(rotation);
				y.draw(iLoc.x- (y.getWidth()/2), iLoc.y - (y.getHeight()/2));
			}
			else
				//Draws a letter at Entity's location
				g.drawString(getName().substring(0, 1),getLocation().getX(), getLocation().getY());
		}
		//Renders hitboxs if enabled
		if (MainGame.displayHitboxes) {
			g.setColor(Color.blue);
			if (hitbox != null)
				g.draw(hitbox);
			if (collisionbox != null)
				g.draw(collisionbox);
			g.setColor(Color.red);
			if (getController().testBox != null)
				g.draw(getController().testBox);
			g.setColor(Color.white);
		}
	}
	
	@Override
	public void addBullet(Entity e) {
		getOwner().addBullet(e);
	}
	
	@Override
	public BulletController getController() {
		return ((BulletController)super.getController());
	}
	
	@Override
	public void init() {//Gives the entity a random UUID
		wOverride = 1;
		hOverride = 1;
		//Creates a hitbox
		hitbox = new Rectangle(getLocation().getX(), getLocation().getY(), wOverride, hOverride);
		//Creates a collisonbox
		collisionbox = new Rectangle(getLocation().getX(), getLocation().getY(), wOverride, hOverride);
		//Indicates that the bullet is initialized
		initialized = true;
	}
	
	/**
	 * Creates a custom bullet
	 * @param name String The name of the bullet
	 * @param x float
	 * @param y float
	 * @param speed float The speed in which this bullet moves
	 * @param angle float The angle in radians
	 * @param lifeTime long How long this bullet lasts
	 * @param minDmg int The minimum damage used in the damage formula
	 * @param maxDmg int The maximum damage used in the damage formula
	 * @param origin Entity the owner of this bullet
	 * @param pierceObstacles boolean Whether or not the bullet pierces obstacles
	 * @param pierceEnemies boolean Whether or not the bullet pierces enemies
	 * @param pierceArmor boolean Whether or not the bullet obeys DEF
	 * @param boomerang boolean Whether or not the bullet boomerangs
	 * @param hasShadow boolean Whether or not the bullet renders a shadow
	 * @param atkScaling boolean Whether or not bullet damage will scale with ATK
	 * @param shotIt int Shot iterator
	 * @param shots int Shots that appeared along with this bullet
	 * @param spread float The bullet spread
	 * @return Bullet The created bullet
	 * @throws SlickException
	 */
	public static Bullet createBullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin, boolean pierceObstacles, boolean pierceEnemies, boolean pierceArmor, boolean boomerang, boolean hasShadow, boolean atkScaling, long shotIt, int shots, float spread) throws SlickException {
		Bullet b = new Bullet(MainGame.getImage("DefaultShot"), name, x, y, speed, angle, lifeTime, minDmg, maxDmg, origin, pierceObstacles, pierceEnemies, boomerang);
		b.hasshadow = hasShadow;
		b.atkScaling = atkScaling;
		b.ignoresArmor = pierceArmor;
		b.spread = spread;
		b.shot = shotIt;
		b.shots = shots;
		b.setTeam(origin.getTeam());
		return b;
	}
	
	/**
	 * Creates bullet using less parameters
	 * @param name String The name of the bullet
	 * @param x float
	 * @param y float
	 * @param speed float The speed in which this bullet moves
	 * @param angle float The angle in radians
	 * @param lifeTime long How long this bullet lasts
	 * @param minDmg int The minimum damage used in the damage formula
	 * @param maxDmg int The maximum damage used in the damage formula
	 * @param origin Entity the owner of this bullet
	 * @return createBullet
	 * @throws SlickException
	 */
	public static Bullet createBullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin) throws SlickException {
		return createBullet(name, angle, angle, angle, angle, lifeTime, maxDmg, maxDmg, origin, false, false, false, false, false, true, 0, 0, 0);
	}
	
	/**
	 * Action taken when the bullet is destroyed
	 */
	public void onDestroy() {
		
	}
	
	/**
	 * Action taken when the bullet hits something
	 */
	public void onHit() {
		
	}
	
	/**
	 * Returns the creator of this bullet
	 * @return Entity
	 */
	public Entity getOrigin() {
		return getController().getOrigin();
	}
	
	/**
	 * Whether or not bullet damage scales with ATK
	 * @return boolean
	 */
	public boolean scalesWithAtk() {
		return atkScaling;
	}
	
	/**
	 * Whether or not the bullet damage obeys DEF
	 * @return boolean
	 */
	public boolean ignoresDefense() {
		return ignoresArmor();
	}

	/**
	 * Returns the base damage(s) of the bullet
	 * @return
	 */
	public int[] getBaseDamage() {
		return basedamage;
	}

	/**
	 * Returns the status effect that this bullet applies
	 * @return StatusEffect
	 */
	public StatusEffect getEffect() {
		return effect;
	}
	
	/**
	 * Returns the shot spread of this bullet
	 * @return float
	 */
	public float getShotSpread() {
		return spread;
	}

	/**
	 * Sets the base damages(s)
	 * @param is int[] mindamage and maxdamage
	 */
	private void setBaseDamage(int[] is) {
		basedamage = is;
	}

	/**
	 * Which shot this bullet was when it was fired
	 * @return long
	 */
	public long getShotIterator() {
		return shot;
	}

	/**
	 * How many other shots appeared with this bullet
	 * @return int
	 */
	public int getShots() {
		return shots;
	}

	/**
	 * Whether or not the bullet ignores armor
	 * @return boolean
	 */
	public boolean ignoresArmor() {
		return ignoresArmor;
	}

	/**
	 * Sets the effect of the bullet
	 * @param effect StatusEffect
	 */
	public void setEffect(StatusEffect effect) {
		this.effect = effect;
	}
}
