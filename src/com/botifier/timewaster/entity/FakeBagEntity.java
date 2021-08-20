package com.botifier.timewaster.entity;

import org.newdawn.slick.SlickException;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Inventory;
import com.botifier.timewaster.util.Team;
import com.botifier.timewaster.util.movements.EntityController;

//visual
public class FakeBagEntity extends Entity {
	private Inventory i;
	private int maxtimealive = 24000;
	private int timealive = 0;
	private boolean played = false;

	public FakeBagEntity(float x, float y, int type) throws SlickException {
		super("bag", MainGame.getImage("purplebag"), new EntityController(x,y),0f);
		solid = false;
		if (type == 1) {
			this.setImage(MainGame.getImage("whitebag"));
			maxtimealive = 48000;
		}
		//s = MainGame.getSound("lootappears");
		i = new Inventory(this, 8);
		team = Team.ALLY;
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
	
	public Inventory getInventory() {
		return i;
	}
}
