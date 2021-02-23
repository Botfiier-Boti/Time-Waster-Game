package com.botifier.timewaster.util.movements;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;

public class LobbedController extends EntityController {
	public Vector2f nextPos;
	Vector2f originPoint;
	float speed;
	float dx = 0;
	float dy = 0;
	long d = 0;
	long dist = 0;
	long duration = 0;
	public LobbedController(float x, float y, long duration, Vector2f dst, Entity origin) {
		super(x, y);
		this.duration = duration;
		this.d = duration;
		this.dst = dst;
		this.originPoint = new Vector2f(x,y);
		this.nextPos = originPoint.copy();
		this.dx=(dst.x-getLoc().x)/getTimeLeft();
		this.dy =(dst.y-getLoc().y)/getTimeLeft();
		angle = Math2.calcAngle(getLoc(), dst);
		dist = (long) (dst.distance(getLoc()));
		speed = 60*dist/duration;
		obeysCollision = false;
	}
	
	@Override
	public void move(int delta) {
		d = getTimeLeft() - delta;
		if (dst == null || getOriginPoint() == null || getTimeLeft() <= 0) {
			try {
				getOwner().onDeath();
			} catch (SlickException e) {
				e.printStackTrace();
			}
			dst = null;
			moving = false;
			return;
		}
		getLoc().x = nextPos.x;
		getLoc().y = nextPos.y;
		if (getLoc().distance(dst) > 0.01f) {
			nextPos = getLoc().copy();
			nextPos.x += dx*delta;
			nextPos.y += dy*delta;	
		}
	}
	
	public long getTimeElapsed() {
		return getDuration()-getTimeLeft();
	}
	
	public long getDuration() {
		return duration;
	}

	public long getTimeLeft() {
		return d;
	}
	
	public Vector2f getOriginPoint() {
		return originPoint;
	}
}
