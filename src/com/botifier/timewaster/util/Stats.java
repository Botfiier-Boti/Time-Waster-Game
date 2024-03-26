package com.botifier.timewaster.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import com.botifier.timewaster.main.MainGame;

/**
 * Entity Statistics class
 * @author Botifier
 *
 */
public class Stats {
	/**
	 * The entity in which these stats represent
	 */
	private Entity owner;

	/**
	 * The current base amount of health the Entity has
	 */
	private float health = 800;
	/**
	 * The maximum base amount of health the Entity can have
	 */
	private float maxhealth = 800;
	/**
	 * The current base amount of attack the Entity has
	 */
	private float atk = 50;
	/**
	 * The current base amount of dexterity the Entity has
	 */
	private float dex = 35;
	/**
	 * The current base amount of defense the Entity has
	 */
	private float def = 0;
	/**
	 * The current base amount of vitality the Entity has
	 */
	private float vit = 0;
	/**
	 * The current base amount of speed the Entity has
	 */
	private float spd = 35;
	
	/**
	 * Temporary modifier to health statistic
	 */
	private float healthMod = 0;
	/**
	 * Temporary modifier to attack statistic
	 */
	private float atkMod = 0;
	/**
	 * Temporary modifier to dexterity statistic
	 */
	private float dexMod = 0;
	/**
	 * Temporary modifier to defense statistic
	 */
	private float defMod = 0;
	/**
	 * Temporary modifier to vitality statistic
	 */
	private float vitMod = 0;
	/**
	 * Temporary modifier to speed statistic
	 */
	private float spdMod = 0;
	
	
	/**
	 * Stats constructor
	 * @param owner Entity The entity these statistics represent
	 */
	public Stats(Entity owner) {
		this.owner = owner;
	}
	
	/**
	 * Returns the current health amount
	 * 0 <= health <= maxhealth+healthMod
	 * @return float Current amount of health
	 */
	public float getCurrentHealth() {
		if (health > maxhealth+healthMod)
			health = maxhealth+healthMod;
		if (health < 0)
			health = 0;
		return health;
	}
	
	/**
	 * Sets the maximum amount of health
	 * @param health float New max health
	 * @param heal boolean Whether or not health should heal to this amount
	 */
	public void setMaxHealth(float health, boolean heal) {
		this.maxhealth = health;
		if (heal == true)
			this.health = maxhealth+healthMod;
	}
	
