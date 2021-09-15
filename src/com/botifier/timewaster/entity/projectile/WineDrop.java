package com.botifier.timewaster.entity.projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.LobbedProjectile;
import com.botifier.timewaster.util.movements.LobbedController;

public class WineDrop extends LobbedProjectile {

	public WineDrop(float x, float y, Vector2f dst, Entity o) {
		super("Red Drop", MainGame.getImage("RedDrop"), new LobbedController(x,y,500,dst,o), dst, o,20);
		hasshadow = true;
		flip = true;
		dir = true;
		setRotation(0);
	}
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
		if (getController().getTimeLeft() > getController().getDuration()/2) {
			flip = true;
			dir = true;
		} else {
			flip = false;
			dir = false;
		}
	}

	@Override
	public void onLand() throws SlickException {

		Circle c = new Circle(getLocation().x, getLocation().y, 5);
		MainGame.spawnTempEffect(c, 100, Color.red);
		for (int i = MainGame.getEntities().size()-1; i > -1; i--) {
			Entity en = MainGame.getEntities().get(i);
			if (en instanceof Bullet || en.isInvincible() || en == this || en.getTeam() == getTeam() || en.invulnerable == true || en.active == false || en.visible == false || getLocation().distance(en.getLocation()) > 5)
				continue;
			en.onHit(15, this, true);
		}
	}

}
