package com.botifier.timewaster.entity.projectile;


import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.entity.BeeSwarm;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.LobbedProjectile;
import com.botifier.timewaster.util.movements.LobbedController;

public class Beehive extends LobbedProjectile{
	public int swarmSize = 5;
	
	public Beehive(float x, float y, Vector2f dst, Entity o) {
		super("Beehive", MainGame.getImage("beehive"), new LobbedController(x,y,500,dst,o), dst, o, 50);
		if (dst.x < x)
			setRotation(-getRotation());
	}
	
	@Override
	public void update(int delta) throws SlickException {
		super.update(delta);
	}

	@Override
	public void onLand() throws SlickException {
		float x = getLocation().getX();
		float y =  getLocation().getY();
		if (x < 0)
			return;
		if (x > MainGame.getCurrentMap().getWidthInTiles()*16)
			return;
		if (y < 0)
			return;
		if (y > MainGame.getCurrentMap().getHeightInTiles()*16)
			return;
		if (!MainGame.getCurrentMap().blocked(null, (int)x/16, (int)y/16)) {
			BeeSwarm isc = new BeeSwarm(x, y, swarmSize, getOwner());
			getOwner().addSpawn(isc);	
		}
	}

}
