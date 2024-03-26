package com.botifier.timewaster.entity.player;


//import java.awt.Color;

import org.newdawn.slick.Animation;
//import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.MouseFollower;
import com.botifier.timewaster.entity.projectile.Beehive;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.states.OverworldState;
import com.botifier.timewaster.statuseffect.effects.InvulnerabilityEffect;
import com.botifier.timewaster.util.AbilityItem;
//import com.botifier.timewaster.entity.ShotgunPattern;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.EquipmentInventory;
import com.botifier.timewaster.util.Inventory;
import com.botifier.timewaster.util.Item.SlotType;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.Team;
import com.botifier.timewaster.util.bulletpatterns.*;
import com.botifier.timewaster.util.managers.StatusEffectManager;
import com.botifier.timewaster.util.movements.LocalPlayerControl;

public class Player extends Entity {
	//Inventories
	public EquipmentInventory ei;
	public Inventory inv;
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
	public BulletPattern p;
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
		setTeam(Team.ALLY);
		overrideMove = true;
		setMaxHealth(620, true);
		getStats().setAttack(60);
		getStats().setDexterity(40);
		getStats().setDefense(15);
		getStats().setVitality(30);
		getStats().setSpeed(105);
		/*getStats().setVitMod(9000000000000000f);
		getStats().setDefMod(1000000000000000f);
		getStats().setHealthMod(1000f);
		getStats().setDexMod(100000f);
		getStats().setAtkMod(1000f);*/
		
		spawncap = 4;
		

		inv = new Inventory(this, 8);
		if (MainGame.debug)
			inv = new Inventory(this, 16);
		
		ei = new EquipmentInventory(this, 4);
		ei.setSlotType(SlotType.EQUIP_WEAPON, 0);
		ei.setSlotType(SlotType.EQUIP_ABILITY, 1);
		ei.setSlotType(SlotType.EQUIP_ARMOR, 2);
		ei.setSlotType(SlotType.EQUIP_RING, 3);

		sem = new StatusEffectManager(this);
		/*followers.add(new MouseFollower(0, 0, 90, this));
		followers.add(new MouseFollower(0, 0, -90, this));
		followers.add(new MouseFollower(0, 0, 0, this));
		followers.add(new MouseFollower(0, 0, 180, this));*/
		//p = new ShotgunPattern();
		//p = new StabPattern();
		//p = new SpinPattern();
		//p = new RockShatterPattern();
		//p = new ExplodePattern();
		//p = new RockThrowPattern();
		//p = new GuidedBulletPattern();
		//p = new WigglyThingPattern();
		//p = new SpherePattern();
		//p = new BeeHivePattern();
		//p = new PincerPattern();
		//build = true;
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(delta);
		//Get input
		Input i = gc.getInput();
		
		//Cap spawns
		if (spawns.size() > 0 && spawns.size() > spawncap) {
			spawns.get(0).destroy = true;
			spawns.remove(0);
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
			updateHitboxes();
			//Move through walking animation while moving
			if (getController().isMoving() == true)
				walk.update(delta);
			
			if (i.isKeyPressed(Input.KEY_I))
				autofire = !autofire;
			
			if (i.isKeyPressed(Input.KEY_SPACE)) {
				AbilityItem ai = (AbilityItem) ei.getFirstItemOfSlot(SlotType.EQUIP_ABILITY);
				if (ai != null) {
					ai.onUseAbility(this, null, new Vector2f(i.getMouseX(), i.getMouseY()));
				}
			}
			
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
				if (autofire || ((i.isMousePressed(Input.MOUSE_LEFT_BUTTON) || i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) && MainGame.getGUI().getFocused() == null) && build == false) {
					if (cooldown <= 0) {
						Vector2f mouse = new Vector2f(i.getMouseX(), i.getMouseY());
						setAngle(Math2.calcAngle(getController().getLoc(), mouse));
						if (p == null) {
							//Default 
							shootBullet(getAngle());
						} else {
							//Vary based on shot pattern
							if (p.lob == true) {
								p.fire(this, mouse.x, mouse.y, getAngle());
							} else if (p.targeted == true) {
								p.fire(this, getLocation().x, getLocation().y, getAngle(), ((OverworldState)MainGame.mm.getState(1)).mtrack);
							}else {
								p.fire(this, getLocation().x, getLocation().y, getAngle());
							}

							cooldown = 60/SPS;
						}
						//Change the direction of the player
						if ((getAngle() < Math.PI && getAngle() > Math.PI/2) || (getAngle() > -Math.PI && getAngle() < -Math.PI/2)) {
							dir = false;
						} else {
							dir = true;
						}
						//Play shot sound
						if (s != null)
							s.play(1, 0.75f);
					}
				}
			}
			
			cooldown--;
			
			getStats().heal(((0.0025f*getMaxHealth())+0.05f*getVitality())/delta, false);
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
			Image i = walk.getCurrentFrame().getScaledCopy(getSize()).getFlippedCopy(dir, flip);
			i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
			i.setRotation(rotation);
			//i.drawFlash(iLoc.getX()-1-i.getWidth()/2, iLoc.getY()-1-i.getHeight(), i.getWidth()+2f, i.getHeight()+2f, Color.black);
			g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()-i.getHeight());
		} else {
			//Draw idle image
			Vector2f iLoc = getLocation().copy();
			iLoc.y -= posMod.y;
			iLoc.x -= posMod.x;
			Image i = image.getScaledCopy(getSize()).getFlippedCopy(dir, flip);
			i.setCenterOfRotation(i.getWidth()/2, i.getHeight()/2);
			i.setRotation(rotation);
			//i.drawFlash(iLoc.getX()-1-i.getWidth()/2, iLoc.getY()-1-i.getHeight(), i.getWidth()+2f, i.getHeight()+2f, Color.black);
			g.drawImage(i, iLoc.getX()-i.getWidth()/2, iLoc.getY()-i.getHeight());
		}
		if (started) {
			g.draw(l);
			g.drawOval(target.getX(), target.getY(), 3, 3);
		}
		if (MainGame.displayHitboxes) {
			g.drawLine(getLocation().getX(), getLocation().getY(), getLocation().getX()+((float)Math.cos(getAngle())*5), getLocation().getY()+((float)Math.sin(getAngle())*5));
			g.draw(hitbox);
			g.draw(collisionbox);
			if (getController().testBox != null)
				g.draw(getController().testBox);
			if (getController().dst != null) {
				g.drawLine(getLocation().getX(), getLocation().getY(), getController().dst.getX(), getController().dst.getY());
			}
			/*g.drawRect(((int)getLocation().getX()/16)*16, ((int)getLocation().getY()/16)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16+1)*16, ((int)getLocation().getY()/16)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16-1)*16, ((int)getLocation().getY()/16)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16)*16, ((int)getLocation().getY()/16+1)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16)*16, ((int)getLocation().getY()/16-1)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16+1)*16, ((int)getLocation().getY()/16+1)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16-1)*16, ((int)getLocation().getY()/16+1)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16+1)*16, ((int)getLocation().getY()/16-1)*16, 16, 16);
			g.drawRect(((int)getLocation().getX()/16-1)*16, ((int)getLocation().getY()/16-1)*16, 16, 16);*/
		}
	}
	
	@Override
	public void onDeath() {
		active = false;
		getStatusEffectManager().addEffect(new InvulnerabilityEffect(5000));
	}

	@Override
	public LocalPlayerControl getController() {
		return (LocalPlayerControl)super.getController();
	}
}
