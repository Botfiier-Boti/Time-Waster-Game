package com.botifier.timewaster.entity.projectile;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.LobbedProjectile;
import com.botifier.timewaster.util.bulletpatterns.RockShatterPattern;
import com.botifier.timewaster.util.movements.LobbedController;

public class Hopper extends LobbedProjectile {
	private final int MAX_JUMPS = 5;
	int jumps = 0;
	RockShatterPattern sp;

	public Hopper(float x, float y, Vector2f dst, Entity o) {
		super("Hopper", MainGame.getImage("Rock"), new LobbedController(x,y,1000,dst,o), dst, o,30);
		sp = new RockShatterPattern();
		rotation = 5;
		
	}
	
	private Hopper(float x, float y, Vector2f dst, Entity o, float maxHeight, long duration) {
		super("Hopper", MainGame.getImage("Rock"), new LobbedController(x,y,duration,dst,o), dst, o,maxHeight);
		sp = new RockShatterPattern();
		rotation = 5;
	}

	@Override
	public void onLand() throws SlickException {
		if (getOwner() == null || getOwner().destroy == true || jumps > MAX_JUMPS)
			return;
		
		Vector2f next = getLocation().copy();
		next.x += Math.cos(getController().getAngle())*(30-jumps);
		next.y += Math.sin(getController().getAngle())*(30-jumps);
		Hopper h = new Hopper(getLocation().x,getLocation().y,next,getOwner(), maxHeight*0.75f, 1000-jumps*100);
		h.jumps = jumps+1;
		h.setRotation(getRotation());
		if (jumps < MAX_JUMPS-1)
			MainGame.getEntityManager().addBullet(h);
		Circle c = new Circle(getLocation().x,getLocation().y, 5);
		for (int i = MainGame.getEntities().size()-1; i > -1; i--) {
			Entity en = MainGame.getEntities().get(i);
			if (en instanceof Bullet || en.isInvincible() || en == this || en.getTeam() == getTeam() || en.invulnerable == true || en.active == false || en.visible == false || getLocation().distance(en.getLocation()) > 20)
				continue;
			if (en.getHitbox().intersects(c))
				en.onHit(175, this, true);
		}
		
	}

}
