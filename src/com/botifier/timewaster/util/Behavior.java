package com.botifier.timewaster.util;

/**
 * Base class for enemy behaviors
 * @author Botifier
 *
 */
public abstract class Behavior {
	/**
	 * The owner of the behavior instance
	 */
	private Enemy owner;
	
	/**
	 * 
	 * @param owner The owner of this behavior object
	 */
	public Behavior(Enemy owner) {
		this.owner = owner;
	}
	
	/**
	 * Returns the owner of this behavior object
	 * @return Enemy 
	 */
	public Enemy getOwner() {
		return owner;
	}
	
	/**
	 * Action done while the behavior is active
	 */
	public abstract void run();
}
