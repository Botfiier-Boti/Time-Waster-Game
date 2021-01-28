package com.botifier.timewaster.util.movements;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;

public class EntityController {
	public LinkedList<Vector2f> path = new LinkedList<Vector2f>();
	public Vector2f src;
	public Vector2f dst;
	public Vector2f lDst;
	public float speed;
	public float bonusspeed;
	public float visualAngle;
	public boolean allyCollision = true;
	protected float truespeed;
	protected float PPS;
	protected boolean moving = false;
	protected boolean obeysCollision = true;
	protected Entity owner;
	public Rectangle testBox = new Rectangle(0,0,0,0);
	protected float angle = 0;
	protected float anglemod = 0;
	protected Vector2f lastMove = new Vector2f(0,0);
	protected Vector2f velocity = new Vector2f(0,0);
	protected Vector2f steer = new Vector2f(0,0);
	protected Vector2f desired = new Vector2f(0,0);
	protected float distance;
	protected boolean UP = true, DOWN = true, LEFT = true, RIGHT = true;
	protected boolean fleeing = false;
	
	public EntityController(float x, float y, float speed) {
		src = new Vector2f(x, y);
		this.lDst = src;
		this.speed = Math2.round(speed, 2);
	}
	
	public EntityController(float x, float y, float speed, boolean obeysCollision) {
		src = new Vector2f(x, y);
		this.speed = Math2.round(speed, 2);
		this.obeysCollision = obeysCollision;
	}
	
	public void move(int delta) {
		boolean eC = false, mC = false;
		UP = true; DOWN = true; LEFT = true; RIGHT = true;
		if (dst == null) {
			moving = false;
			return;
		}
		if (allyCollision && !(getOwner() instanceof Player) && getOwner().visible) {
			seperate();
		}
		if (fleeing) {
			fleeArrive();
		} else {
			arrive();
		}
		/*switch (b) {
			default:
				arrive();
				break;
			case FLEEING:
				fleeArrive();
				break;
			case CHASING:
				arrive();
				break;
		}*/
		truespeed = speed+bonusspeed;
		PPS = 0.6f + 1.5f*(truespeed/75f);
		if (dst == null)
			return;
		velocity.add(steer);	
		Vector2f temp = Math2.truncate(velocity,PPS);
		visualAngle = angle;
		angle = Math2.calcAngle(src, dst);
		lDst = src.copy();
		if (path.size() > owner.followers.size()*16)
			path.removeLast();
		if (owner.followers.size() > 0 && moving)
			path.addFirst(lDst);
		else {
			if (path.size() > 0)
				path.clear();
		}
		if (obeysCollision) {
			testMove();
			//eC = testMapCollision(src.copy().add(Math2.truncate(velocity,PPS)));
			//mC = testEntityCollision(dst);
			if (eC || mC) {
				//return;
			}
		}
		if (UP == false && temp.y < 0) {
			temp.y = 0;
		}

		if (DOWN == false && temp.y > 0) {
			temp.y = 0;
		}
		
		if (LEFT == false && temp.x < 0) {
			temp.x = 0;
		}

		if (RIGHT == false && temp.x > 0) {
			temp.x = 0;
		}
		
		if (src.distance(dst) < PPS) {
			if (obeysCollision) {
				if (UP && DOWN && LEFT && RIGHT) {
					temp.x = 0;
					temp.y = 0;
					src = dst;
					//dst = null;
					moving = false;
				} else {
					temp.x = 0;
					temp.y = 0;
					//dst = null;
					moving = false;
				}
			} else {
				temp.x = 0;
				temp.y = 0;
				src = dst;
				dst = null;
				moving = false;
			}
		} 
		if (dst != null) {
			if (dst.x > src.x && RIGHT == false) {
				dst.x = src.x;
				temp.x = 0;
			}
			if (dst.x < src.x && LEFT == false) {
				dst.x = src.x;
				temp.x = 0;
			}
			if (dst.y > src.y && DOWN == false) {
				dst.y = src.y;
				temp.y = 0;
			}
			if (dst.y < src.y && UP == false) {
				dst.y = src.y;
				temp.y = 0;
			}
		}
		if (moving)
			src.add(temp);
		lastMove = temp;
		// make the object "snap" to the destination if it's close enough (to avoid vibrating)
		if (dst != null && Math.round(src.getX()) == Math.round(dst.getX())) {
				src.x = dst.getX();
		}
		if (dst != null && Math.round(src.getY()) == Math.round(dst.getY())) {
				src.y = dst.getY();
		}
		velocity.scale(0);
		steer.scale(0);
		//testBox = null;
		eC = testMapCollision(src.copy().add(Math2.truncate(velocity,PPS)));
		if (dst != null && src.distance(dst) < PPS) {
			moving = false;
		} else if (dst == null) {
			moving = false;
		} else {
			moving = true;
		}
	}
	
	public void stop() {
		dst = null;
		moving = false;
	}
	
