package com.botifier.timewaster.entity;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.behaviors.OrbitBehavior;
import com.botifier.timewaster.util.movements.EnemyController;

/**
 * Bee Swarm class
 * @author Botifier
 *
 */
public class BeeSwarm extends Enemy{
	/**
	 * Circle of influence
	 */
	Circle influenceCircle = null;
	
	/**
	 * Bee Swarm constructor
	 * @param x float X position
	 * @param y float Y position
	 * @param size int Amount of bees within the swarm
	 * @param owner Entity The owner of the swarm
	 */
	public BeeSwarm(float x, float y, int size, Entity owner) {
		super("Bee Swarm", MainGame.getImage("beehive"), new EnemyController(x,y,3f,50, false), null, null);
		visible = true;
		iModifier = 0.1f;
		setMaxHealth(1,false);
		spawncap = size;
		invulnerable = true;
		healthbarVisible = false;
		getStats().setSpeed(50);
		setOwner(owner);
		getController().allyCollision = false;
		//Destroy if owner is invalid
		if (getOwner() == null) {
			destroy = true;
			return;
		}
		//Create bees
		for (int i = 0; i < size; i++) {
			Bee b = new Bee(x,y);
			b.getController().allyCollision = false;
			b.getStats().setAttack(getOwner().getAttack()/4);
			b.setTeam(this.getTeam());
			b.setOwner(this);
			spawns.add(b);
		}
		//behaviors.add(new OrbitBehavior(this));
		//((OrbitBehavior)behaviors.get(0)).setTeleport(true);
		currentBehavior = 0;
		influenceCircle = new Circle(this.getLocation().x, this.getLocation().y, getInfluence());
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
			//((OrbitBehavior)behaviors.get(0)).setRadius(2);
			//Orbit owner
			//if (((OrbitBehavior)behaviors.get(0)).getTarget() != getOwner())
				//((OrbitBehavior)behaviors.get(0)).setTarget(this.getOwner());
			/*Change based on owner movement
			if (getOwner().getController().isMoving()) {

				getStats().setSpdMod(getOwner().getSpeed()*2);
			} else {
				getStats().setSpdMod(0);
			}*/

			getStats().setSpdMod(getOwner().getSpeed());
			//Iterate through bees
			for (int i = spawns.size()-1; i > -1; i--) {
				Bee b = (Bee) spawns.get(i);
				if (b.destroy == true) {
					spawns.remove(b);
					MainGame.getEntityManager().removeEntity(b);
					continue;
				}
				if (b.getTeam() != this.getTeam())
					b.setTeam(this.getTeam());
				if (!MainGame.getEntities().contains(b) && b.destroy != true)
					MainGame.getEntities().add(b);
				if (b.getController().wanderArea != this.influenceCircle) {
					b.getController().wanderArea = this.influenceCircle;
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
