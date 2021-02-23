package com.botifier.timewaster.entity;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.movements.EntityController;

//visual
public class FakeBagEntity extends Entity {
	int maxtimealive = 12000;
	int timealive = 0;
	Sound s;
	boolean played = false;

	public FakeBagEntity(float x, float y, int type) throws SlickException {
		super("bag", MainGame.getImage("purplebag"), new EntityController(x,y),0f);
		solid = false;
		if (type == 1) {
			this.setImage(MainGame.getImage("whitebag"));
			maxtimealive = 48000;
		}
		//s = MainGame.getSound("lootappears");
		invincible = true;
		healthbarVisible = false;
	}

	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (played == false) {
			if (seen)
				//s.play(1f, 0.5f);
			played = true;
		}
		timealive += delta;
		if (timealive >= maxtimealive) {
			destroy = true;
		}
	}
}
