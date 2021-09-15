package com.botifier.timewaster.util;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.bulletpatterns.BulletPattern;
import com.botifier.timewaster.util.managers.StatusEffectManager;
import com.botifier.timewaster.util.movements.EnemyController;

/**
 * Enemy class
 * @author Botifier
 *
 */
public class Enemy extends Entity {
	/**
	 * Idle animation
	 */
	protected Animation aIdle;
	/**
	 * Walk animation
	 */
	protected Animation aWalk;
	/**
	 * Attack animation
	 */
	protected Animation aAttack;
	/**
	 * The image to be rendered
	 */
	protected Image current;
	/**
	 * Walk animation spritesheet
	 */
	protected SpriteSheet walk;
	/**
	 * Attack animation spritesheet
	 */
	protected SpriteSheet attack;
	/**
	 * Delay before the entity can do anything.
	 */
	protected long activateDelay = 0;
	/**
	 * Whether or not the entity is attacking
	 */
	protected boolean attacking = false;	
	/**
	 * Whether or not the attack animation is played automatically
	 */
	protected boolean autoPlayAttack = true;
	/**
	 * Whether or not the entity flips automatically
	 */
	public boolean autoFlip = true;
	/**
	 * The Entity's behaviors
	 */
	public ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
	/**
	 * The Entity's bullet patterns
	 */
	public ArrayList<BulletPattern> patterns = new ArrayList<BulletPattern>();
	/**
	 * The currently used bullet pattern 
	 * -1 for none
	 */
	public int currentPattern = -1;
	/**
	 * The currently used behavior
	 * -1 for none
	 */
	public int currentBehavior = -1;
	/**
	 * The Entity's current target
	 */
	public Entity cls = null;
	
	/**
	 * Enemy Constructor
	 * @param name String Name of the entity
	 * @param i Image Idle image of the entity.
	 * @param controller EnemyController Controls entity movement
	 * @param walk SpriteSheet Walk animation
	 * @param attack SpriteSheet Attack animation
	 */
	public Enemy(String name, Image i, EnemyController controller, SpriteSheet walk, SpriteSheet attack) {
		super(name, i, controller);
		this.walk = walk;
		this.attack = attack;
		if (walk != null) {
			aWalk = new Animation(walk, (int)(300-(Math.max(0.6f + 1.5f*(getSpeed()/75f), 1))));
			aWalk.setLooping(true);
			aWalk.start();
		}
		if (attack != null) {
			aAttack = new Animation(attack,(int)(200-2*(Math.max(5*(3.5f), 1))));
			aAttack.setLooping(false);
			aAttack.stop();
			//aAttack.start();
		}
		setTeam(Team.ENEMY);
	}
	
	/**
	 * Enemy constructor with influence modifier
	 * @param name String Name of the entity
	 * @param i Image Idle image of the entity.
	 * @param controller EnemyController Controls entity movement
	 * @param walk SpriteSheet Walk animation
	 * @param attack SpriteSheet Attack animation
	 * @param imod float Influence multiplier 
	 */
	public Enemy(String name, Image i, EnemyController controller, SpriteSheet walk, SpriteSheet attack, float imod) {
		super(name, i, controller, imod);
		this.walk = walk;
		this.attack = attack;
		if (walk != null) {
			aWalk = new Animation(walk, (int)(300-(Math.max(0.6f + 1.5f*(getSpeed()/75f), 1))));
			aWalk.setLooping(true);
			aWalk.start();
		}
		if (attack != null) {
			aAttack = new Animation(attack,(int)(200-(Math.max(60/3.5f, 1))));
			aAttack.setLooping(false);
			aAttack.stop();
			//aAttack.start();
		}
		setTeam(Team.ENEMY);
	}
	
