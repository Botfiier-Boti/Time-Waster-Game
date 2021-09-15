package com.botifier.timewaster.entity.projectile;

import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.LobbedProjectile;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.bulletpatterns.RockShatterPattern;
import com.botifier.timewaster.util.movements.LobbedController;

public class Rock extends LobbedProjectile {
	Entity cls = null;
	Random r = new Random();
	float rotate = 3;
	RockShatterPattern rsp;

	public Rock(float x, float y, Vector2f dst, Entity o) {
		super("Rock", MainGame.getImage("rock"), new LobbedController(x,y,1000,dst,o), dst, o,50);
		if (dst.x < x)
			rotate = -rotate;
		iModifier = 5f;
		rsp = new RockShatterPattern();
		visible = false;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		rotation+=rotate;
	}
	
	@Override
	public void onLand() throws SlickException {
		cls = MainGame.getEntityManager().findClosestEnemy(this, 500);
		if (seen) {

			if (cls == null) {
				rsp.fire(this, this.getLocation().getX(),  this.getLocation().getY(), (float)Math.toRadians(r.nextInt(360)));
			} else {
				float angle = Math2.calcAngle(getLocation(),cls.getLocation());
				rsp.fire(this, this.getLocation().getX(),  this.getLocation().getY(), angle);
			}
		}
		destroy = true;
	}

}
