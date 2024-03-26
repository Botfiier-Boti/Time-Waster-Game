package com.botifier.timewaster.entity;


import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.behaviors.OrbitBehavior;
import com.botifier.timewaster.util.bulletpatterns.GuidedBulletPattern;
import com.botifier.timewaster.util.movements.EnemyController;

/**
 * Bee Enemy
 * @author Botifier
 *
 */
public class Bee extends Enemy {
	/**
	 * Bee's bullet pattern
	 */
	GuidedBulletPattern gb;
	/**
	 * Sound played when the bee is hit
	 */
	Sound s;
	/**
	 * Bee's shot cooldown
	 */
	long cooldown = 0;

	/**
	 * Bee constructor
	 * @param x float X position
	 * @param y float Y position
	 */
	public Bee(float x, float y) {
		super("Bee", MainGame.getImage("Bee"), new EnemyController(x,y, 0.5f, 0), null, null);
		s = MainGame.getSound("yalikejazz");
		//Changes the "influence" modifier to 0.1f
		iModifier = 0.1f;
		//Changes max health to 250 and sets current health to that amount.
		setMaxHealth(50, true);
		//Gives the bee an insane amount of defense
		getStats().setDefense(20000);
		getStats().setDexterity((float)(2*Math.random()));
		//Randomizes the bee's movement speed
		getStats().setSpeed((float) (20+Math.random()*50));
		//Make the bee ignore collision with entities on the same team.
		getController().allyCollision = false;
		//Prevent the bee from lingering without an owner
		linger = false;
		targetable = false;
		//Adds the "Orbit" Behavior the bee and makes it orbit at a radius of 1
		behaviors.add(new OrbitBehavior(this));
		((OrbitBehavior)behaviors.get(0)).setRadius(1);

		//Adds the bullet pattern
		gb = new GuidedBulletPattern();
		patterns.add(gb);

		//Sets the Bullet Pattern to the GuidedBullet Pattern
		currentPattern = 0;
		//Sets the behavior to the "Orbit Behavior"
		currentBehavior = 0;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		//Destroy if owner dies
		if (getOwner() == null) {
			destroy = true;
			return;
		}
		//Orbit the owner
		((OrbitBehavior)behaviors.get(0)).setTarget(getOwner());
		cls = null;
		if ((currentBehavior == 0 || currentBehavior == -1) && attacking == false) {
			cls = MainGame.getEntityManager().findClosestEnemy(this, getInfluence());
		}
		if (cls != null) {
			shootBullet(Math2.calcAngle(getLocation(),cls.getLocation()), cls, false);
		}
		/*if (cooldown <= 0) {
			Entity cls = null;
			//Find closest target
			for (int i = MainGame.getEntities().size()-1; i > -1; i--) {
				Entity en = MainGame.getEntities().get(i);
				if (en instanceof Bullet || en.isInvincible() || en == this || en.getTeam() == getTeam() || en.invulnerable == true || en.active == false || en.visible == false || getLocation().distance(en.getLocation()) > getInfluence())
					continue;
				if (cls == null)
					cls = en;
				if (getLocation().distance(en.getLocation()) < getLocation().distance(cls.getLocation())) {
					cls = en;
				}
			}
			//getController().wander(false, 10f);
			//Fire bullet if target is found
			if (cls != null) {
				try {
					float angle = Math2.calcAngle(getLocation(),cls.getLocation());
					gb.fire(this, getLocation().getX(),  getLocation().getY(), angle, cls);
					cooldown = (long)(60-60/3.5f*1*Math.random());
					attacking = true;
					return;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			} else if (cls == null && attacking == true){
				attacking = false;
			}
		}*/
		//Die over time
		getStats().setCurrentHealth(getStats().getCurrentHealth()-0.1f);
		cooldown--;
	}
	
	//Unused
	@Override
	public void onHitByBullet(Bullet b) {
		super.onHitByBullet(b);
	}


}
