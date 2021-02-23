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
		if ((getOwner().getLocation().x != (MainGame.getCurrentMap().getWidthInTiles()*16)/2) || (getOwner().getLocation().y != (MainGame.getCurrentMap().getHeightInTiles()*16)/2)) {
			isCentered = false;
			getOwner().getController().dash((MainGame.getCurrentMap().getWidthInTiles()*16)/2, (MainGame.getCurrentMap().getHeightInTiles()*16)/2);
		} else if (MainGame.getCurrentMap().blocked((MainGame.getCurrentMap().getWidthInTiles()*16)/2, (MainGame.getCurrentMap().getHeightInTiles()*16)/2)) {
			isCentered = true;
			getOwner().getController().stop();
		}else {
			isCentered = true;
			getOwner().getController().stop();
		}
	}
	
	public boolean isCentered() {
		if ((getOwner().getLocation().x != (MainGame.getCurrentMap().getWidthInTiles()*16)/2) || (getOwner().getLocation().y != (MainGame.getCurrentMap().getHeightInTiles()*16)/2))
			isCentered = false;
		return isCentered;
	}
	
	public void reset() {
		isCentered = false;
	}

}
