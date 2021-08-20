package com.botifier.timewaster.util.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.botifier.timewaster.entity.enemy.SpearBeing;
import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.Bullet;
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
	private ArrayList<Entity> bullets = new ArrayList<Entity>();
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
							en.collisionbox.getHeight());
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
				if (en.getStatusEffectManager() != null && en.getStatusEffectManager().getStatusEffects().isEmpty() == false) {
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
	
	public void clearBullets() {
		for (int i = entities.size()-1; i >= 0; i--) {
			Entity e = entities.get(i);
			e.b.clear();
		}
		bullets.clear();
	}
	
	public ArrayList<Entity> getAllVisibleEntities() {
		Stream<Entity> ent = entities.stream()
		.filter(e -> e != null)
		.filter(e -> e.seen == true)
		.filter(e -> e.visible == true);
		List<Entity> l = ent.collect(Collectors.toList());
		ArrayList<Entity> al = new ArrayList<Entity>(l);
		return al;
	}
	
	public ArrayList<Entity> getAllNearbyEntities(Entity e, float max) {
		Stream<Entity> ent = entities.stream()
			.filter(e2 -> e2 != null)
			.filter(e2 -> e2.getLocation().distance(e.getLocation()) <= max)
			.filter(e2 -> e2.visible == true);
		List<Entity> l = ent.collect(Collectors.toList());
		ArrayList<Entity> al = new ArrayList<Entity>(l);
		return al;
	}
	
	public ArrayList<Entity> getAllNearbyEnemies(Entity e, float max) {
		Stream<Entity> ent = entities.stream()
			.filter(e2 -> e2 != null)
			.filter(e2 -> e2.team != e.team)
			.filter(e2 -> e2.getLocation().distance(e.getLocation()) <= max)
			.filter(e2 -> e2.visible == true);
		List<Entity> l = ent.collect(Collectors.toList());
		ArrayList<Entity> al = new ArrayList<Entity>(l);
		return al;
	}
	
	
	public Entity findClosest(Entity e, float max) {
		for (int i = entities.size()-1; i > -1; i--) {
			Entity e2 = entities.get(i);
			float dist = e2.getLocation().distance(e.getLocation());
			if (dist > max || e2.targetable == false )
				continue;
			if ((i-1 >= 0 && dist > entities.get(i-1).getLocation().distance(e.getLocation())))
				return e2;
			if (i+1 < entities.size() && dist > entities.get(i+1).getLocation().distance(e.getLocation()))
				return e2;
		}
		return null;
	}
	
	public Entity findClosestEnemy(Entity e, float max) {
		if (entities.size() < 4) {
			Entity cls = null;
			for (int i = entities.size() - 1; i > -1; i--) {
				Entity en = entities.get(i);
				if (en instanceof Bullet || en.isInvincible() || en == e || en.team == e.team || en.invulnerable == true
						|| en.targetable == false || en.active == false || en.visible == false
						|| e.getLocation().distance(en.getLocation()) > max || (cls != null && e.getLocation().distance(en.getLocation()) > e.getLocation().distance(cls.getLocation())))
					continue;
				cls = en;
			}
			return cls;
		}
		boolean full = false;
		for (int i = entities.size() - 1; i > -1; i--) {
			Entity e2 = entities.get(i);
			float dist = e2.getLocation().distance(e.getLocation());
			if (dist > max || e2.team == e.team || e2 == e || e2.targetable == false || e2.invulnerable == true || e2.visible == false )
				continue;
			if (i-1 < -1 && full == false) {
				i = entities.size()-1;
				full = true;
				continue;
			}
			if ((i-1 >= 0 && (dist < entities.get(i-1).getLocation().distance(e.getLocation()) || (entities.get(i-1).targetable == false || entities.get(i-1).visible == false || entities.get(i-1).active == false || entities.get(i-1).invulnerable == true || entities.get(i-1).team == e.team ))))
				return e2;
			if (i+1 < entities.size() && (dist < entities.get(i+1).getLocation().distance(e.getLocation()) || (entities.get(i+1).targetable == false || entities.get(i+1).visible == false || entities.get(i+1).active == false || entities.get(i+1).invulnerable == true || entities.get(i+1).team == e.team)))
				return e2;
			if (i-1 < 0 || i+1 >= entities.size())
				return e2;
		}
		return null;
	}
	
	public Entity findClosestAlly(Entity e, float max) {
		for (int i = entities.size()-1; i > -1; i--) {
			Entity e2 = entities.get(i);
			float dist = e2.getLocation().distance(e.getLocation());
			System.out.println(dist);
			if (dist > max || e2.team != e.team|| e2.targetable == false )
				continue;
			if ((i-1 >= 0 && dist > entities.get(i-1).getLocation().distance(e.getLocation())))
				return e2;
			if (i+1 < entities.size() && dist > entities.get(i+1).getLocation().distance(e.getLocation()))
				return e2;
		}
		return null;
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
		entityAliases.put("spearman", SpearBeing.class.getName());
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
