package com.botifier.timewaster.entity;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.behaviors.OrbitBehavior;
import com.botifier.timewaster.util.movements.EnemyController;

public class BeeSwarm extends Enemy{
	Circle influenceCircle = null;
	
	public BeeSwarm(float x, float y, int size, Entity owner) {
		super("Bee Swarm", MainGame.getImage("beehive"), new EnemyController(x,y,50,3f,50, false), null, null);
		visible = true;
		iModifier = 0.1f;
		maxhealth = 1;
		health = 1;
		spawncap = size;
		invulnerable = true;
		healthbarVisible = false;
		o = owner;
		//Destroy if owner is invalid
		if (o == null) {
			destroy = true;
			return;
		}
		//Create bees
		for (int i = 0; i < size; i++) {
			Bee b = new Bee(x,y);
			b.bAttack = getOwner().atk/4;
			b.team = this.team;
			b.o = this;
			spawns.add(b);
		}
		behaviors.add(new OrbitBehavior(this));
		currentBehavior = 0;
		influenceCircle = new Circle(this.getLocation().x, this.getLocation().y, influence);
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		//Destroy if owner dies
		if (getOwner() == null) {
			destroy = true;
			return;
		}
		
		if (spawns.size() > 0) {
			//getController().wander(false, 1f);
			((OrbitBehavior)behaviors.get(0)).setRadius(2);
			//Orbit owner
			((OrbitBehavior)behaviors.get(0)).setTarget(this.getOwner());
			//Change based on owner movement
			if (getOwner().getController().isMoving()) {
				getController().bonusspeed = getOwner().getController().speed;
			} else {
				getController().bonusspeed = 0;
			}
			//Iterate through bees
			for (int i = spawns.size()-1; i > -1; i--) {
				Bee b = (Bee) spawns.get(i);
				if (b.destroy == true) {
					spawns.remove(b);
					MainGame.getEntityManager().removeEntity(b);
					continue;
				}
				if (b.team != this.team)
					b.team = this.team;
				if (!MainGame.getEntities().contains(b) && b.destroy != true)
					MainGame.getEntities().add(b);
				if (b.getController().wanderArea != this.influenceCircle) {
					b.getController().wanderArea = this.influenceCircle;
				}
				if (getOwner().getController().isMoving()) {
					b.getController().bonusspeed = getOwner().getController().speed;
				} else {
					b.getController().bonusspeed = 0;
				}
			}
		} else {
			//Destroy if out of bees
			destroy = true;
		}
	}
	
	@Override 
	public void onDeath() throws SlickException {
		super.onDeath();
	}

	@Override
	public EnemyController getController() {
		return (EnemyController) super.getController();
	}


}
