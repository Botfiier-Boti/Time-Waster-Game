	package com.botifier.timewaster.entity.enemy;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.statuseffect.effects.InvulnerabilityEffect;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.behaviors.OrbitBehavior;
import com.botifier.timewaster.util.behaviors.SetDashBehavior;
import com.botifier.timewaster.util.bulletpatterns.*;
import com.botifier.timewaster.util.movements.EnemyController;

public class Construct extends Enemy {
	Animation activate;
	boolean activated = false;
	int phase = 0;
	
	//Phase change percentage
	float phasechange = 0.05f;
	//Last phase change
	float lastHealth = 1.0f;
	
	public Construct(float x, float y) {
		super("Construct", MainGame.getImage("Construct"), new EnemyController(x, y, 0.5f, 100, true), new SpriteSheet(MainGame.getImage("ConstructWalk"), 16, 16), null);
		activate = new Animation(new SpriteSheet(MainGame.getImage("ConstructActivate"), 16, 16), 200);
		activate.setLooping(false);
		activate.setAutoUpdate(true);
		activate.stop();
		this.behaviors.add(new OrbitBehavior(this));
		this.behaviors.add(new SetDashBehavior(this));
		StabPattern stp = new StabPattern();
		stp.shots = 1;
		stp.shotMultiplier = 4;
		stp.momentumDelay = 1000;
		stp.homing = true;
		stp.homingThreshold = 0.2f;
		this.patterns.add(stp);
		this.patterns.add(new ConstructEyeLasers());
	}
	
	@Override
	public void init() {
		super.init();
		activateDelay = 500;
		solid = false;
		setMaxHealth(22500,true);
		getStats().setDexterity(10);
		getStats().setDefense(150);
		getStats().setSpeed(5);
		obstacle = false;
		autoPlayAttack = false;
		spawncap = 10;
		linger = false;		
		size = 3f;
		this.updateHitboxes();
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		Entity cls;
		if (getController().isMoving() == false) {
			current = activate.getCurrentFrame();
			activate.update(delta);
		}
		if (this.getStats().getCurrentHealth()/this.getMaxHealth() < lastHealth-phasechange) {
			spawns.clear();
			lastHealth = lastHealth-phasechange;
			getStats().setCurrentHealth(getMaxHealth()*lastHealth);
			getController().stop();
			getStatusEffectManager().removeEffect(InvulnerabilityEffect.class);
			phase++;
		}
		if (activated) {
			if (activate.isStopped()) {
				getStatusEffectManager().removeEffect(InvulnerabilityEffect.class);
				cls = MainGame.getEntityManager().findClosestEnemy(this, 200);
				if (cls != null) {
					float angle = Math2.calcAngle(cls.getLocation(),getLocation());
					switch (phase) {
						case 0:
							currentBehavior = 0;
							OrbitBehavior cb = (OrbitBehavior)behaviors.get(0);
							cb.setTarget(cls);
							cb.setRadius(1.1f);
							cb.setTheta(angle);
							currentPattern = 1;
							shootBullet(Math2.calcAngle(getLocation(),cls.getLocation()), cls, false);
							break;
						case 1:
							currentBehavior = 1;
							currentPattern = 0;
							SetDashBehavior db = (SetDashBehavior)behaviors.get(1);
							db.setDashDistance(1);
							db.setBonusSpeed(50);
							if (db.isDashing() == false) {
								shootBullet(Math2.calcAngle(getLocation(),cls.getLocation()), cls, true);
								db.setTarget(cls.getLocation());
							}
							break;
						default:
							currentBehavior = 0;
							phase = 0;
							break;
					}
				} else {
					currentBehavior = -1;
				}
			}
		} else {
			cls = MainGame.getEntityManager().findClosestEnemy(this, 75);
			if (cls != null && active == true) {
				activated = true;
				activate.restart();
				activate.start();
			} else if (this.getStatusEffectManager().hasEffect(InvulnerabilityEffect.class) == false) {
				InvulnerabilityEffect e = new InvulnerabilityEffect(10);
				e.setInfinite(true);
				this.getStatusEffectManager().addEffect(e);	
			}
		}
		
	}
	
	@Override
	public void onDeath() throws SlickException {
		super.onDeath();
		MainGame.getEntityManager().addEntity(new Hoppy(getLocation().x, getLocation().y));
	}
	
}
