package com.botifier.timewaster.util.movements;

import org.newdawn.slick.geom.Vector2f;

public class SimpleEntityController extends EntityController {

	public SimpleEntityController(float x, float y) {
		super(x, y);
	}

	public SimpleEntityController(float x, float y, boolean obeysCollision) {
		super(x, y, obeysCollision);
	}
	
	@Override
	public void move(int delta) {
		src.add(new Vector2f((src.x-dst.y)*getPPS(), (src.y-dst.y)*getPPS()));
	}

}
