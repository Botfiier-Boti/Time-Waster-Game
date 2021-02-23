package com.botifier.timewaster.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;

public class Stats {
	private Entity owner;

	private float health = 800;
	private float maxhealth = 800;
	private float atk = 50;
	private float dex = 35;
	private float def = 0;
	private float vit = 0;
	private float spd = 35;
	
	private float healthMod = 0;
	private float atkMod = 0;
	private float dexMod = 0;
	private float defMod = 0;
	private float vitMod = 0;
	private float spdMod = 0;
	
	
	
	public Stats(Entity owner) {
		this.owner = owner;
	}
	
	public float getCurrentHealth() {
		if (health > maxhealth+healthMod)
			health = maxhealth+healthMod;
		if (health < 0)
			health = 0;
		return health;
	}
	
	public void setMaxHealth(float health, boolean heal) {
		this.maxhealth = health;
		if (heal == true)
			this.health = maxhealth+healthMod;
	}
	
	public void damage(int damage, Entity origin, boolean ignoresDefense) {
		if (getOwner().active == false)
			return;
		if (origin.team == getOwner().team || origin == getOwner() || getOwner().invincible == true || getOwner().invulnerable == true)
			return;
		int d = 0;
		if (ignoresDefense == false) 
			d = (int) Math.max(damage*0.15f, damage-((getDefense()+getDefMod())));
		else 
			d = damage;
		if (d > 0) {
			try {
				MainGame.spawnTempText("-"+d, getOwner().hitbox.getCenterX(), getOwner().hitbox.getMinY(), Color.red);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		health -= d;
	}
	
	public void heal(float amount, boolean text) {
		float nAmount = amount;
		if (nAmount > (maxhealth+healthMod)-health)
			nAmount = (maxhealth+healthMod)-health;
		if (nAmount < 0)
			nAmount = 0;
		if (nAmount == 0)
			return;
		if (text) {
			try {
				MainGame.spawnTempText("+"+(int)nAmount, getOwner().hitbox.getMinX(), getOwner().hitbox.getMinY(), Color.green);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		health += nAmount;
	}
	
	public void heal(float amount) {
		heal(amount,true);
	}
	
	public void setCurrentHealth(float ch) {
		if (ch < 0) {
			health = 0;
		} else if (ch <= maxhealth+healthMod) {
			health = ch;
		} else {
			health = maxhealth+healthMod;
		}
	}
	
	public void setAttack(float atk) {
		this.atk = atk;
	}
	
	public void setDexterity(float dex) {
		this.dex = dex;
	}
	
	public void setVitality(float vit) {
		this.vit = vit;
	}
	
	public void setDefense(float def) {
		this.def = def;
	}
	
	public void setSpeed(float speed) {
		this.spd = Math2.round(speed, 2);;
	}
	
	public void setHealthMod(float healthMod) {
		this.healthMod = healthMod;
	}

	public void setDefMod(float defMod) {
		this.defMod = defMod;
	}

	public void setDexMod(float dexMod) {
		this.dexMod = dexMod;
	}

	public void setAtkMod(float atkMod) {
		this.atkMod = atkMod;
	}

	public void setVitMod(float vitMod) {
		this.vitMod = vitMod;
	}

	public void setSpdMod(float spdMod) {
		this.spdMod = Math2.round(spdMod,2);
		if (this.spdMod < -spd+1)
			this.spdMod = -spd+1;
	}
	
	public float getMaxHealth() {
		return maxhealth;
	}
	
	public float getAttack() {
		return atk;
	}
	
	public float getDexterity() {
		return dex;
	}
	
	public float getDefense() {
		return def;
	}
	
	public float getVitality() {
		return vit;
	}
	
	public float getSpeed() {
		return spd;
	}
	
	public float getHealthMod() {
		return healthMod;
	}

	public float getDefMod() {
		return defMod;
	}
	
	public float getVitMod() {
		return vitMod;
	}

	public float getSpdMod() {
		return spdMod;
	}
	
	public float getAtkMod() {
		return atkMod;
	}

	public float getDexMod() {
		return dexMod;
	}
	
	public float getPPS() {
		return 0.6f + 1.5f*((spd+spdMod)/75f);
	}
	
	public Entity getOwner() {
		return owner;
	}
	
}
