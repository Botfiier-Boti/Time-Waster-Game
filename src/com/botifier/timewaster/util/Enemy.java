package com.botifier.timewaster.util;

import java.util.ArrayList;
import java.util.LinkedList;

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

public class Enemy extends Entity {
	protected Animation aIdle;
	protected Animation aWalk;
	protected Animation aAttack;
	protected Image current;
	SpriteSheet walk;
	protected SpriteSheet attack;
	protected long activateDelay = 0;
	protected boolean attacking = false;	
	protected boolean autoPlayAttack = true;
	public boolean autoFlip = true;
	public ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
	public ArrayList<BulletPattern> patterns = new ArrayList<BulletPattern>();
	public int currentPattern = -1;
	public int currentBehavior = -1;
	public Entity cls = null;
	
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
		team = Team.ENEMY;
	}
	
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
		team = Team.ENEMY;
	}
	
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
		team = Team.ENEMY;
	}
	
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
		team = Team.ENEMY;
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
		if (cls != null && cls.getLocation().distance(getLocation()) > influence)
			cls = null;
	}

	@Override
	public void draw(Graphics g) {
		if (visible == true) {
			if (current != null) {
				Vector2f iLoc = getLocation().copy();
				iLoc.y -= posMod.y;
				iLoc.x -= posMod.x;
				Image i = current.getScaledCopy(size).getFlippedCopy(dir, flip);
				i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
				i.setRotation(rotation);
				g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()-i.getHeight());
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
				g.drawLine(getLocation().getX(), getLocation().getY(), getLocation().getX()+((float)Math.cos(angle)*5), getLocation().getY()+((float)Math.sin(angle)*5));
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
	

	
	public boolean shootBullet(float x, float y, boolean force) throws SlickException {
		if (cooldown <= 0 || force) {
			BulletPattern bp = patterns.get(currentPattern);
			bp.fire(this, x, y, angle, null);
			float SPS = (1.5f + 6.5f*((getDexterity())/75f))*bp.fireSpeed;
			cooldown = 60/SPS;
			if (autoPlayAttack) 
				playAttackAnimation(60/SPS);
			return true;
		}
		return false;
	}
	
	public boolean shootBullet(float angle, Entity e, boolean force) throws SlickException {
		if (cooldown <= 0 || force) {
			BulletPattern bp = patterns.get(currentPattern);
			bp.fire(this, getLocation().x, getLocation().y, angle, e);
			float SPS = (1.5f + 6.5f*((getDexterity())/75f))*bp.fireSpeed;
			cooldown = 60/SPS;
			if (autoPlayAttack) 
				playAttackAnimation(60/SPS);
			return true;
		}
		return false;
	}
	
	public void playAttackAnimation(float speed) {
		if (attack == null)
			return;
		attacking = true;
		for (int i = 0; i < aAttack.getFrameCount(); i++) {
			aAttack.setDuration(i, (int)(10*speed/aAttack.getFrameCount()));
		}
		aAttack.restart();
		
	}
	
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

	@Override
	public EnemyController getController() {
		return (EnemyController) super.getController();
	}
}