	/**
	 * Enemy constructor with influence modifier and idle animation
	 * @param name String Name of the entity
	 * @param i Image Idle image of the entity.
	 * @param controller EnemyController Controls entity movement
	 * @param walk SpriteSheet Walk animation
	 * @param attack SpriteSheet Attack animation
	 * @param idle SpriteSheet Idle animation
	 * @param idleSpeed int Speed of the idle animation
	 * @param imod float Influence multiplier 
	 */
	public Enemy(String name, Image i, EnemyController controller, SpriteSheet walk, SpriteSheet attack, SpriteSheet idle, int idleSpeed, float imod) {
		super(name, i, controller, imod);
		this.walk = walk;
		this.attack = attack;
		if (walk != null) {
			aWalk = new Animation(walk, (int)(300-(Math.max(0.6f + 1.5f*(getSpeed()/75f), 1))));
			aWalk.setLooping(true);
			aWalk.start();
		}
		if (attack != null) {
			aAttack = new Animation(attack,(int)(200-(Math.max(60/3.5f, 1))));
			aAttack.setLooping(false);
			aAttack.stop();
			//aAttack.start();
		}
		if (idle != null) {
			aIdle = new Animation(idle, idleSpeed);
			aIdle.setLooping(true);
			//aIdle.start();
		}
		setTeam(Team.ENEMY);
	}
	
	/**
	 * Clone enemy constructor
	 * @param e Enemy To clone
	 */
	public Enemy(Enemy e) {
		super(e);
		this.walk = e.walk;
		this.attack = e.attack;
		if (walk != null) {
			aWalk = new Animation(walk, (int)(300-(Math.max(0.6f + 1.5f*(getSpeed()/75f), 1))));
			aWalk.setLooping(true);
			aWalk.start();
		}
		if (attack != null) {
			aAttack = new Animation(attack,(int)(200-(Math.max(60/3.5f, 1))));
			aAttack.setLooping(false);
			aAttack.stop();
			//aAttack.start();
		}
		setTeam(Team.ENEMY);
	}
	
	@Override
	public void init() {
		super.init();
		current = image;
		sem = new StatusEffectManager(this);
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (activateDelay > 0) {
			if (active == true)
				active = false;
			activateDelay -= delta;
			if (activateDelay <= 0)
				active = true;
			return;
		}
		if (behaviors.size() > 0) {
			if (currentBehavior >= 0  && currentBehavior < behaviors.size()) {
				behaviors.get(currentBehavior).run();
			}
		}
		if (aWalk != null && getController().isMoving()) {
			aWalk.update(delta);
			current = aWalk.getCurrentFrame();
		} 
		if (aAttack != null && attacking) {
			if (aAttack.isStopped() == true) {
				attacking = false;
				aAttack.restart();
				aAttack.stop();
			}
			
			aAttack.update(delta);
			aAttack.stopAt(aAttack.getFrameCount()-1);
			current = aAttack.getCurrentFrame();
		}
		
		if (attacking == false && getController().isMoving() == false) {
			if (aIdle == null)
				current = image;
			else {
				aIdle.update(delta);
				current = aIdle.getCurrentFrame();
			}
		}
		
		if (getController().dst != null && getController().isMoving() && autoFlip == true) {
			float angle = (float) Math.toDegrees(getController().getAngle());
			if ((angle < -90 || angle > 90))  {
				dir = true;
			} else {
				dir = false;
			}
		}
		if (cls != null && cls.getLocation().distance(getLocation()) > getInfluence())
			cls = null;
	}

	@Override
	public void draw(Graphics g) {
		if (visible == true) {
			if (current != null) {
				Vector2f iLoc = getLocation().copy();
				iLoc.y -= posMod.y;
				iLoc.x -= posMod.x;
				Image i = current.getFlippedCopy(dir, flip);
				i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
				i.setRotation(rotation);
				
				i.draw(iLoc.getX()-(i.getWidth()*getSize())/2, iLoc.getY()-i.getHeight()*getSize(), i.getWidth()*getSize(), i.getHeight()*getSize());
				
				/*if (attacking == true && aAttack != null) {
					Image i = aAttack.getCurrentFrame().getScaledCopy(size).getFlippedCopy(dir, flip);
					Vector2f iLoc = getLocation().copy();
					iLoc.y -= posMod.y;
					iLoc.x -= posMod.x;
					i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
					i.setRotation(rotation);
					g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()+-i.getHeight());
				} else if (getController().isMoving() == true && aWalk != null) {
					Image i = aWalk.getCurrentFrame().getScaledCopy(size).getFlippedCopy(dir, flip);
					Vector2f iLoc = getLocation().copy();
					iLoc.y -= posMod.y;
					iLoc.x -= posMod.x;
					i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
					i.setRotation(rotation);
					g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()-i.getHeight());
				} else {
					Vector2f iLoc = getLocation().copy();
					iLoc.y -= posMod.y;
					iLoc.x -= posMod.x;
					Image i = image.getScaledCopy(size).getFlippedCopy(dir, flip);
					i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
					i.setRotation(rotation);
					g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()-i.getHeight());
				}*/
			}
			else
				g.drawString(getName().substring(0, 1), getLocation().getX(), getLocation().getY());
			
			if (MainGame.displayHitboxes) {
				g.drawLine(getLocation().getX(), getLocation().getY(), getLocation().getX()+((float)Math.cos(getAngle())*5), getLocation().getY()+((float)Math.sin(getAngle())*5));
				g.draw(hitbox);
				if (getController().testBox != null)
					g.draw(getController().testBox);
				if (getController().dst != null) {
					g.drawLine(getLocation().getX(), getLocation().getY(), getController().getDst().getX(), getController().getDst().getY());
				}
					
			}
		}
	}
	