	/**
	 * Preforms the damage calculation and subtracts health
	 * @param damage int Damage to do
	 * @param origin Entity Damage origin
	 * @param ignoresDefense boolean Whether or not the damage should obey defenses
	 */
	public void damage(int damage, Entity origin, boolean ignoresDefense) {
		if (getOwner().active == false)
			return;
		if (origin.getTeam() == getOwner().getTeam() || origin == getOwner() || getOwner().invincible == true || getOwner().invulnerable == true)
			return;
		int d = 0;
		if (ignoresDefense == false) 
			d = (int) Math.max(damage*0.15f, damage-((getDefense()+getDefMod())));
		else 
			d = damage;
		if (d > 0) {
			try {
				Color c = Color.red;
				if (ignoresDefense)
					 c = Color.orange;
				MainGame.spawnTempText("-"+d, getOwner().hitbox.getCenterX(), getOwner().hitbox.getMinY(), c);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		} else if (d < 0) {
			try {
				Color c = Color.green;
				MainGame.spawnTempText("+"+Math.abs(d), getOwner().hitbox.getCenterX(), getOwner().hitbox.getMinY(), c);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		health -= d;
	}
	
	/**
	 * Heals for specified amount
	 * @param amount float To heal
	 * @param text boolean Whether or not to display heal text
	 */
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
	
	/**
	 * Heals for specified amount
	 * @param amount float To heal
	 */
	public void heal(float amount) {
		heal(amount,true);
	}
	
	/**
	 * Sets current health to specified amount
	 * @param ch float 0 <= ch <= maxhealth+healthMod
	 */
	public void setCurrentHealth(float ch) {
		if (ch < 0) {
			health = 0;
		} else if (ch <= maxhealth+healthMod) {
			health = ch;
		} else {
			health = maxhealth+healthMod;
		}
	}
	
	/**
	 * Sets base attack
	 * @param atk float To set
	 */
	public void setAttack(float atk) {
		this.atk = atk;
	}
	/**
	 * Sets base dexterity
	 * @param dex float To set
	 */
	public void setDexterity(float dex) {
		this.dex = dex;
	}
	/**
	 * Sets base vitality
	 * @param vit float To set
	 */
	public void setVitality(float vit) {
		this.vit = vit;
	}
	/**
	 * Sets base defense
	 * @param def float To set
	 */
	public void setDefense(float def) {
		this.def = def;
	}
	/**
	 * Sets base speed
	 * @param spd float To set
	 * rounded to 2 decimal spaces
	 */
	public void setSpeed(float speed) {
		this.spd = Math2.round(speed, 2);
	}
	
	/**
	 * Changes the health modifier to specified amount
	 * @param healthMod float To set
	 */
	public void setHealthMod(float healthMod) {
		this.healthMod = healthMod;
	}
	
	/**
	 * Changes the defense modifier to specified amount 
	 * @param defMod float To set
	 */
	public void setDefMod(float defMod) {
		this.defMod = defMod;
	}

	/**
	 * Changes the dexterity modifier to specified amount
	 * @param dexMod float To set
	 */
	public void setDexMod(float dexMod) {
		this.dexMod = dexMod;
	}

	/**
	 * Changes the attack modifier to specified amount
	 * @param atkMod float To set
	 */
	public void setAtkMod(float atkMod) {
		this.atkMod = atkMod;
	}

	/**
	 * Changes the vitality modifier to specified amount
	 * @param vitMod float To set
	 */
	public void setVitMod(float vitMod) {
		this.vitMod = vitMod;
	}

	/**
	 * Changes the speed modifier to specified amount
	 * @param spdMod float To set
	 * rounded to 2 decimal places
	 */
	public void setSpdMod(float spdMod) {
		this.spdMod = Math2.round(spdMod,2);
	}
	
	/**
	 * Sets all of the modifiers at once
	 * @param healthMod float To set
	 * @param defMod float To set
	 * @param atkMod float To set
	 * @param vitMod float To set
	 * @param dexMod float To set
	 * @param spdMod float To set
	 */
	public void setAllMods(float healthMod, float defMod, float atkMod, float vitMod, float dexMod, float spdMod) {
		setHealthMod(healthMod);
		setDefMod(defMod);
		setAtkMod(atkMod);
		setVitMod(vitMod);
		setDexMod(dexMod);
		setSpdMod(spdMod);
	}
	
	/**
	 * Returns the maximum amount of health
	 * @return float Maximum health
	 */
	public float getMaxHealth() {
		return maxhealth;
	}
	
	/**
	 * Returns the base attack stat
	 * @return float Base attack
	 */
	public float getAttack() {
		return atk;
	}
	
	/**
	 * Returns the base dexterity stat
	 * @return float Base dexterity
	 */
	public float getDexterity() {
		return dex;
	}
	
	/**
	 * Returns the base defense stat
	 * @return float Base defense
	 */
	public float getDefense() {
		return def;
	}
	
	/**
	 * Returns the base vitality stat
	 * @return float Base vitality
	 */
	public float getVitality() {
		return vit;
	}
	
	/**
	 * Returns the base speed stat
	 * @return float Base speed
	 */
	public float getSpeed() {
		return spd;
	}
	
	/**
	 * Returns the health modifier
	 * @return float Health modifier
	 */
	public float getHealthMod() {
		return healthMod;
	}

	/**
	 * Returns the defense modifier
	 * @return float Defense modifier
	 */
	public float getDefMod() {
		return defMod;
	}
	
	/**
	 * Returns the vitality modifier
	 * @return float Vitality modifier
	 */
	public float getVitMod() {
		return vitMod;
	}

	/**
	 * Returns the speed modifier
	 * @return float Speed modifier
	 */
	public float getSpdMod() {
		return spdMod;
	}
	
	/**
	 * Returns the attack modifier
	 * @return float Attack modifier
	 */
	public float getAtkMod() {
		return atkMod;
	}

	/**
	 * Returns the dexterity modifier
	 * @return float Dexterity modifier
	 */
	public float getDexMod() {
		return dexMod;
	}
	
	/**
	 * Returns the number of pixels moved per update
	 * @return
	 */
	public float getPPU() {
		return Math2.round(0.6f + 1.5f*((spd+spdMod)/75f), 2);
	}
	
	/**
	 * Returns the owner of these statistics
	 * @return Entity Statistic owner
	 */
	public Entity getOwner() {
		return owner;
	}
	
}
