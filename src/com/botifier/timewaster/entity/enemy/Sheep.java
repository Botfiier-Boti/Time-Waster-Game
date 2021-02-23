package com.botifier.timewaster.entity.enemy;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.Team;
import com.botifier.timewaster.util.bulletpatterns.SpinPattern;
import com.botifier.timewaster.util.movements.EnemyController;

public class Sheep extends Enemy {
	//Last person to attack
	Entity lastAttacker = null;
	//Shot pattern
	SpinPattern sp;

	public Sheep(float x, float y) {
		super("Sheep", MainGame.getImage("idlesheep"), new EnemyController(x, y, 1f, 20), new SpriteSheet(MainGame.getImage("sheep"), 8, 8), null,0.5f);
		sp = new SpinPattern();
		team = Team.ENEMY;
		setMaxHealth(500, true);
		getStats().setSpeed(5);
		getStats().setDefense(50);
	}

	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (lastAttacker != null) {
			//Attack the last entity to attack the sheep
			if (lastAttacker.getLocation().distance(getLocation()) < influence) {
				float angle = Math2.calcAngle(getController().getLoc(), lastAttacker.getLocation());
				sp.fire(this, getLocation().getX(), getLocation().getY(), angle);
				if (lastAttacker.getLocation().distance(getLocation()) < influence*0.75)
					//flee from attacker
					getController().flee(lastAttacker.getLocation().x,lastAttacker.getLocation().y);
				else 
					getController().stop();
			} else {
				//wander
				getController().wander(false, 2f);//getController().dash(lastAttacker.getLocation().x,lastAttacker.getLocation().y);
			}
			
			//Reset last attacker
			if (lastAttacker.destroy == true) {
				lastAttacker = null;
			} else if (lastAttacker.getStats().getCurrentHealth() <= 0) {
				lastAttacker = null;
			} else if (lastAttacker.invulnerable == true) {
				lastAttacker = null;
			} else if (lastAttacker.isInvincible() == true) {
				lastAttacker = null;
			}
		} else {
			//wander
			getController().wander(false, 2f);
		}
		
	}
	
	@Override
	public void onHit(int damage, Entity origin, boolean ignoresDefense) {
		super.onHit(damage, origin, ignoresDefense);
		if (damage > 0) {
			lastAttacker = origin;
		}
	}
}
