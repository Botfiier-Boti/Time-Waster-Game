package com.botifier.timewaster.util;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.movements.EnemyController;

public class Enemy extends Entity {
	protected Animation aWalk;
	protected Animation aAttack;
	SpriteSheet walk;
	SpriteSheet attack;
	protected float fireSpeed = 0.95f;
	protected long activateDelay = 0;
	protected boolean attacking = false;	
	public boolean autoFlip = true;
	public ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
	public int currentBehavior = -1;
	public Entity cls = null;
	
	public Enemy(String name, Image i, EnemyController controller, SpriteSheet walk, SpriteSheet attack) {
		super(name, i, controller);
		this.walk = walk;
		this.attack = attack;
		if (walk != null) {
			aWalk = new Animation(walk, (int)(300-(Math.max(0.6f + 1.5f*(getController().speed/75f), 1))));
			aWalk.setLooping(true);
			aWalk.start();
		}
		if (attack != null) {
			aAttack = new Animation(attack,(int)(200-2*(Math.max(5*(3.5f*fireSpeed), 1))));
			aAttack.setLooping(false);
			aAttack.start();
		}
		team = Team.ENEMY;
	}
	
	public Enemy(String name, Image i, EnemyController controller, SpriteSheet walk, SpriteSheet attack, float imod) {
		super(name, i, controller, imod);
		this.walk = walk;
		this.attack = attack;
		if (walk != null) {
			aWalk = new Animation(walk, (int)(300-(Math.max(0.6f + 1.5f*(getController().speed/75f), 1))));
			aWalk.setLooping(true);
			aWalk.start();
		}
		if (attack != null) {
			aAttack = new Animation(attack,(int)(200-(Math.max(60/3.5f*fireSpeed, 1))));
			aAttack.setLooping(false);
			aAttack.start();
		}
		team = Team.ENEMY;
	}
	
	@Override
	public void init() {
		super.init();
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
		if (aAttack != null && attacking) {
			if (aAttack.isStopped() == true)
				attacking = false;
			aAttack.update(delta);
			aAttack.stopAt(aAttack.getFrameCount()-1);
		}
		if (aWalk != null && getController().isMoving()) {
			aWalk.update(delta);
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
			if (image != null) {
				if (attacking == true && aAttack != null) {
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
				}
			}
			else
				g.drawString(getName().substring(0, 1), getController().src.getX(), getController().src.getY());
			
			if (MainGame.displayHitboxes) {
				g.drawLine(getLocation().getX(), getLocation().getY(), getLocation().getX()+((float)Math.cos(angle)*5), getLocation().getY()+((float)Math.sin(angle)*5));
				g.draw(hitbox);
				if (getController().testBox != null)
					g.draw(getController().testBox);
				if (getController().dst != null) {
					g.drawLine(getController().src.getX(), getController().src.getY(), getController().dst.getX(), getController().dst.getY());
				}
					
			}
			
		}
	}
	
	public void playAttackAnimation(int speed) {
		if (attack == null)
			return;
		attacking = true;
		for (int i = 0; i < aAttack.getFrameCount(); i++) {
			aAttack.setDuration(i, 10*(int)Math.max(speed, 1)/aAttack.getFrameCount());
		}
		aAttack.restart();
		
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
