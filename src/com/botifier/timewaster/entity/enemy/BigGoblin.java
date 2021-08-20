package com.botifier.timewaster.entity.enemy;

import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.botifier.timewaster.entity.FakeBagEntity;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.statuseffect.effects.InvulnerabilityEffect;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.behaviors.CenterBehavior;
import com.botifier.timewaster.util.behaviors.CircleBehavior;
import com.botifier.timewaster.util.behaviors.SetDashBehavior;
import com.botifier.timewaster.util.bulletpatterns.ExplodePattern;
import com.botifier.timewaster.util.bulletpatterns.RockThrowPattern;
import com.botifier.timewaster.util.bulletpatterns.ShotgunPattern;
import com.botifier.timewaster.util.bulletpatterns.StabPattern;
import com.botifier.timewaster.util.movements.EnemyController;

public class BigGoblin extends Enemy {
	boolean started = false;
	boolean dashing = false;
	//Cooldowns
	long centeringTime = 0;
	long chaseduration = 10;
	long basechase = 1;
	long distanceDashed = 0;
	long baseDashDistance = 1;//50
	long dashcooldown = 0;
	long basedash = 0;//40
	long count = 0;
	//Current phase
	int phase = 1;
	//int rage = 0;
	//float angle = 0;
	//Phase change percentage
	float phasechange = 0.25f;
	//Last phase change
	float lastHealth = 1.0f;

	public BigGoblin(float x, float y) throws SlickException {
		super("Big Goblin", MainGame.getImage("BigGobboIdle"), new EnemyController(x, y, 0.25f, 300),
				new SpriteSheet(MainGame.getImage("BigGobboWalk"), 16, 16),
				new SpriteSheet(MainGame.getImage("BigGobboAttack"), 16, 16), 1f);
		this.behaviors.add(new SetDashBehavior(this));
		this.behaviors.add(new CenterBehavior(this));
		this.behaviors.add(new CircleBehavior(this));
		ShotgunPattern sp = new ShotgunPattern();
		RockThrowPattern rp = new RockThrowPattern();
		StabPattern stp = new StabPattern();
		ExplodePattern ep = new ExplodePattern();
		patterns.add(sp);
		patterns.add(rp);
		patterns.add(stp);
		patterns.add(ep);
		stp.obstaclePierce = true;
		stp.shots = 4;
		stp.shotMultiplier = 10;
		stp.boomerang = true;
		stp.fireSpeed = 0.1f;
		stp.wavy = true;
		stp.itShots = false;
		stp.amplitude = 0.4f;
		stp.frequency = 1000f;
	}
	
	@Override
	public void init() {
		super.init();
		activateDelay = 500;
		solid = false;
		setMaxHealth(20000,true);
		getStats().setDexterity(10);;
		getStats().setDefense(20);
		getStats().setSpeed(75);
		obstacle = false;
		autoPlayAttack = false;
		size = 2.5f;
		spawncap = 10;
		phase = 1;
		chaseduration = 1;//10;
		currentBehavior = -1;
		linger = false;
		this.updateHitboxes();
	}

	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (behaviors.size() <= 0)
			return;
		//Find target
		cls = null;
		if (currentBehavior == 0 || currentBehavior == -1)
			cls = MainGame.getEntityManager().findClosestEnemy(this, influence);
		/*for (int i = MainGame.getEntities().size() - 1; i > -1; i--) {
			Entity en = MainGame.getEntities().get(i);
			if (en instanceof Bullet || en.isInvincible() || en == this || en.team == team || en.invulnerable == true
					|| en.active == false || en.visible == false
					|| getLocation().distance(en.getLocation()) > influence || (cls != null && getLocation().distance(en.getLocation()) > getLocation().distance(cls.getLocation())))
				continue;
			cls = en;
		}*/
		
