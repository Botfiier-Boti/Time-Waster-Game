package com.botifier.timewaster.util.bulletpatterns;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;

public abstract class BulletPattern {
	protected String name;
	protected long shotIt = 1;
	Entity target = null;
	public float aMod = 0;
	public float offset = 0;
	public float aOffset = 0;
	public float amplitude = 1;
	public float frequency = 0.25f;
	public float fireSpeed = 1.0f;
	public float scale = 1.0f;
	public float homingThreshold = 0.85f;
	public float bulletSpeed = 300; 
	public long momentumDelay = 0;
	public long momentumCap = 0;
	public long duration = 100;
	public int shots = 1;
	public float spread = 2;
	public int mindamage = 10, maxdamage = 10;
	public boolean enemyPierce = false;
	public boolean obstaclePierce = false;
	public boolean armorPierce = false;
	public boolean atkScaling = true;
	public boolean boomerang = false;
	public boolean hasShadow = false;
	public boolean targeted = false;
	public boolean wavy = false;
	public boolean lob = false;
	public boolean inf = false;
	public boolean itShots = true;
	public boolean homing = false;
	public boolean predictive = false;
	public Image override = null;
	
	
	public abstract void fire(Entity owner, float x, float y, float angle, Entity target) throws SlickException;
	
	public void fire(Entity owner, float x, float y, float angle) throws SlickException {
		fire(owner,x,y,angle,null);
	}
	
	public Bullet createBullet(Entity owner, float x, float y, float angle, int i) throws SlickException {
		/*if (owner.active == false)
			return null;
		double na = Math.toDegrees(angle);
		na = Math.toDegrees(angle)+((i*spread));
		Bullet b = Bullet.createBullet("Bob", x, y, bulletSpeed, (float)Math.toRadians(na), duration, mindamage,maxdamage,owner, obstaclePierce, enemyPierce, boomerang, hasShadow, atkScaling);
		b.ignoresArmor = armorPierce;
		b.getController().setHoming(targeted);
		b.getController().target = target;
		if (override != null)
			b.setImage(override);*/
		return createBullet(owner,x,y,angle,i,target);
	}
	
	public Bullet createBullet(Entity owner, float x, float y, float angle, int i, Entity target) throws SlickException {
		if (owner.active == false)
			return null;
		double na = Math.toDegrees(angle);
		if (shots % 2 != 0) {
			na = Math.toDegrees(angle)-(((shots/2)-i)*(spread));
		} else if (shots % 2 == 0 && shots != 0) {
			na = Math.toDegrees(angle)-((shots/2-i-0.5)*(spread));
		}
		float nx = 0;
		float ny = 0;
		if (offset > 0 || offset < 0) {
			float rad = (float) ((offset)*Math.sqrt(offset));
			float aRad = (float) Math.toRadians(aOffset);
			nx = (int) ((rad*8) * Math.cos(angle+aRad));
			ny = (int) ((rad*8) * Math.sin(angle+aRad));		
		}
		if (predictive == true && target != null && target.getController().isMoving()) {
			na = Math.toDegrees(Math2.calcAngle(new Vector2f(x+nx, y+ny), new Vector2f(target.getLocation().x+(float)(Math.sin(target.angle))*target.getController().getPPS()*10,target.getLocation().y+(float)(Math.cos(target.angle))*target.getController().getPPS()*10)));
		}
		Bullet b = Bullet.createBullet("Bob", x+nx, y+ny, bulletSpeed, (float)Math.toRadians(na+aMod), duration, mindamage,maxdamage,owner, obstaclePierce, enemyPierce, armorPierce, boomerang, hasShadow, atkScaling, shotIt, shots, spread);
		b.getController().inf = inf;
		b.size = scale;
		b.getController().wavy = wavy;
		b.getController().frequency = frequency;
		b.getController().amplitude = amplitude;
		b.getController().homingThreshold = homingThreshold;
		b.getController().momentumDelay = momentumDelay;
		b.getController().momentumCap = momentumCap;
		/*if (i % 2 == 0) {
			//b.getController().amplitude = -amplitude;
		}*/
		if (override != null)
			b.setImage(override);
		if (target != null && homing) {
			b.getController().setHoming(true);
			b.getController().target = target;	
		}
		//xOffset = -xOffset;
		//yOffset = -yOffset;
		if (itShots)
			shotIt++;
		if (shotIt > 2)
			shotIt = 1;
		owner.addBullet(b);
		return b;
	}
	
	public String getName() {
		return name != null ? name : getClass().getSimpleName();
	}
	
	public float getFireSpeed() {
		return fireSpeed;
	}
	
	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setTarget(Entity e) {
		target = e;
	}

	
}
