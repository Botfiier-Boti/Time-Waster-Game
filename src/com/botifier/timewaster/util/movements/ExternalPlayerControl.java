package com.botifier.timewaster.util.movements;

import org.newdawn.slick.geom.Vector2f;

//For later
public class ExternalPlayerControl extends EntityController {
	boolean UP, DOWN, LEFT, RIGHT;
	
	public ExternalPlayerControl(float x, float y, float speed) {
		super(x, y, speed);
	}
	
	public void control(float nx, float ny) {
		PPS = 0.6f + 1.5f*(speed/75f);
		if (UP)
			ny -= PPS;
		if (DOWN)
			ny += PPS;
		if (LEFT)
			nx -= PPS;
		if (RIGHT)
			nx += PPS;
		if (nx > src.getX() || nx < src.getX() || ny > src.getY() || ny < src.getY()) {
			dst = null;
			setDestination(nx, ny); 
		}
	}
	
	public boolean getFiring() {
		return false;
	}
	
	public Vector2f getControl() {
		return new Vector2f(0,0);
	}


}
