package com.botifier.timewaster.entity.player;


import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.projectile.Beehive;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.states.OverworldState;
import com.botifier.timewaster.statuseffect.effects.InvulnerabilityEffect;
//import com.botifier.timewaster.entity.ShotgunPattern;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.Team;
import com.botifier.timewaster.util.bulletpatterns.*;
import com.botifier.timewaster.util.movements.LocalPlayerControl;

public class Player extends Entity {
	//Walk animation
	Animation walk;
	//Shot sound
	Sound s;
	//whether or not to use the lob functionality
	public boolean lob = false;
	//whether or not to use the build functionality
	public boolean build = false;
	//Autofire
	public boolean autofire = false;
	//Shots multiplier
	float SPS = 0;
	//Shot cooldown
	float cooldown = 0;
	//Period of safety
	public long invulPeriod = 0;
	//Current bullet pattern
	BulletPattern p;
	//Line
	Line l = new Line(0,0);
	//Targeted location
	Vector2f target = new Vector2f(0,0);
	//Whether the line is started during lob
	boolean started = false;
	
	public Player(String name, float x, float y) throws SlickException {
		super(name, MainGame.getImage("PlayerIdle"), new LocalPlayerControl(x, y));
		SpriteSheet ss = new SpriteSheet(MainGame.getImage("PlayerWalk"), 8, 8);
		walk = new Animation(ss, 100);
		walk.setLooping(true);
		walk.start();
		//s = MainGame.getSound("bladeswing");
		getController().setCollision(true);
		team = Team.ALLY;
		overrideMove = true;
		setMaxHealth(620, true);
		getStats().setAttack(50);
		getStats().setDefense(15);
		getStats().setVitality(30);
		getStats().setSpeed(50);
		spawncap = 4;
		//p = new WigglyThingPattern();
		//p = new SpherePattern();
		//p = new BeeHivePattern();
		//p = new PincerPattern();
		//build = true;
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		//Get input
		Input i = gc.getInput();
		
		//Cap spawns
		if (spawns.size() > 0 && spawns.size() > spawncap) {
			spawns.get(0).destroy = true;
			spawns.remove(0);
		}
		
		//Iterate and manage spawns
		for (int w = spawns.size()-1; w > -1; w--) {
			Entity e = spawns.get(w);
			if (!MainGame.getEntities().contains(e) && e.destroy != true)
				MainGame.getEntities().add(e);
			if (e.team != this.team)
				e.team = this.team;
		}
		
		//Perform functions while active
		if (this.active) {
			//Set the shot multiplier
			SPS = 1.5f + 6.5f*((getDexterity())/75f);
			if (p != null)
				SPS *= p.getFireSpeed();
			//Accept input
			getController().control(i);
			//Move player
			getController().move(delta);
			//Move through walking animation while moving
			if (getController().isMoving() == true)
				walk.update(delta);
			//Update if increased in size
			if (size != lSize) {
				init();
				lSize = size;
			} 
			
			if (i.isKeyPressed(Input.KEY_I))
				autofire = !autofire;
			//Modify hitbox and collisionbox
			if (wOverride > 0  && hitbox.getWidth() != wOverride)
				((Rectangle)hitbox).setWidth(wOverride);
			if (hOverride > 0 && hitbox.getHeight() != hOverride)
				((Rectangle)hitbox).setHeight(hOverride);
			collisionbox.setCenterX(getLocation().getX());
			collisionbox.setY(getLocation().getY()-collisionbox.getHeight()-posMod.y);
			hitbox.setCenterX(getLocation().getX());
			hitbox.setY(getLocation().getY()-hitbox.getHeight()-posMod.y);
			/*if(i.isKeyPressed(Input.KEY_B)) {
				build = !build;
			}*/
			
			//Lob functionality
			if (lob == true || (p != null && p.lob)) {
				float radius = 50;
				if (started) {
					l.set(getLocation().getX(),getLocation().getY(),l.getX2(),l.getY2());
					Vector2f l1 = new Vector2f(l.getX1(),l.getY1());
					Vector2f l2 = new Vector2f(l.getX2(),l.getY2());
					float a = Math2.calcAngle(l2,l1);
					float nx = (float) (radius*Math.cos(a))*l.length()/24;
					float ny = (float) (radius*Math.sin(a))*l.length()/24;
					target.set(l1.x+nx, l1.y+ny);
				}
				if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
					float x = i.getMouseX();
					float y = i.getMouseY();
					if (started == false && cooldown <= 0) {
						//Begin line
						l.set(getLocation().getX(),getLocation().getY(),x,y);
						float a = Math2.calcAngle(new Vector2f(l.getX1(),l.getY1()), new Vector2f(x,y));
						float nx = (float) (radius*Math.cos(a));
						float ny = (float) (radius*Math.sin(a));
						
						if (x < l.getX1() + nx && x < l.getX1()) 
							x = l.getX1() + nx;
						else if (x > l.getX1() + nx && x > l.getX1()) 
							x = l.getX1() + nx;
						if (y > l.getY1() + ny && y > l.getY1()) 
							y = l.getY1() + ny;
						else if (y < l.getY1() + ny && y < l.getY1()) 
							y = l.getY1() + ny;
						
						l.set(l.getX1(),l.getY1(),x,y);
						started = true;
					} else {
						//End line
						float a = Math2.calcAngle(new Vector2f(l.getX1(),l.getY1()), new Vector2f(x,y));
						float nx = (float) (radius*Math.cos(a));
						float ny = (float) (radius*Math.sin(a));
						
						if (x < l.getX1() + nx && x < l.getX1()) 
							x = l.getX1() + nx;
						else if (x > l.getX1() + nx && x > l.getX1()) 
							x = l.getX1() + nx;
						if (y > l.getY1() + ny && y > l.getY1()) 
							y = l.getY1() + ny;
						else if (y < l.getY1() + ny && y < l.getY1()) 
							y = l.getY1() + ny;
						
						l.set(l.getX1(),l.getY1(),x,y);
						
					}
				} else if (started && !i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
					//Fire shot in lobbed variation
					if (cooldown <= 0) {
						started = false;
						
						if (p == null) {
							//Default
							Beehive b = new Beehive(l.getX1(), l.getY1(), target, this);
							this.b.add(b);
						} else {
							//Fire Shot
							p.fire(this, target.getX(), target.getY(), 0);
						}
						//Reset line
						l.set(0,0,0,0);
						//Play shot sound
						if (s != null)
							s.play(1, 0.5f);
						cooldown = 60/SPS;
					}
				}
			} else {
				//Fire normal shot
				if (autofire || i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && build == false) {
					if (cooldown <= 0) {
						Vector2f mouse = new Vector2f(i.getMouseX(), i.getMouseY());
						angle = Math2.calcAngle(getController().getLoc(), mouse);
						if (p == null) {
							//Default 
							shootBullet(angle, true);
						} else {
							//Vary based on shot pattern
							if (p.lob == true) {
								p.fire(this, mouse.x, mouse.y, angle);
							} else if (p.targeted == true) {
								p.fire(this, getLocation().x, getLocation().y, angle, ((OverworldState)MainGame.mm.getState(1)).mtrack);
							}else {
								p.fire(this, getLocation().x, getLocation().y, angle);
							}
						}
						//Change the direction of the player
						if ((angle < Math.PI && angle > Math.PI/2) || (angle > -Math.PI && angle < -Math.PI/2)) {
							dir = false;
						} else {
							dir = true;
						}
						//Play shot sound
						if (s != null)
							s.play(1, 0.75f);
						cooldown = 60/SPS;
					}
				}
			}
			
			cooldown--;
			
			getStats().heal(((0.0025f*getMaxHealth())+0.05f*getVitality())/delta, false);
			
			if (getStats().getCurrentHealth() <= 0) {
				//Make player inactive on Death
				active = false;
				getStatusEffectManager().addEffect(new InvulnerabilityEffect(5000));
				return;
			}
			
			//Manage followers
			if (followers.size() > 0) {
				for (int i1 = 0; i1 < followers.size(); i1++) {
					Entity e = followers.get(i1);
					if (e.destroy == true) {
						followers.remove(e);
						i1--;
						continue;
					}
					if (e.team != this.team)
						e.team = this.team;
					if (!MainGame.getEntities().contains(e) && e.destroy != true)
						MainGame.getEntities().add(e);
					e.forceUpdate(delta);
				}
			}
		}
		//JOptionPane.
	}
	
	@Override
	public void draw(Graphics g) {
		if (getController().isMoving() == true && walk != null) {
			//Draw current frame of walking animation
			Vector2f iLoc = getLocation().copy();
			iLoc.y -= posMod.y;
			iLoc.x -= posMod.x;
			Image i = walk.getCurrentFrame().getScaledCopy(size).getFlippedCopy(dir, flip);
			i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
			i.setRotation(rotation);
			g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()-i.getHeight());
		} else {
			//Draw idle image
			Vector2f iLoc = getLocation().copy();
			iLoc.y -= posMod.y;
			iLoc.x -= posMod.x;
			Image i = image.getScaledCopy(size).getFlippedCopy(dir, flip);
			i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
			i.setRotation(rotation);
			g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()-i.getHeight());
		}
		if (started) {
			g.draw(l);
			g.drawOval(target.getX(), target.getY(), 3, 3);
		}
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

	@Override
	public LocalPlayerControl getController() {
		return (LocalPlayerControl)super.getController();
	}
}
