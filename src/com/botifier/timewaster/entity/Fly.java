package com.botifier.timewaster.entity;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.movements.EnemyController;

public class Fly extends Enemy {

	public Fly(float x, float y) {
		super("Fly", null, new EnemyController(x,y,1f, 100), null, null, new SpriteSheet(MainGame.getImage("Fly"), 8, 8), 10, 1f);
	}
	
	@Override
	public void init() {
		super.init();
		autoFlip = false;
		healthbarVisible = false;
		getStats().setSpeed(10);
		getController().allyCollision = false;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		getController().wander(false, 0.1f);
	}



}
