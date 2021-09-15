package com.botifier.timewaster.entity;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.behaviors.OrbitBehavior;
import com.botifier.timewaster.util.bulletpatterns.BulletPattern;
import com.botifier.timewaster.util.movements.EnemyController;

//Mouse following thing
public class MouseFollower extends Enemy {

	BulletPattern current = null;
	float angleMod = 0;
	
	public MouseFollower(float x, float y, float angleMod, Entity owner) {
		super("Follower", MainGame.getImage("beehive"), new EnemyController(x, y, 3f, 50), null, null);visible = true;
		autoFlip = false;
		iModifier = 0.1f;
		invulnerable = true;
		healthbarVisible = false;
		targetable = false;
		getController().allyCollision = false;
		setOwner(owner);
		getStats().setAttack(getOwner().getAttack());
		getStats().setSpeed(500);
		OrbitBehavior b = new OrbitBehavior(this);
		b.setRadius(1);
		b.setTarget(getOwner());
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
		setAngle(Math2.calcAngle(getOwner().getController().getLoc(), mouse));
		((OrbitBehavior)behaviors.get(0)).setTheta(getAngle()+Math.toRadians(angleMod));
		if (getOwner() instanceof Player) {
			Player p = (Player)getOwner();
			current = p.p;	
		}

		getStats().setDexterity(getOwner().getDexterity()/2);
		if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) || (getOwner() instanceof Player && ((Player)getOwner()).autofire == true)) {
			float SPS = 1.5f + 6.5f*((getDexterity())/75f);;
			if (current == null) {
				this.shootBullet(getAngle());	
			} else {
				if (cooldown <= 0) {
					if (current.lob == true) {
						current.fire(getOwner(), mouse.x, mouse.y, getAngle());
					} else if (current.targeted == true) {
						current.fire(getOwner(), getLocation().x, getLocation().y, getAngle());//, ((OverworldState)MainGame.mm.getState(1)).mtrack);
					}else {
						current.fire(getOwner(), getLocation().x, getLocation().y, getAngle());
					}
					cooldown = 3000/SPS;
				}
			}
			
		}
	}
	
	@Override
	public boolean shootBullet(float angle, boolean force) throws SlickException {
		if (firing == false) {
			firing = true;	
		}
		if (cooldown <= 0 || force) {
			getOwner().addBullet(new Bullet("Bob", getController().getLoc().x, getController().getLoc().y, 200, angle, 350, 75, 90,this));
			float SPS = 1.5f + 6.5f*((getDexterity())/75f);
			cooldown = 1000/SPS;
			firing = false;
			return true;
		}
		return false;
	}
}
