package com.botifier.timewaster.util.behaviors;

import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.util.Behavior;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;

public class OrbitBehavior extends Behavior {
	public int slice = 32;
	int time = 0;
	float radius = 5;
	double theta = 0;
	double sliceE = 0;
	boolean auto = true;
	boolean teleport = false;
	Entity orbit;
	
	public OrbitBehavior(Enemy owner) {
		super(owner);
	}

	@Override
	public void run() {
		if (orbit != null) {
			Vector2f loc = orbit.getLocation();
			if ( theta >= Math.PI*2)
				theta = 0;
			sliceE = (2*Math.PI)/(this.slice*getOwner().getController().getPPS());
			float rad = (float) ((radius)*Math.sqrt(radius));
			float nx = (int) ((rad*8) * Math.cos(theta));
			float ny = (int) ((rad*8) * Math.sin(theta));			
			if (teleport == false) {
				getOwner().getController().dash(loc.x+nx, loc.y+ny);
				if (getOwner().getPositionRelativeTo(loc).distance(new Vector2f(nx,ny)) < getOwner().getController().getPPS()) {
					if (auto)
						theta += sliceE;
					time = 0;
				}
				time++;
			} else {
				getOwner().getController().teleport(loc.x+nx, loc.y+ny);
			}
		}
	}
	
	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}
	
	public void setAutomatic(boolean auto) {
		this.auto = auto;
	}
	
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setTarget(Entity e) {
		this.orbit = e;
	}
	
	public boolean isTeleport() {
		return teleport;
	}
	
	public boolean isAutomatic() {
		return auto;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public Entity getTarget() {
		return orbit;
	}
}
