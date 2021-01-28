package com.botifier.timewaster.util;

public abstract class Behavior {
	private Enemy owner;
	
	public Behavior(Enemy owner) {
		this.owner = owner;
	}
	
	public Enemy getOwner() {
		return owner;
	}
	
	public abstract void run();
}
