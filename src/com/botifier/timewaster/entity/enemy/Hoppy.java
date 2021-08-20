package com.botifier.timewaster.entity.enemy;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Enemy;
import com.botifier.timewaster.util.movements.EnemyController;

public class Hoppy extends Enemy {
	Vector2f start;
	Vector2f dst;
	long timeElapsed = 0;
	SpriteSheet hops;
	Animation hop;
	Curve c;

	public Hoppy(float x, float y) {
		super("Hoppy", MainGame.getImage("HoppyIdle"), new EnemyController(x, y, 0.5f, 10), null, null);
		hops = new SpriteSheet(MainGame.getImage("Hoppy"), 16, 16);
		hop = new Animation(hops, 1);
		getController().allyCollision = false;
		getController().setCollision(false);
		getController().outside = false;
		this.healthbarVisible = false;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (getController().wander(false, 0.25f)) {
			start = getController().getLoc().copy();
			dst = getController().dst;
			int tdist = (int) getController().getLoc().distance(dst);	
			float height =   (int) start.distance(dst)/2;
			Vector2f p1 = start.copy();
			p1.x += (start.x-dst.x)/(tdist/getController().getPPS())*0.25f;
			p1.y += (start.y-dst.y)/(tdist/getController().getPPS())*0.25f-height;
			Vector2f p2 = dst.copy();
			p2.x += (start.x-dst.x)/(tdist/getController().getPPS())*0.75f;
			p2.y += (start.y-dst.y)/(tdist/getController().getPPS())*0.75f-height;
			c = new Curve(start, p1, p2, getController().dst, (int) (tdist/getController().getPPS()));
			timeElapsed = 0;
			posMod.y = 0;
		}
		if (c != null && start != null && dst != null && timeElapsed < (start.distance(dst)/getController().getPPS())) {
			int tdist = (int) start.distance(dst);	
			posMod.y = getLocation().y-c.pointAt((float)timeElapsed/(tdist/getController().getPPS())).y;
			if (posMod.y < 0)
				posMod.y = 0;
			if (timeElapsed < (tdist/getController().getPPS())) {
				timeElapsed++;
				getController().cooldown = 50;
			}
			getController().setCollision(false);
			/*if (getController().getTimeLeft() > getController().getDuration()/2) {
				posMod.y -= 1;
			} else {
				posMod.y += 1;
			}*/
		}
		if (dst == null || start == null || timeElapsed >= (start.distance(dst)/getController().getPPS())) {
			if (posMod.y > 0)
				posMod.y-=posMod.y*0.25f;
			//sgetController().cooldown = 0;
			dst = null;
			getController().setCollision(true);
		}
		if (getController().isMoving())
			setCurrentImage(hop.getImage(0));
		else
			setCurrentImage(hop.getImage(1));
	}
	

}
