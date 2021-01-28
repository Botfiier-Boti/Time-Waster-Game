package com.botifier.timewaster.entity.enemy;

import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.botifier.timewaster.entity.FakeBagEntity;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.behaviors.CenterBehavior;
import com.botifier.timewaster.util.behaviors.CircleBehavior;
import com.botifier.timewaster.util.behaviors.SetDashBehavior;
import com.botifier.timewaster.util.bulletpatterns.BeeHivePattern;
import com.botifier.timewaster.util.bulletpatterns.ExplodePattern;
import com.botifier.timewaster.util.bulletpatterns.RockThrowPattern;
import com.botifier.timewaster.util.bulletpatterns.ShotgunPattern;
import com.botifier.timewaster.util.bulletpatterns.StabPattern;
import com.botifier.timewaster.util.movements.EnemyController;

public class BigGoblin extends Enemy {
	boolean started = false;
	//Shot patterns
	ExplodePattern ep;
	StabPattern stp;
	ShotgunPattern sp;
	BeeHivePattern bp;
	RockThrowPattern rp;
	boolean dashing = false;
	//Cooldowns
	long cooldown = 0;
	long beecooldown = 1;
	long chaseduration = 10;
	long basebee = 30;
	long basechase = 1;
	long distanceDashed = 0;
	long baseDashDistance = 1;//50
	long dashcooldown = 0;
	long basedash = 0;//40
	//Current phase
	int phase = 1;
	//int rage = 0;
	float angle = 0;
	//Phase change percentage
	float phasechange = 0.25f;
	//Last phase change
	float lastHealth = 1.0f;

	public BigGoblin(float x, float y) throws SlickException {
		super("Big Goblin", MainGame.getImage("BigGobboIdle"), new EnemyController(x, y, 35, 0.25f, 300),
				new SpriteSheet(MainGame.getImage("BigGobboWalk"), 16, 16),
				new SpriteSheet(MainGame.getImage("BigGobboAttack"), 16, 16), 1f);

		this.behaviors.add(new SetDashBehavior(this));
		this.behaviors.add(new CenterBehavior(this));
		this.behaviors.add(new CircleBehavior(this));
	}
	
	@Override
	public void init() {
		super.init();
		getController().runspeed = 75;
		activateDelay = 500;
		solid = false;
		maxhealth = 20000;
		health = maxhealth;
		obstacle = false;
		def = 40;
		size = 2.5f;
		spawncap = 10;
		sp = new ShotgunPattern();
		bp = new BeeHivePattern();
		rp = new RockThrowPattern();
		stp = new StabPattern();
		ep = new ExplodePattern();
		stp.shotMultiplier =5;
		stp.shots = 2;
		phase = 1;
		chaseduration = 1;//10;
		currentBehavior = -1;
		linger = false;
	}

	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		//Find target
		cls = null;
		if (currentBehavior == 0 || currentBehavior == -1)
		for (int i = MainGame.getEntities().size() - 1; i > -1; i--) {
			Entity en = MainGame.getEntities().get(i);
			if (en instanceof Bullet || en.isInvincible() || en == this || en.team == team || en.invulnerable == true
					|| en.active == false || en.visible == false
					|| getLocation().distance(en.getLocation()) > influence || (cls != null && getLocation().distance(en.getLocation()) > getLocation().distance(cls.getLocation())))
				continue;
			cls = en;
		}
		