	@Override
	public boolean shootBullet(float angle, boolean force) throws SlickException {
		if (currentPattern < 0 || currentPattern >= patterns.size()) {
			return super.shootBullet(angle, force);
		}
		if (cooldown <= 0 || force) {
			BulletPattern bp = patterns.get(currentPattern);
			float SPS = (1.5f + 6.5f*((getDexterity())/75f))*bp.fireSpeed;
			if (autoPlayAttack) 
				playAttackAnimation(60/SPS);
			bp.fire(this, getLocation().x, getLocation().y, angle, null);
			cooldown = 1000/SPS;
			return true;
		}
		return false;
	}
	

	/**
	 * Shoot bullet from position
	 * @param x float 
	 * @param y float 
	 * @param force boolean Whether or not it cares about cooldown
	 * @return boolean Whether or not the shot was fired
	 * @throws SlickException
	 */
	public boolean shootBullet(float x, float y, boolean force) throws SlickException {
		if (cooldown <= 0 || force) {
			BulletPattern bp = patterns.get(currentPattern);
			bp.fire(this, x, y, getAngle(), null);
			float SPS = (1.5f + 6.5f*((getDexterity())/75f))*bp.fireSpeed;
			cooldown = 1000/SPS;
			if (autoPlayAttack) 
				playAttackAnimation(60/SPS);
			return true;
		}
		return false;
	}
	
	/**
	 * Shoot bullet targeting entity
	 * @param angle float angle at which the bullet is fired.
	 * @param e Entity Target entity
	 * @param force boolean Whether or not it cares about cooldown
	 * @return boolean Whether or not the shot was fired.
	 * @throws SlickException
	 */
	public boolean shootBullet(float angle, Entity e, boolean force) throws SlickException {
		if (cooldown <= 0 || force) {
			BulletPattern bp = patterns.get(currentPattern);
			bp.fire(this, getLocation().x, getLocation().y, angle, e);
			float SPS = (1.5f + 6.5f*((getDexterity())/75f))*bp.fireSpeed;
			cooldown = 1000/SPS;
			if (autoPlayAttack) 
				playAttackAnimation(60/SPS);
			return true;
		}
		return false;
	}
	
	/**
	 * Plays attack animation
	 * @param speed float Speed in which animation is played
	 */
	public void playAttackAnimation(float speed) {
		if (attack == null)
			return;
		attacking = true;
		for (int i = 0; i < aAttack.getFrameCount(); i++) {
			aAttack.setDuration(i, (int)(10*speed/aAttack.getFrameCount()));
		}
		aAttack.restart();
		
	}
	
	/**
	 * Add a behavior if it isn't already there
	 * @param b Behavior To add
	 */
	public void addBehavior(Behavior b) {
		boolean has = false;
		for (int i = behaviors.size()-1; i >= 0; i--) {
			Behavior be = behaviors.get(i);
			if (be == null)
				continue;
			if (be.getClass() ==  b.getClass()) {
				has = true;
				break;
			}
		}
		if (has == false)
			behaviors.add(b);
	}
	
	/*public Enemy copy() {
		Enemy e = (Enemy) super.copy();
		e.behaviors = behaviors;
		e.currentBehavior = currentBehavior;
		e.autoFlip = autoFlip;
		return e;
	}*/

	/**
	 * Sets the current used image
	 * @param i Image To use
	 */
	public void setCurrentImage(Image i) {
		current = i;
	}
	
	@Override
	public EnemyController getController() {
		return (EnemyController) super.getController();
	}
}
