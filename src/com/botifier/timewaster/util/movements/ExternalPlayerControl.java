package com.botifier.timewaster.util.movements;

import org.newdawn.slick.geom.Vector2f;

//For later
public class ExternalPlayerControl extends EntityController {
	boolean UP, DOWN, LEFT, RIGHT;
	
	public ExternalPlayerControl(float x, float y) {
		super(x, y);
	}

	public boolean getFiring() {
		return false;
	}
	
	public Vector2f getControl() {
		return new Vector2f(0,0);
	}


}
