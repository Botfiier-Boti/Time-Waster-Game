package com.botifier.timewaster.entity;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.behaviors.OrbitBehavior;
import com.botifier.timewaster.util.movements.EnemyController;

//Mouse following thing
public class MouseFollower extends Enemy {

	float angleMod = 0;
	
	public MouseFollower(float x, float y, float angleMod, Entity owner) {
		super("Follower", MainGame.getImage("beehive"), new EnemyController(x, y, 3f, 50), null, null);visible = true;
		autoFlip = false;
		iModifier = 0.1f;
		invulnerable = true;
		healthbarVisible = false;
		o = owner;
		getStats().setAttack(o.getAttack());
		getStats().setSpeed(500);
		OrbitBehavior b = new OrbitBehavior(this);
		b.setRadius(1);
		b.setTarget(o);
		b.setAutomatic(false);
		b.setTeleport(true);
		behaviors.add(b);
		currentBehavior = 0;
		this.angleMod = angleMod;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		Input i = MainGame.mm.getInput();
		if (getOwner() == null) {
			destroy = true;
			return;
		}

		Vector2f mouse = new Vector2f(i.getMouseX(), i.getMouseY());
		angle = Math2.calcAngle(getOwner().getController().getLoc(), mouse);
		((OrbitBehavior)behaviors.get(0)).setTheta(angle+Math.toRadians(angleMod));
		
		if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			this.shootBullet(angle);
		}
	}
}