	public void arrive() {
		int arriveRadius = 1;
		desired.sub(dst.copy().sub(src));
		distance = desired.length();
		if (distance > 0) {
			float spd = PPS * (distance/arriveRadius);
			spd = Math.min(spd, PPS);
			desired.scale(spd/distance);
			steer.sub(desired.copy().sub(velocity));
			moving = true;
		} else {
			moving = false;
		}
	}
	
	public void fleeArrive() {
		int arriveRadius = 10;
		desired.sub(src.copy().sub(dst));
		distance = desired.length();
		if (distance > 0) {
			float spd = PPS * (distance/arriveRadius);
			spd = Math.min(spd, PPS);
			desired.scale(spd/distance);
			steer.sub(desired.copy().sub(velocity));
			moving = true;
		} else {
			moving = false;
		}
	}
	
	public void seperate() {
		int seperateRadius = (int) (getOwner().collisionbox.getWidth()/4);
		for (int i = MainGame.getEntities().size()-1; i > 0; i--) {
			Entity e = MainGame.getEntities().get(i);
			if (e == getOwner() || e.getController().isMoving() == false || e.team != getOwner().team || e.active == false || e.visible == false || e.destroy == true || e.getController().obeysCollision == false || (getLoc().distance(e.getLocation()) >= seperateRadius && e.getLocation().distance(getLoc()) >= e.collisionbox.getWidth()/4)) {
				continue;
			}
			desired.sub(src.copy().sub(e.getLocation()));
			desired.sub(velocity);
			steer.sub(desired);
		}
	}
	
	public boolean testEntityCollision(Vector2f dst) {
		if (dst == null)
			return false;
		int up = 0, down = 0, left = 0, right = 0;
		ArrayList<Entity> test = MainGame.getEntities();
		Vector2f fake = src.copy();
		fake.add(velocity);
		testBox = new Rectangle(fake.x,fake.y,owner.collisionbox.getWidth(),owner.collisionbox.getHeight());
		//testBox.setBounds(fake.getX()-owner.collisionbox.getWidth()/2,fake.getY()-owner.collisionbox.getHeight(),owner.collisionbox.getWidth(),owner.collisionbox.getHeight());
		for (int i = test.size()-1; i >= 0; i--) {
			Entity en = test.get(i);
			if (en.solid == false || en.obstacle == false || en.destroy == true || en == getOwner())
				continue;
			if (en.getLocation().distance(testBox.getLocation()) <= owner.influence) {
				if (testBox.intersects(en.collisionbox)) {
					if (src.x+velocity.x > fake.x) {
						left++;
					} else if (src.x+velocity.x < fake.x) {
						right++;
					}
					if (src.y+velocity.y < fake.y) {
						up++;
					} else if (src.y+velocity.y > fake.y) {
						down++;
					}
				}
			} else
				continue;
		}
		if (up > 0)
			UP = false;
		if (down > 0)
			DOWN = false;
		if (left > 0)
			LEFT = false;
		if (right > 0)
			RIGHT = false;
		if (up+down+left+right > 0) {
			return true;
		}
		return false;
	}
	
	public boolean testAllEntityCollision(Vector2f dst) {
		if (dst == null)
			return false;
		ArrayList<Entity> test = MainGame.getEntities();
		float angle = Math2.calcAngle(src,dst);
		Vector2f spd = new Vector2f(0, 0);
		Vector2f desired_velocity = dst.copy().sub(getLoc()).getNormal();
		Vector2f fake = src.copy();
		// make the object "snap" to the destination if it's close enough (to avoid vibrating)
		if (Math.round(src.getX()) == Math.round(dst.getX())) {
			src.x = dst.getX();
		}
		if (Math.round(src.getY()) == Math.round(dst.getY())) {
			src.y = dst.getY();
		}
		// move the object
		if (src.getX() != dst.getX())
			spd.x = Math2.round((float) (Math.cos(angle)*(PPS)), 1);
		if (src.getY() != dst.getY())
			spd.y = Math2.round((float) (Math.sin(angle)*(PPS)), 1);

		Vector2f steering = Math2.truncate(desired_velocity.sub(spd), PPS*2);
		steering.x /= 2;
		steering.y /= 2;
				
		this.velocity = spd;
		fake.add(Math2.truncate(spd.add(steering), PPS));
		testBox =  new Rectangle(fake.getX()-owner.collisionbox.getWidth()/2,fake.getY()-owner.collisionbox.getHeight(),owner.collisionbox.getWidth(),owner.collisionbox.getHeight());
		for (int i = test.size()-1; i >= 0; i--) {
			Entity en = test.get(i);
			if (en == getOwner())
				continue;
			if (en.getLocation().distance(testBox.getLocation()) <= owner.influence) {
				if (testBox.intersects(en.collisionbox)) {
					return true;
				}
			} else
				continue;
		}
		return false;
	}
	