		//Phase change 
		if (this.getStats().getCurrentHealth()/this.getMaxHealth() < lastHealth-phasechange) {
			spawns.clear();
			lastHealth = lastHealth-phasechange;
			getStats().setCurrentHealth(getMaxHealth()*lastHealth);
			dashing = false;
			dashcooldown = basedash;
			getController().stop();
			((SetDashBehavior)behaviors.get(0)).endDash();
			((CenterBehavior)behaviors.get(1)).reset();
			((CircleBehavior)behaviors.get(2)).started = false;
			getStatusEffectManager().removeEffect(InvulnerabilityEffect.class);
			currentBehavior = -1;
			count = 0;
			phase++;
		}
		if (active) {

			float SPS = (1.5f + 6.5f*((getDexterity())/75f));
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
					shootBullet(o.angle);
				} 
				cooldown--;
				return;
			}
			switch (phase) {
			case 1:
				//Phase 1
				currentBehavior = 0;
				currentPattern = 0;
				basedash = 40;
				if (cls == null && dashing == false) {
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
						/*if (cooldown <= 0 && dashing) {
							playAttackAnimation((int) (5 * (3.5f * fireSpeed)/2));
						}
						if (attacking == false && dashing && cooldown <= 0) {
							sp.fire(this, getController().getLoc().getX(), getController().getLoc().getY(),
									((SetDashBehavior) behaviors.get(currentBehavior)).getAngle());
							cooldown = (long) (5 * (3.5f * fireSpeed));
						}*/
						
						if (shootBullet(((SetDashBehavior)behaviors.get(currentBehavior)).getAngle()))
							playAttackAnimation(60/SPS);
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
				//Perform once centered
				if (((CenterBehavior)behaviors.get(1)).isCentered() || centeringTime > 300) {
					invulnerable = false;
					phase = 3;
					currentBehavior = 2;
					((CircleBehavior)behaviors.get(currentBehavior)).started = false;
					((CircleBehavior)behaviors.get(currentBehavior)).setCirclePos(MainGame.getCurrentMap().getCenter().x,MainGame.getCurrentMap().getCenter().y);
					((CircleBehavior)behaviors.get(currentBehavior)).setRadius(4);
					getStatusEffectManager().removeEffect(InvulnerabilityEffect.class);
				} else {
					centeringTime++;
					if (this.getStatusEffectManager().hasEffect(InvulnerabilityEffect.class) == false) {
						InvulnerabilityEffect e = new InvulnerabilityEffect(10);
						e.setInfinite(true);
						this.getStatusEffectManager().addEffect(e);	
					}
				}
				break;
			case 3:
				//Phase 2
				currentPattern = 1;
				currentBehavior = 2;
				try {
					chaseduration = basechase;
					SPS *= patterns.get(currentPattern).fireSpeed;
					if (count <= 0)
						count -= delta;
					else 
						count+=delta;
					if (count <= -60/SPS) {
						playAttackAnimation(60/SPS);
						count = 1;
					}
					if (count > (long)aAttack.getDuration(0)*1.3) {
						shootBullet(0, true);
						count = 0;
					}

				} catch (SlickException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				//Phase 3
				currentPattern = 1;
				currentBehavior = 1;
				SPS *= patterns.get(currentPattern).fireSpeed;
				if (count <= 0)
					count -= delta;
				else 
					count+=delta;
				if (count <= -60/SPS) {
					playAttackAnimation(60/SPS);
					count = 1;
				}
				if (count > (long)aAttack.getDuration(0)*1.3) {
					shootBullet(0, true);
					count = 0;
				}
				break;
			case 5:
				//Phase 4
				if (spawns.size() < 4) {
					for (int i = 0; i < 4; i++) 
						addSpawn(new SnakeHead(MainGame.getCurrentMap().getCenter().x, MainGame.getCurrentMap().getCenter().y, 20,  i*90));
					this.getStatusEffectManager().addEffect(new InvulnerabilityEffect(1500));
					this.dashcooldown = 100;
					return;
				}
				basedash = 0;
				currentBehavior = 1;
				currentPattern = 3;
				SPS *= patterns.get(currentPattern).fireSpeed;
				if (dashcooldown <= 0) {
					if (count <= 0)
						count -= delta;
					else 
						count+=delta;
					if (count <= -60/SPS) {
						playAttackAnimation(60/SPS);
						count = 1;
					}
					if (count > (long)aAttack.getDuration(0)*1.1) {
						if (shootBullet(angle, true)) {
							getController().setAngle(angle-((float)Math.PI/128f));
						}
						count = 0;
					}
				} else
					dashcooldown--;
				
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
	}
	
	@Override
	public void onDeath() throws SlickException {
		super.onDeath();
		Random r = new Random();
		int i = r.nextInt(300);
		if (i == 26) {
			try {
				MainGame.getEntityManager().addEntity(new FakeBagEntity(getLocation().x, getLocation().y, 1));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		} else if (i > 26 || i < 100) {
			try {
				MainGame.getEntityManager().addEntity(new FakeBagEntity(getLocation().x,getLocation().y, 0));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

}
