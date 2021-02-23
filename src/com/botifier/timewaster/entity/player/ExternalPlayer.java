package com.botifier.timewaster.entity.player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
//import com.botifier.timewaster.entity.ShotgunPattern;
import com.botifier.timewaster.util.Entity;
import com.botifier.timewaster.util.Math2;
import com.botifier.timewaster.util.Team;
import com.botifier.timewaster.util.bulletpatterns.BulletPattern;
import com.botifier.timewaster.util.movements.ExternalPlayerControl;
import com.botifier.timewaster.util.movements.LocalPlayerControl;

public class ExternalPlayer extends Entity {
	Sound s;
	public boolean build = false;
	public int dex = 50;
	float SPS = 0;
	float cooldown = 0;
	public long invulPeriod = 0;
	BulletPattern p;
	
	public ExternalPlayer(String name, float x, float y) throws SlickException {
		super(name, MainGame.getImage("debugman"), new ExternalPlayerControl(x, y));
		s = new Sound("bladeSwing.wav");
		getController().setCollision(true);
		team = Team.ALLY;
		overrideMove = true;
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		Input i = gc.getInput();
		if (getStats().getCurrentHealth() <= 0) {
			active = false;
		}
		if (this.active) {
			SPS = 1.5f + 6.5f*(dex/75f);
			if (p != null)
				SPS *= p.getFireSpeed();
			getController().control(i);
			getController().move(delta);
			
			hitbox.setCenterX(getLocation().getX());
			hitbox.setY(getLocation().getY()-hitbox.getHeight());
			collisionbox.setCenterX(getLocation().getX());
			collisionbox.setY(getLocation().getY()-collisionbox.getHeight()+2);
			/*if(i.isKeyPressed(Input.KEY_B)) {
				build = !build;
			}*/
			if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && build == false) {
				if (cooldown <= 0) {
					Vector2f mouse = new Vector2f(i.getMouseX(), i.getMouseY());
					float angle = Math2.calcAngle(getController().getLoc(), mouse);
					if (p == null) {
						b.add(new Bullet("Bob", getController().getLoc().x, getController().getLoc().y, 100, angle, 25, 45, 90,this));
					} else {
						p.fire(this, getController().getLoc().x, getController().getLoc().y, angle);
					}
					if ((angle < Math.PI && angle > Math.PI/2) || (angle > -Math.PI && angle < -Math.PI/2)) {
						dir = false;
					} else {
						dir = true;
					}
					s.play(1, 0.5f);
					cooldown = 60/SPS;
				}
			}
			cooldown--;
		}
		//JOptionPane.
	}

	@Override
	public LocalPlayerControl getController() {
		return (LocalPlayerControl)getController();
	}
}
