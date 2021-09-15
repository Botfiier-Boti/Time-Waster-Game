package com.botifier.timewaster.entity.enemy;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.behaviors.CircleBehavior;
import com.botifier.timewaster.util.bulletpatterns.ExplodePattern;
import com.botifier.timewaster.util.bulletpatterns.SpherePattern;
import com.botifier.timewaster.util.movements.EnemyController;

//Test thingy
public class IceSphereClone extends Enemy {
	//Bullet patterns
	SpherePattern sp;
	ExplodePattern ep;
	//Cooldowns
	long cooldown = 0;
	long dashcooldown;

	public IceSphereClone(float x, float y) throws SlickException {
		super("Ice Sphere", MainGame.getImage("FakeIce"), new EnemyController(x, y, 0.1f, 100), null, null,2f);
		sp = new SpherePattern();
		ep = new ExplodePattern();
		behaviors.add(new CircleBehavior(this));
		currentBehavior = 0;
		getStats().setDefense(0);
		getStats().setSpeed(100);
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		
		
		if (dashcooldown <= 0) {
			//Find target
			Entity cls = null;
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
			//Dash at target 
			if (cls != null) {
				getStats().setSpeed(100);
				currentBehavior = -1;
				getController().dash(cls.getLocation().x, cls.getLocation().y);
				dashcooldown = 500;
				return;
			} else {
				getStats().setSpeed(1);
				getController().wander(false,0.25f);
			}
		} else {
			getStats().setSpeed(1);
			//wander
			getController().wander(false,0.25f);
		}
		if (getOwner() != null) {
			//Circle owner if it exists
			CircleBehavior cb = (CircleBehavior)(behaviors.get(0));
			currentBehavior = 0;
			cb.setRadius(2);
			cb.setCirclePos(getOwner().getLocation());
		}
		//Fire shots
		if (cooldown <= 0) {
			try {
				sp.fire(this, getController().getLoc().getX(), getController().getLoc().getY(), 0);
				cooldown = (long)(60/3.5f*sp.fireSpeed);
				return;
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		dashcooldown-=1;
		cooldown-=1;
	}
	
	@Override
	public void onDeath() {
		//Explode on death
		if (active) {
			try {
				ep.fire(this, getController().getLoc().getX(), getController().getLoc().getY(), 0);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			active = false;
		}
		destroy = true;
	}

}
