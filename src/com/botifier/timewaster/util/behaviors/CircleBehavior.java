package com.botifier.timewaster.util.behaviors;

import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.Behavior;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Math2;

public class CircleBehavior extends Behavior {
	public boolean started = false;
	public boolean direction = false;
	public int slice = 32;
	Vector2f circlePos;
	Vector2f lastCirclePos;
	float radius = 5;
	double theta = 0;
	double sliceE = 0;
	
	public CircleBehavior(Enemy owner) {
		super(owner);
	}
	
	public CircleBehavior(Enemy owner, double theta) {
		super(owner);
		setTheta(theta);
	}

	@Override
	public void run() {
		if (radius < 0)
			radius = 0;
		if (circlePos != null) {
			if ( theta >= Math.PI*2)
				theta = 0;
			sliceE = (2*Math.PI)/(this.slice*getOwner().getController().getPPS());
			float rad = (float) ((radius)*Math.sqrt(radius));
			float nx = (int) (circlePos.x + (rad*8) * Math.cos(theta));
			float ny = (int) (circlePos.y + (rad*8) * Math.sin(theta));
			getOwner().getController().dash(nx, ny);
			if (getOwner().getLocation().distance(new Vector2f(nx,ny)) < getOwner().getController().getPPS() || (getOwner().getController().obeysCollision() == true && getOwner().getController().isBlocked())) {
				theta += sliceE*(direction == false ? 1 : -1);
				nx = (int) (circlePos.x + (rad*8) * Math.cos(theta));
				ny = (int) (circlePos.y + (rad*8) * Math.sin(theta));
				getOwner().getController().dash(nx, ny);
				lastCirclePos = circlePos;
			}
			if (theta > 2*Math.PI)
				theta = 0;
		} else {
			getOwner().getController().stop();
		}
	}

	public void setCirclePos(Vector2f circlePos) {
		this.circlePos = circlePos;
		if (started == false ) {
			started = true;
			this.theta = Math2.calcAngle(circlePos,getOwner().getLocation());
		} 
	}
	
	public void setCirclePos(float x, float y) {
		this.circlePos = new Vector2f(x, y);
		if (started == false ) {
			started = true;
			this.theta = Math2.calcAngle(circlePos,getOwner().getLocation());
		}
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setTheta(double theta) {
		if (started == false)
			started = true;
		this.theta = theta;
	}
	
	public Vector2f getCirclePos() {
		return circlePos.copy();
	}
	
	public float getRadius() {
		return radius;
	}
	
	public double getTheta() {
		return theta;
	}
}