	public void testMove() {
		MainGame m = MainGame.mm;
		if ((((int)(getOwner().collisionbox.getMinX()-PPS)/16 >= 0) && ((int)(getOwner().collisionbox.getMaxX()+PPS)/16 < MainGame.mm.m.getWidthInTiles())) 
				&& (((int)(getOwner().collisionbox.getMinY()-PPS)/16 >= 0) && ((int)(getOwner().collisionbox.getMaxY()+PPS)/16 < MainGame.mm.m.getHeightInTiles()))) {
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMaxX())/16, (int)(getOwner().collisionbox.getMinY()-PPS)/16) == true)
				{
					UP = false;
				}
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMinX())/16, (int)(getOwner().collisionbox.getMinY()-PPS)/16) == true)
				{
					UP = false;
				}
				
				//DOWN
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMaxX())/16, (int)(getOwner().collisionbox.getMaxY()+PPS)/16) == true)
				{
					DOWN = false;
				}
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMinX())/16, (int)(getOwner().collisionbox.getMaxY()+PPS)/16) == true)
				{
					DOWN = false;
				}
				//LEFT
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMinX()-PPS)/16, (int)(getOwner().collisionbox.getMaxY())/16) == true)
				{
					LEFT = false;
				}
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMinX()-PPS)/16, (int)(getOwner().collisionbox.getMinY())/16) == true)
				{
					LEFT = false;
				}
				
				//RIGHT
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMaxX()+PPS)/16, (int)(getOwner().collisionbox.getMaxY())/16) == true)
				{
					RIGHT = false;
				} 
				if (m.m.blocked(null, (int)(getOwner().collisionbox.getMaxX()+PPS)/16, (int)(getOwner().collisionbox.getMinY())/16) == true)
				{
					RIGHT = false;
				} 
			}
		/*if (UP == false &&  dst.y < src.y)
			dst.y = src.y;
		if (DOWN == false &&  dst.y > src.y)
			dst.y = src.y;
		if (LEFT == false &&  dst.x < src.x)
			dst.y = src.y;
		if (RIGHT == false &&  dst.x > src.x)
			dst.y = src.y;*/
		//testEntityCollision(dst);
	}
	
	public boolean testMapCollision(Vector2f dst) {
		Vector2f fake = new Vector2f(src.x, src.y);
		testBox = new Rectangle(fake.x-owner.collisionbox.getWidth()/2,fake.y-owner.collisionbox.getHeight()/2,owner.collisionbox.getWidth(),owner.collisionbox.getHeight());
		int x = (int)dst.x/16;
		int y = (int)dst.y/16;
		boolean blocked = MainGame.mm.m.blocked(testBox, x, y);
		if (blocked == true) {
			float angle = (int) Math.toDegrees(this.angle);
			if (((angle >= 180 && angle <= 360))) {
				//float disty = (y*16)-(src.y);
				dst.y = src.y;
				UP = false;
			} else if ((angle <= 180 && angle >= 0)) {
				//float disty = (src.y)-(y*16);
				dst.y = src.y;
				DOWN = false;
			}
			if ((angle >= 90 && angle <= 270)) {
				dst.x = src.x;
				LEFT = false;
			}
			else if ((angle <=90 || angle >=270))
				RIGHT = false;
			if (UP && DOWN && LEFT && RIGHT)
				System.out.println("Error");
		}
		return blocked;
	}
	
	public boolean isBlocked() {
		return UP && DOWN && LEFT && RIGHT ? false : true;
	}
	
	public Vector2f getLocInTiles() {
		Vector2f tileloc = new Vector2f();
		tileloc.x = (int)src.x/16;
		tileloc.y = (int)src.y/16;
		return tileloc;
	}
	
	public Vector2f getLoc() {
		return src;
	}
	
	public Vector2f getDst() {
		return dst;
	}
	
	public Vector2f getDir() {
		if (lastMove != null)
			return lastMove.copy();
		return null;
	}
	
	public float getPPS() {
		return PPS;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public void teleport(float x, float y) {
		if (x == src.getX() && y == src.getY())
			return;
		src.x = x;
		src.y = y;
		dst = null;
	}
	
	public void teleport(Vector2f loc) {
		if (loc.x == src.getX() && loc.y == src.getY())
			return;
		src.x = loc.x;
		src.y = loc.y;
		dst = null;
	}
	
	public void setDestination(float x, float y) {
		if (x == src.getX() && y == src.getY())
			return;
		dst = new Vector2f(Math2.round(x, 2), Math2.round(y, 2));
		moving = true;
	}
	
	public void setCollision(boolean c) {
		this.obeysCollision = c;
	}
	
	public boolean obeysCollision() {
		return this.obeysCollision;
	}
	
	public boolean isMoving() {
		return moving;
	}

	public Entity getOwner() {
		return owner;
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}
	
	public EntityController copy() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		
		Constructor<? extends EntityController> c = null;
		try {
			c = getClass().getConstructor(float.class, float.class, float.class, boolean.class);
			return c.newInstance(src.x,src.y,speed,obeysCollision);
		} catch (NoSuchMethodException e) {
			try {
				c = getClass().getConstructor(float.class, float.class, float.class);
				return c.newInstance(src.x,src.y,speed);
			} catch (NoSuchMethodException e1) {
				return null;
			}
		}
	}
}
