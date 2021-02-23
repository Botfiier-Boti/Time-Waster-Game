package com.botifier.timewaster.util.movements;



public class TestEntityController extends EntityController {
	float wanderAngle = 0;
	public TestEntityController(float x, float y) {//, float speed) {
		super(x, y);
		obeysCollision = false;
	}
	
	/*@Override
	public void move(int delta) {
		if (dst == null)
			return;
		PPS = 0.6f + 1.5f*(speed/75f);
		arrive();
		velocity.add(steer);
		src.add(Math2.truncate(velocity,PPS));
		angle = (float) Math.toRadians(velocity.getTheta());
		velocity.scale(0);
		steer.scale(0);
	}
	public void fleeArrive() {
		int arriveRadius = 5;
		desired.sub(src.copy().sub(dst));
		distance = desired.length()/2;
		if (distance > 0) {
			float spd = PPS * (distance/arriveRadius);
			spd = Math.min(spd, PPS);
			desired.scale(spd/distance);
			steer.sub(desired.copy().sub(velocity));
		}
	}
	public void seek() {
		desired.sub(dst.copy().sub(src));
		desired.normalise();
		desired.scale(PPS);
		steer.sub(desired.copy().sub(velocity));
		steer = Math2.truncate(steer, PPS);
		steer.scale(PPS);
	}
	
	public void flee() {
		desired.sub(src.copy().sub(dst));
		desired.normalise();
		desired.scale(PPS);
		steer.sub(desired.copy().sub(velocity));
		steer = Math2.truncate(steer, PPS);
		steer.scale(PPS);
	}
	
	public void arrive() {
		int arriveRadius = 10;
		desired.sub(dst.copy().sub(src));
		distance = desired.length();
		if (distance > 0) {
			float spd = PPS * (distance/arriveRadius);
			spd = Math.min(spd, PPS);
			desired.scale(spd/distance);
			steer.sub(desired.copy().sub(velocity));
		}
	}
	*/

}
