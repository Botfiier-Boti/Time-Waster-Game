package com.botifier.timewaster.entity.enemy;

import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.bulletpatterns.WigglyThingPattern;
import com.botifier.timewaster.util.movements.EnemyController;

public class BulletSpawner extends Enemy {
	final int WIGGLE_DAMAGE = 200;
	final int OUTER_DAMAGE = 175;
	final int length = 10;
	WigglyThingPattern wtp;
	WigglyThingPattern wtp2;
	long basecooldown;
	long cooldown = 0;
	long cooldown2 = 0;
	long cooldown3 = 0;
	long delay = 2450;
	int originU = 0;
	int originD = 0;
	int counter = 0;

	public BulletSpawner(float x, float y, long cooldown) {
		super("BulletSpawner", MainGame.getImage("Head"), new EnemyController(x, y, 0, 0), null, null);
		basecooldown = cooldown;
		wtp = new WigglyThingPattern();
		wtp2 = new WigglyThingPattern();
		setMaxHealth(45000, true);
		getStats().setSpeed(0);
		posMod.y = 3;
	}
	
	public BulletSpawner(float x, float y) {
		super("BulletSpawner", MainGame.getImage("Head"), new EnemyController(x, y, 0, 0), null, null);
		basecooldown = 750;
		wtp = new WigglyThingPattern();
		wtp2 = new WigglyThingPattern();
		setMaxHealth(45000, true);
		getStats().setSpeed(0);
		posMod.y = 3;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);

		if (counter >= 3) {
			counter = 0;
			cooldown = (long) (basecooldown*1.5);
		}
		if (cooldown3 <= 0) {

			b.add(Bullet.createBullet("Bob", getController().getLoc().x, getController().getLoc().y, 100, (float)Math.toRadians(180), 20000, OUTER_DAMAGE, 0,this,false,true,false,false,false, false, 0, 0, 0));
			b.add(Bullet.createBullet("Bob", getController().getLoc().x, getController().getLoc().y, 100, 0, 20000, OUTER_DAMAGE, 0,this,false,true,false,false,false, false, 0, 0, 0));
			cooldown3 = 50;
		}
		if (delay <= 0) {
			if (cooldown <= 0) {
				wtp.fire(this, getLocation().x,  getLocation().y, (float)Math.toRadians(-90));
				wtp2.fire(this, getLocation().x,  getLocation().y, (float)Math.toRadians(90));
				counter++;
				/*for (int i = 0; i < length; i++) {
					float fireLoc = getLocation().x-15*i;
					if (i > originU+1 || i < originU-1)
						wtp.fire(this, fireLoc, getLocation().y,  (float)Math.toRadians(-90));	
					if (i > originD+1 || i < originD-1)
						wtp2.fire(this, fireLoc, getLocation().y, (float)Math.toRadians(90));
				}
				for (int i = 0; i < length; i++) {
					float fireLoc =  getLocation().x+15*i;
					if (i > originU+1 || i < originU-1)
						wtp.fire(this, fireLoc, getLocation().y,  (float)Math.toRadians(-90));	
					if (i > originD+1 || i < originD-1)
						wtp2.fire(this, fireLoc, getLocation().y, (float)Math.toRadians(90));
				}*/
				cooldown = basecooldown;
			}
			if (cooldown2 <= 0) {
				for (int i = 0; i < 16; i++) {
					b.add(Bullet.createBullet("Bob", getController().getLoc().x-(length*15)-(i*15/2), getController().getLoc().y, 60, (float)Math.toRadians(90), 20000, OUTER_DAMAGE, 0,this,false,true,false,false,false, false, 0, 0, 0));
					b.add(Bullet.createBullet("Bob", getController().getLoc().x-(length*15)-(i*15/2), getController().getLoc().y, 60, (float)Math.toRadians(-90), 20000, OUTER_DAMAGE, 0,this,false,true,false,false,false, false, 0, 0, 0));
					b.add(Bullet.createBullet("Bob", getController().getLoc().x+(length*15)+(i*15/2), getController().getLoc().y, 60, (float)Math.toRadians(90), 20000, OUTER_DAMAGE, 0,this,false,true,false,false,false, false, 0, 0, 0));
					b.add(Bullet.createBullet("Bob", getController().getLoc().x+(length*15)+(i*15/2), getController().getLoc().y, 60, (float)Math.toRadians(-90), 20000, OUTER_DAMAGE, 0,this,false,true,false,false,false, false, 0, 0, 0));
				}
				cooldown2 = 300;
			}
			cooldown-=delta;
			cooldown2-=delta;
		} else {
			delay -= delta;
		}
		cooldown3 -= delta;
	}
	
	@Override
	public String getParameters() {
		return super.getParameters()+", "+(long)basecooldown;
	}

}
