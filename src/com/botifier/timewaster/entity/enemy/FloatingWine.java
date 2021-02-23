package com.botifier.timewaster.entity.enemy;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;

import com.botifier.timewaster.entity.projectile.WineDrop;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.behaviors.FollowBehavior;
import com.botifier.timewaster.util.movements.EnemyController;

public class FloatingWine extends Enemy {

	Random r = new Random();
	long cooldown = 20;

	public FloatingWine(float x, float y) {
		super("Floating Wine", MainGame.getImage("WineGlass"), new EnemyController(x,y, 1f, 100), new SpriteSheet(MainGame.getImage("FloatingWine"),16,16), new SpriteSheet(MainGame.getImage("WineAttack"),16,16),2);
		size = 0.75f;
		iModifier = 0.20f;
		healthbarVisible = false;
		FollowBehavior fb = new FollowBehavior(this);
		fb.setMaxDistance(10);
		behaviors.add(fb);
		getStats().setSpeed(5);
		currentBehavior = 0;
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		//Find closest target that is farther than 5 pixels
		cls = null;
		for (int i = MainGame.getEntities().size()-1; i > -1; i--) {
			Entity en = MainGame.getEntities().get(i);
			if (en instanceof Bullet || en.isInvincible() || en.team == this.team || en == this|| en.invulnerable == true || en.active == false || en.visible == false || getLocation().distance(en.getLocation()) > influence)
				continue;
			if (cls == null) {
				if (en.getLocation().distance(getLocation()) > 5); 
					cls = en;
			}
			if (getLocation().distance(en.getLocation()) < getLocation().distance(cls.getLocation())) {
				if (en.getLocation().distance(getLocation()) > 5); 
					cls = en;
			}
		}
		if (cls != null && cls.getLocation().distance(getLocation()) > 5) {
			//Follow target
			FollowBehavior fb = (FollowBehavior)behaviors.get(0);

			fb.setTarget(cls);
			if (getController().isMoving()) {
				if (cooldown <= 0 ) {
					//Shoot 'drop' behind 
					float x = this.getLocation().x-10*(float)Math.cos(-angle)*(getController().getPPS());
					float y =  this.getLocation().y+10*(float)Math.sin(-angle)*(getController().getPPS());
					Circle c = new Circle(x, y, 5);
					MainGame.spawnTempEffect(c, 25, Color.red);
					for (int i = MainGame.getEntities().size()-1; i > -1; i--) {
						Entity en = MainGame.getEntities().get(i);
						if (en instanceof Bullet || en.isInvincible() || en == this || en.team == team || en.invulnerable == true || en.active == false || en.visible == false || getLocation().distance(en.getLocation()) > 5)
							continue;
						en.onHit(15, this, true);
					}
					cooldown = 20;
				}
			}
		} else {
			//Stop moving when there is no target
			attacking = false;
			getController().stop();
		}
		cooldown--;
	}
	
	@Override
	public void onHit(int damage, Entity origin, boolean ignoresDefense) {
		super.onHit(damage, origin, ignoresDefense);
		//Retaliate
		b.add(new WineDrop(this.getLocation().x, this.getLocation().y,  origin.getLocation().copy(), this));
		playAttackAnimation(20);
	}
	
}
