package com.botifier.timewaster.util.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//import java.util.Random;
import java.util.Map.Entry;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

import com.botifier.timewaster.entity.Effect;
import com.botifier.timewaster.entity.PopupText;
import com.botifier.timewaster.entity.TestChest;
import com.botifier.timewaster.entity.enemy.BigGoblin;
import com.botifier.timewaster.entity.enemy.BulletSpawner;
import com.botifier.timewaster.entity.enemy.Sheep;
import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Camera;
import com.botifier.timewaster.util.Entity;

public class EntityManager {
	public static HashMap<String, Class<?>[]> entityClasses = new HashMap<String, Class<?>[]>();
	public static HashMap<String, String> entityAliases = new HashMap<String, String>();
	@SuppressWarnings("unused")
	private MainGame m;
	private Image shadowImage;
	//private Random r = new Random();
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> bullets = new ArrayList<Entity>(200000);
	private ArrayList<Entity> visualEffects = new ArrayList<Entity>();
	private ArrayList<Entity> overlaidEffects = new ArrayList<Entity>();

	public EntityManager(MainGame m) {
		this.m = m;
	}
	
	public void init() throws SlickException {
		shadowImage = MainGame.getImage("Shadow");
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		for (int i = visualEffects.size()-1; i > -1; i--) {
			Entity en = visualEffects.get(i);
			if (en == null)
				continue;
			en.update(delta);
			if (en.destroy == true) {
				visualEffects.remove(en);
				visualEffects.trimToSize();
				continue;
			}
		}
		for (int ie = entities.size() - 1; ie >= 0; ie--) {
			Entity en = entities.get(ie);
			if (en == null)
				continue;
			if (en.destroy == true) {
				entities.remove(en);
				entities.trimToSize();
				continue;
			}

			/*if (en instanceof Bob) {
				Bob b = (Bob) en;
				if (b.p == null) {
					aspf = new AStarPathFinder(m, 1000, false);
					b.mod = true;
					b.p = aspf.findPath(null, (int) (b.getController().src.x / 16),
							(int) (b.getController().src.y / 16), (int) (Math.random() * m.getWidthInTiles()),
							(int) (Math.random() * m.getHeightInTiles()));
				}
			}*/
			if (en instanceof Player) {
				((Player) en).update(gc, delta);
			} else {
				en.update(delta);
			}
		}
		for (int ie = bullets.size() - 1; ie >= 0; ie--) {
			Entity bu = bullets.get(ie);
			if (bu == null)
				continue;
			if (bu.destroy == true) {
				bullets.remove(bu);
				continue;
			}
			bu.update(delta);
		}
		
		for (int ie = overlaidEffects.size() - 1; ie >= 0; ie--) {
			Entity pu = overlaidEffects.get(ie);
			if (pu == null)
				continue;
			if (pu.destroy == true) {
				overlaidEffects.remove(pu);
				overlaidEffects.trimToSize();
				continue;
			}
			pu.update(delta);
		}
	}
	
