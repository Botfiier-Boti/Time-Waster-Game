package com.botifier.timewaster.util.movements;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;

public class BulletController extends EntityController {
	boolean negativeSpeed = false;
	boolean pierceEnemies = false;
	boolean pierceObstacles = false;
	boolean pierceDefense = false;
	boolean boomerangs = false;
	private Vector2f nLoc = new Vector2f(0,0);
	private boolean homing = false;
	public boolean inf = false;
	public boolean wavy = false;
	public float PPS;
	public float speed;
	public float angleMod = 0f;
	public float homingThreshold = 0.85f;
	public float acceleration = 0f;
	public float deceleration = 0f;
	public float waveIterator = 1/8f;
	public double amplitude = 1;
	public double frequency = 0.25;
	public long momentumDelay = 0; 
	public long momentumCap = -1;
	float damage = 0.0f;
	long distTr = 0;
	long dist = 100;
	long bpoint = 100;
	float wp2 = 0;
	float angle;
	public Entity target;
	private Entity origin;
	private ArrayList<Entity> enemiesHit = new ArrayList<Entity>();
	private Vector2f originPoint;
	private double wavePos = 0;
	
	public BulletController(float x, float y, float speed, long life, float angle, Entity origin) {
		super(x, y);
		this.dist = life;
		this.angle = angle;
		this.speed = speed;
		moving = true;
		this.setOrigin(origin);
		this.originPoint = this.getLoc();
	}
	
	public BulletController(float x, float y, float speed, long life, float angle, Entity origin, boolean ignoreObstacles, boolean pierceEnemies, boolean boomerang) {
		super(x, y);
		this.dist = life;
		this.angle = angle;
		this.speed = speed;
		moving = true;
		this.setOrigin(origin);
		this.pierceObstacles = ignoreObstacles;
		this.pierceEnemies = pierceEnemies;
		this.boomerangs = boomerang;
		bpoint = dist/2;
		this.originPoint = this.getLoc();
	}
	
	@Override
	public void move(int delta) {
		if (getOwner().destroy)
			return;
		PPS = speed/60f;
		if (moving || inf) {
			if (isHoming() && distTr < dist*homingThreshold) {
				if (dst != null) {
					float dAngle = Math2.calcAngle(getLoc(), dst);	
					angle = Math2.lerpAngle(angle, dAngle, ((float)-Math.PI/360*1f*delta));
					
					/*if (getLoc().distance(dst) < PPS)
						PPS = 0;
					else  {
						float dAngle = Math2.calcAngle(getLoc(), dst);	
						angle = dAngle;
					}*/
				} else if (target != null) {
					float dAngle = Math2.calcAngle(getLoc(), target.getLocation());	
					angle = Math2.lerpAngle(angle, dAngle, ((float)-Math.PI/360*(0.5f*PPS)*delta));
					/*if (getLoc().distance(target.getLocation()) < PPS)
						PPS = 0;
					else  {
						float dAngle = Math2.calcAngle(getLoc(), target.getLocation());	
						angle = dAngle;
					}*/
				} else {
					target = MainGame.getEntityManager().findClosestEnemy(getOwner(), 1000);
				}
			}
			float x = (float)(Math.cos((angle+angleMod) % (Math.PI * 2))*(PPS))*(delta/18f);
			float y = (float)(Math.sin((angle+angleMod) % (Math.PI * 2))*(PPS))*(delta/18f);

			if (wavy) {
				float beta = (float) (angle+angleMod + Math.PI/2);
				wavePos = (double) (amplitude*Math.cos(wp2/frequency));
				float xMod = (float) ((Math.cos(beta)*wavePos));
				float yMod = (float) ((Math.sin(beta)*wavePos));
				x += xMod*(getOwner().getShotIterator() % 2 == 0 ? 1 : -1);;
				y += yMod*(getOwner().getShotIterator() % 2 == 0 ? 1 : -1);
				wp2 += waveIterator;
				
			}
			visualAngle = Math2.calcAngle(getLoc(), new Vector2f(getLoc().x+x,getLoc().y+y));
			if (distTr > momentumDelay) {
				nLoc.set(x, y);
				
				if (pierceObstacles == false) {
					Vector2f cLoc = src;
					if (cLoc.x/16 >= 0 && cLoc.y/16 >= 0 &&MainGame.getCurrentMap().blocked((int)(cLoc.x/16), (int)(cLoc.y/16))) {
						distTr = 0;
						dst = null;
						moving = false;
						return;
					}
				}

				ArrayList<Entity> e = MainGame.getEntityManager().getEntities();//.getAllNearbyEnemies(getOwner(), PPS*32);
				for (int i = e.size()-1; i > -1; i--) {
					Entity en = e.get(i);
					if (en == null || en instanceof Bullet|| en.targetable == false || getOwner().getHitbox() == null || en.getHitbox() == null || getOrigin() == null || getOrigin().getTeam() == en.getTeam()  || en.active == false || en.destroy == true || en.invulnerable == true || en.isInvincible()) 
						continue;
					if (en.getLocation().distance(getOwner().getLocation()) > PPS*128)
						continue;
					if ((pierceEnemies == true && enemiesHit.contains(en)))
						continue;
					if (en.getHitbox().intersects(getOwner().getHitbox())) {
						en.onHitByBullet((Bullet)getOwner());
						if (pierceEnemies == false) {
							distTr = 0;
							dst = null;
							moving = false;
							return;
						} else {
							enemiesHit.add(en);
						}
						continue;
					}
				}
				
				src.add(nLoc);
				if (momentumCap > 0 && distTr < momentumCap) {
					speed+=acceleration;
					speed-=deceleration;	
				}
				if (boomerangs && distTr > bpoint+momentumDelay/2) {
					if (pierceEnemies == true)
						enemiesHit.clear();
					this.angle = (float) (angle-Math.PI);
					//this.angleMod = (float) (-angleMod);
					if (wavy) {
						amplitude = -amplitude;
					}
					homing = false;
					boomerangs = false;
				}
			}
			distTr +=delta;
			if (distTr >= dist && inf == false) {
				distTr = 0;
				dst = null;
				moving = false;
			 }
			if (speed < 0 && negativeSpeed == false)
				speed = 0;
			
		} else {
			return;
		}
	}
	
	public float getDuration() {
		return dist;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getAngle() {
		return angle;
	}

	public Entity getOrigin() {
		return origin;
	}

	public void setOrigin(Entity origin) {
		this.origin = origin;
	}

	public Vector2f getOriginPoint() {
		return originPoint;
	}

	public boolean isHoming() {
		return homing;
	}

	public void setHoming(boolean homing) {
		this.homing = homing;
	}
	
	public float getSine(float mult) {
		if (wavy && amplitude != 0) {
			return (float) (Math.sin(((mult*(2*Math.PI))*frequency)*amplitude));
		}
		return 0;
	}
	
	@Override
	public Bullet getOwner() {
		return (Bullet)super.getOwner();
	}
}
