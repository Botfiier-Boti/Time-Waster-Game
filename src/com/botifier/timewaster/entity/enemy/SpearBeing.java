package com.botifier.timewaster.entity.enemy;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.behaviors.DashBehavior;
import com.botifier.timewaster.util.bulletpatterns.PincerPattern;
import com.botifier.timewaster.util.movements.EnemyController;

public class SpearBeing extends Enemy {
	boolean shot = false;

	public SpearBeing(float x, float y) {
		super("Spearman", MainGame.getImage("spearman"), new EnemyController(x, y, 1f, 80), new SpriteSheet(MainGame.getImage("spearmanwalk"), 8, 8), null);
		DashBehavior db = new DashBehavior(this);
		db.setAutomatic(true);
		db.setCoolodown(170);
		db.setBonusSpeed(50);
		addBehavior(db);
		patterns.add(new PincerPattern());
		currentPattern = 0;
		currentBehavior = 0;
	}
	
	@Override
	public void init() {
		super.init();
		setMaxHealth(500f, true);
		getStats().setDefense(0f);
		getStats().setSpeed(20f);
		getController().allyCollision = false;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (((DashBehavior)behaviors.get(0)).isDashing() == false) {
			getController().wander(false, 0.15f);
			if (shot == false) {
				shootBullet(angle);
				shot = true;
			}
		} else {
		   shot = false;
		}
	}
}
