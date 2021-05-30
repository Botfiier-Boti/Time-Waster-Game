package com.botifier.timewaster.util;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.statuseffect.StatusEffect;
import com.botifier.timewaster.util.movements.BulletController;
import com.botifier.timewaster.util.movements.EntityController;

public class Bullet extends Entity {
	static Image placehold = null;
	public int[] basedamage;
	public StatusEffect effect = null;
	public boolean ignoresArmor = false;
	public boolean atkScaling = true;
	public float spread = 0;
	public float shot = 0;
	public float shots = 0;
	
	public Bullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin) throws SlickException {
		super(name, MainGame.getImage("DefaultShot"), new BulletController(x, y, speed, lifeTime, angle, origin),0);
		rotation = (float) Math.toDegrees(angle);
		basedamage = new int[] {minDmg, maxDmg};
		wOverride = 3;
		hOverride = 3;
		healthbarVisible = false;
		hasshadow = false;
	}
	
	public Bullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin, boolean pierceObstacles, boolean pierceEnemies, boolean boomerang) throws SlickException {
		super(name, MainGame.getImage("DefaultShot"), new BulletController(x, y, speed, lifeTime, angle, origin, pierceObstacles, pierceEnemies, boomerang),0);
		rotation = (float) Math.toDegrees(angle);
		basedamage = new int[] {minDmg, maxDmg};
		wOverride = 3;
		hOverride = 3;
		healthbarVisible = false;
		hasshadow = false;
	}
	
	public Bullet(Image i, String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin, boolean pierceObstacles, boolean pierceEnemies, boolean boomerang) throws SlickException {
		super(name, i, new BulletController(x, y, speed, lifeTime, angle, origin, pierceObstacles, pierceEnemies, boomerang),0);
		rotation = (float) Math.toDegrees(angle);
		basedamage = new int[] {minDmg, maxDmg};
		wOverride = 3;
		hOverride = 3;
		healthbarVisible = false;
		hasshadow = false;
	}
	
	public Bullet(String name, Image i, EntityController controller) {
		super(name,i,controller);
		rotation = 0;
		basedamage = new int[] {0, 0};
		wOverride = 3;
		hOverride = 3;
		healthbarVisible = false;
		hasshadow = false;
	}

	@Override
	public void update(int delta) throws SlickException {
		angle = getController().visualAngle;
		if (!overrideMove && getController() != null)
			getController().move(delta);
		if (getController().getOrigin() != null)
			team = getController().getOrigin().team;
		if (hitbox != null) {
			hitbox.setCenterX(getLocation().getX());
			hitbox.setY(getLocation().getY()-posMod.y-(hitbox.getHeight()));	
		}
		rotation = (float) Math.toDegrees(getController().visualAngle);
		if (getController().isMoving() == false) {
			onDestroy();
			destroy = true;
			return;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	@Override
	public void addBullet(Entity e) {
		o.addBullet(e);
	}
	
	@Override
	public BulletController getController() {
		return ((BulletController)super.getController());
	}
	
	public static Bullet createBullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin, boolean pierceObstacles, boolean pierceEnemies, boolean boomerang, boolean hasShadow, boolean atkScaling) throws SlickException {
		Bullet b = new Bullet(MainGame.getImage("DefaultShot"), name, x, y, speed, angle, lifeTime, minDmg, maxDmg, origin, pierceObstacles, pierceEnemies, boomerang);
		b.hasshadow = hasShadow;
		b.atkScaling = atkScaling;
		b.team = origin.team;
		return b;
	}
	
	public static Bullet createBullet(String name, float x, float y, float speed, float angle, long lifeTime, int minDmg, int maxDmg, Entity origin) throws SlickException {
		return createBullet(name, angle, angle, angle, angle, lifeTime, maxDmg, maxDmg, origin, false, false, false, false, true);
	}
	
	public void onDestroy() {
		
	}
	
	public void onHit() {
		
	}
	
	public Entity getOrigin() {
		return getController().getOrigin();
	}
	
	public boolean scalesWithAtk() {
		return atkScaling;
	}
	
	public boolean ignoresDefense() {
		return ignoresArmor;
	}

}