	public void draw(GameContainer gc, Graphics g) {
		Camera c = MainGame.getCamera();
		//Sorting the Entity array
		Collections.sort(entities);
		
		//Sorting the Bullet array
		Collections.sort(bullets);
		//Rendering Shadows
		g.setColor(new Color(0,0,0,0.7f));
		//Rendering visual effects
		g.setColor(Color.white);
		for (int i = visualEffects.size()-1; i > -1; i--) {
			Entity en = visualEffects.get(i);
			if (en == null)
				continue;
			en.draw(g);
		}
		
		g.setColor(Color.black);
		
		//Rendering entity shadows
		for (int i = entities.size()-1; i > -1; i--) {
			Entity en = entities.get(i);
			if (en == null)
				continue;
			if (en.getLocation().distance(c.getCenter()) <= MainGame.getViewDistance()) {
				en.seen = true;
				if (en.visible && en.hasshadow && shadowImage != null)
					shadowImage.draw(en.getLocation().getX() - en.collisionbox.getWidth() / 2,
							en.getLocation().getY() - en.collisionbox.getHeight()+1, en.collisionbox.getWidth(),
							en.collisionbox.getHeight() + 1);
			} else {
				en.seen = false;
			}
		}
		
		//Rendering bullet shadows
		for (int i = bullets.size() -1; i > -1; i--) {
			Entity bu = bullets.get(i);
			if (bu == null)
				continue;
			if (bu.getLocation().distance(c.getCenter()) <= MainGame.getViewDistance()) {
				bu.seen = true;
				if (bu.hasshadow)
					shadowImage.draw(bu.getLocation().getX() - bu.collisionbox.getWidth() / 2,
							bu.getLocation().getY() - bu.collisionbox.getHeight()+1, bu.collisionbox.getWidth(),
							bu.collisionbox.getHeight() + 1);
			} else {
				bu.seen = false;
			}
		}
		
		g.setColor(Color.white);
		//Rendering entities
		for (int i = entities.size() - 1; i > -1; i--) {
			Entity en = entities.get(i);
			if (en == null)
				continue;
			if (en.seen == true) {
				//Render Entities
				en.draw(g);
			}
		}
		
		//Rendering healthbars and status effects
		for (int i = entities.size()-1; i > -1; i--) {
			Entity en = entities.get(i);
			if (en == null)
				continue;
			if (en.seen == true) {
				if (en.getStatusEffectManager().getStatusEffects().isEmpty() == false) {
					ArrayList<Image> symbols = en.getStatusEffectManager().getVisuals();
					int offset = symbols.size() > 1 ? 8*(symbols.size()/2)-8 : 4;
					for (int e = symbols.size()-1; e > -1; e--) {
						Image im = symbols.get(e);
						if (im == null)
							continue;
						im.draw(en.hitbox.getCenterX()-offset-(e*8), en.hitbox.getMinY()-8);
					}
				}
				if (en.isHealthbarVisible()) {
					g.setColor(Color.red);
					g.fillRect(en.getLocation().x-6, en.hitbox.getMaxY()+1, 12, 3);
					g.setColor(Color.green);
					g.fillRect(en.getLocation().x-6, en.hitbox.getMaxY()+1, (12)*(Math.max(en.getStats().getCurrentHealth(), 0)/en.getMaxHealth()), 3);
					if (en.invulnerable == false || en.isInvincible() == true)
						g.setColor(Color.black);
					else
						g.setColor(Color.blue);
					g.drawRect(en.getLocation().x-6, en.hitbox.getMaxY()+1, 12, 3);
					g.setColor(Color.white);
				}
			}
		}
		
		//Rendering bullets
		if (bullets.size() > 0) {
			for (int e = 0; e < bullets.size(); e++) {
				Entity bu = bullets.get(e);
				if (bu == null)
					continue;
				if (bu.seen) {
					bu.draw(g);
				}
			}
		}
		
		
		//Rendering overlaid effects
		for (int i = overlaidEffects.size()-1; i > -1; i--) {
			Entity en = overlaidEffects.get(i);
			if (en == null)
				continue;
			en.draw(g);
		}
		
		
		
		
	}
	
	public static void loadEntities() throws SlickException {
		entityClasses.put("dammahclone", new Class[] {float.class, float.class, long.class});
		//entityClasses.put("biggoblin", new Class[] {float.class, float.class});
	}
	
	public static void loadAliases() {
		entityAliases.put("biggoblin", BigGoblin.class.getName());
		entityAliases.put("dammahclone", BulletSpawner.class.getName());
		entityAliases.put("testchest", TestChest.class.getName());
		entityAliases.put("sheep", Sheep.class.getName());
	}
	
	public static Class<?>[] getEntityInstArgs(String name) {
		if (entityClasses.containsKey(name.toLowerCase()))
			return entityClasses.get(name.toLowerCase());
		else
			return new Class[] {float.class, float.class};
	}
	
	public static String getFromAlias(String name) {
		return entityAliases.get(name.toLowerCase());
	}
	
	public static boolean isAlias(String name) {
		return entityAliases.containsKey(name.toLowerCase());
	}
	
	public static boolean classHasAlias(Class<?> c) {
		return entityAliases.containsValue(c.getName());
	}
	
	public static String getAlias(Class<?> c) {
		for (Entry<String, String> entry : entityAliases.entrySet()) {
			if (entry.getValue().equals(c.getName())) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public static Entity createEntityOfType(String name, Object... args) {
		Entity e = null;
		try {
			String truename = name;
			if (isAlias(name) == true)
				truename = getFromAlias(name);
			e = (Entity) Class.forName(truename).getConstructor(getEntityInstArgs(name)).newInstance(args);
		} catch (Exception e1 ) {
			e1.printStackTrace();
		}
		return e;
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void addBullet(Entity e) {
		bullets.add(e);
	}
	
	public void addVisualEffect(Entity e) {
		visualEffects.add(e);
	}
	
	public void addOverlaidEffect(Entity e) {
		overlaidEffects.add(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	public void removeBullet(Entity e) {
		bullets.remove(e);
	}
	
	public void removeVisualEffect(Entity e) {
		visualEffects.remove(e);
	}
	
	public void removeOverlaidEffect(Entity e) {
		overlaidEffects.remove(e);
	}

	public void spawnTempText(String s, float x, float y, Color c) throws SlickException {
		addOverlaidEffect(new PopupText(s, x - MainGame.ttfS.getWidth(s) / 2, y - 10, c));
	}
	
	public void spawnTempEffect(Shape s, long duration, Color c) throws SlickException {
		addVisualEffect(new Effect(s, duration, c));
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public ArrayList<Entity> getBullets() {
		return bullets;
	}
	
	public ArrayList<Entity> getVisualEffects() {
		return visualEffects;
	}

	public ArrayList<Entity> getOverlayedEffects() {
		return overlaidEffects;
	}
}
