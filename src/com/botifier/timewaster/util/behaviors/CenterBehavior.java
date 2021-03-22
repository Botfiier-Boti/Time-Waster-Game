package com.botifier.timewaster.util.behaviors;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Behavior;
import com.botifier.timewaster.util.Enemy;

public class CenterBehavior extends Behavior {
	private boolean isCentered = false;

	public CenterBehavior(Enemy o) {
		super(o);
	}

	@Override
	public void run() {
		if ((getOwner().getLocation().x != (MainGame.getCurrentMap().getCenter().x) || (getOwner().getLocation().y != (MainGame.getCurrentMap().getCenter().y)))) {
			isCentered = false;
			getOwner().getController().dash(MainGame.getCurrentMap().getCenter().x, MainGame.getCurrentMap().getCenter().y);
		} else if (MainGame.getCurrentMap().blocked((int)MainGame.getCurrentMap().getCenter().x, (int)MainGame.getCurrentMap().getCenter().y)) {
			isCentered = true;
			getOwner().getController().stop();
		}else {
			isCentered = true;
			getOwner().getController().stop();
		}
	}
	
	public boolean isCentered() {
		if ((getOwner().getLocation().x != (MainGame.getCurrentMap().getCenter().x) || (getOwner().getLocation().y != (MainGame.getCurrentMap().getCenter().y))))
			isCentered = false;
		return isCentered;
	}
	
	public void reset() {
		isCentered = false;
	}

}
