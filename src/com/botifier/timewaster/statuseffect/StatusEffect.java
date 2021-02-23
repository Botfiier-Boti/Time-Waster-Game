package com.botifier.timewaster.statuseffect;

import org.newdawn.slick.Image;

import com.botifier.timewaster.util.Entity;

public abstract class StatusEffect {
	protected Image symbol;
	protected Entity afflicted;
	protected boolean inf = false;
	protected long duration;
	protected int maxstacks = 1;
	protected StatusEffectType type;
	protected String name;
	
	public StatusEffect(String name, long duration, StatusEffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}
	
	public abstract void onApply();
	
	public abstract void onEnd();
	
	public abstract void tick();
	
	public void update(int delta) {
		if (afflicted == null)
			return;
		if (inf == false)
			duration -= delta;
		if (duration <= 0) {
			onEnd();
			afflicted = null;
			return;
		}
		tick();
	}
	
	public void setInfinite(boolean b) {
		inf = b;
	}
	
	public void setAfflicted(Entity afflicted) {
		this.afflicted = afflicted;
	}
	
	public int getMaxStacks() {
		return maxstacks;
	}
	
	public long getRemainingDuration() {
		return duration;
	}
	
	public boolean isInfinite() {
		return inf;
	}
	
	public Entity getAfflicted() {
		return afflicted;
	}
	
	public Image getVisual() {
		return symbol;
	}

	public StatusEffectType getEffectType() {
		return type;
	}
}