		//Phase change 
		if (this.health/this.maxhealth < lastHealth-phasechange) {
			if (invulnerable)
				invulnerable = false;
			spawns.clear();
			lastHealth = lastHealth-phasechange;
			health = maxhealth*lastHealth;
			dashing = false;
			dashcooldown = basedash;
			getController().stop();
			((SetDashBehavior)behaviors.get(0)).endDash();
			((CenterBehavior)behaviors.get(1)).reset();
			((CircleBehavior)behaviors.get(2)).started = false;
			currentBehavior = -1;
			phase++;
		}
		if (active) {
			//Functionality if has an owner
			if (o != null) {
				if (o.getLocation().distance(getLocation()) <= 30) {
					dashing = false;
					getController().stop();
				}  else {
					dashing = true;
					getController().dash(o.getLocation().x, o.getLocation().y);
				}
				if (cooldown <= 0 && dashing) {
					sp.fire(this, getController().getLoc().getX(), getController().getLoc().getY(),
							o.angle);
					cooldown = (long) (5 * (3.5f * fireSpeed));
					playAttackAnimation((int) cooldown);
				} 
				cooldown--;
				return;
			}
			switch (phase) {
			case 1:
				//Phase 1
				currentBehavior = 0;
				basedash = 40;
				if (cls == null) {
					phase = -1;
					break;
				}
				dashing = ((SetDashBehavior) behaviors.get(currentBehavior)).dashing;
				if (dashing == false) {
					if (chaseduration > 0) {
						if (cls != null && dashcooldown <= 0) {
							((SetDashBehavior) behaviors.get(currentBehavior)).setTarget(cls.getLocation());
							dashcooldown = basedash;
						}
						if (dashcooldown > 0 && dashing == false) {
							dashcooldown--;
						}
						chaseduration -= 1;
					}
				} else {
					try {
						if (cooldown <= 0 && dashing) {
							sp.fire(this, getController().getLoc().getX(), getController().getLoc().getY(),
									((SetDashBehavior) behaviors.get(currentBehavior)).getAngle());
							cooldown = (long) (5 * (3.5f * fireSpeed));
							playAttackAnimation((int) cooldown);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}	
				} 
				if (cls == null && attacking == true) {
					attacking = false;
				}
				if (chaseduration <= 0) {
					/*
					 * if (spawns.size() == 0) phase = 2; else phase = 0;
					 */
					chaseduration = basechase;
				}
				break;
			case 2:
				//Phase 1.5
				currentBehavior = 1;
				/*
				 * if (spawns.size() >= spawncap) { phase = 1; chaseduration = basechase; } else
				 * if (beecooldown <= 0) { if (cls != null) { attacking = true;
				 * getController().stop(); if (cls.getController() != null) bp.fire(this,
				 * cls.getLocation().getX(), cls.getLocation().getY(), 0);
				 * 
				 * beecooldown = basebee; } }
				 */
				invulnerable = true;
				//Perform once centered
				if (((CenterBehavior)behaviors.get(1)).isCentered()){
					invulnerable = false;
					phase = 3;
					currentBehavior = 2;
					((CircleBehavior)behaviors.get(currentBehavior)).started = false;
					((CircleBehavior)behaviors.get(currentBehavior)).setCirclePos((MainGame.mm.m.getWidthInTiles()*16)/2,(MainGame.mm.m.getHeightInTiles()*16)/2);
					((CircleBehavior)behaviors.get(currentBehavior)).setRadius(4);
				}
				break;
			case 3:
				//Phase 2
				currentBehavior = 2;
				try {
					chaseduration = basechase;
					if (beecooldown <= 0) {
						rp.fire(this, getController().getLoc().getX(), getController().getLoc().getY(), 0);
						float SPS = (8 * (3.5f * 0.5f));
						SPS *= rp.fireSpeed;
						beecooldown = (long) (60 / SPS);
						playAttackAnimation((int) beecooldown);
					}

				} catch (SlickException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				//Phase 3
				currentBehavior = 1;
				boolean centered = ((CenterBehavior)behaviors.get(1)).isCentered();
				if (centered) {
					if (beecooldown <= 0) {
						rp.fire(this, getController().getLoc().getX(), getController().getLoc().getY(), 0);
						float SPS = (4 * (3.5f * 0.5f));
						SPS *= rp.fireSpeed;
						beecooldown = (long) (60 / SPS);
						playAttackAnimation((int) beecooldown);
					}
				}
				break;
			case 5:
				//Phase 4
				/*currentBehavior = 1;
				((CircleBehavior)behaviors.get(currentBehavior)).setCirclePos((MainGame.mm.m.getWidthInTiles()*16)/2,(MainGame.mm.m.getHeightInTiles()*16)/2);
				((CircleBehavior)behaviors.get(currentBehavior)).setRadius(3);*/
				if (spawns.size() < 4) {
					for (int i = 0; i < 4; i++) 
						addSpawn(new SnakeHead((MainGame.mm.m.getWidthInTiles() * 16) / 2, (MainGame.mm.m.getHeightInTiles() * 16) / 2, 19,  i*90));
					dashcooldown = 120;
					invulnerable = true;
					return;
				}
				if (cls == null) {
					invulnerable = false;
					phase = -1;
					break;
				}
				basedash = 20;
				currentBehavior = 0;
				dashing = ((SetDashBehavior) behaviors.get(0)).dashing;
				 ((SetDashBehavior) behaviors.get(0)).setDashDistance(60);
				if (dashing == false) {
					if (chaseduration > 0) {
						if (cls != null && dashcooldown <= 0) {
							invulnerable = false;
							((SetDashBehavior) behaviors.get(currentBehavior)).setTarget(cls.getLocation());
							dashcooldown = basedash;
						}
						if (dashcooldown > 0 && dashing == false) {
							dashcooldown--;
						}
					}
				} else {
					try {
						if (cooldown <= 0 && dashing) {
							sp.fire(this, getController().getLoc().getX(), getController().getLoc().getY(),
									((SetDashBehavior) behaviors.get(currentBehavior)).getAngle());
							cooldown = (long) (5 * (3.5f * fireSpeed));
							playAttackAnimation((int) cooldown);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}	
				} 
				if (cls == null && attacking == true) {
					attacking = false;
				}
				break;
			default:
				if (cls != null) {
					phase = 1;
					break;
				}
				currentBehavior = -1;
				getController().wander(false, 0.05f);
				break;
			}
		}
		
		beecooldown -= 1;
		cooldown -= 1;
	}
	
	@Override
	public void onDeath() throws SlickException {
		super.onDeath();
		Random r = new Random();
		int i = r.nextInt(300);
		if (i == 26) {
			try {
				MainGame.getEntityManager().addEntity(new FakeBagEntity(getController().src.x,getController().src.y, 1));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		} else if (i > 26 || i < 100) {
			try {
				MainGame.getEntityManager().addEntity(new FakeBagEntity(getController().src.x,getController().src.y, 0));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

}
