package com.botifier.timewaster.util.managers;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import com.botifier.timewaster.statuseffect.StatusEffect;
import com.botifier.timewaster.statuseffect.StatusEffectType;
import com.botifier.timewaster.util.Entity;

public class StatusEffectManager {
	private Entity owner;
	private ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
	private ArrayList<Class<? extends StatusEffect>> immunities = new ArrayList<Class<? extends StatusEffect>>();
	
	public StatusEffectManager(Entity owner) {
		this.owner = owner;
	}
	
	public void update(int delta) {
		if (owner == null)
			return;
		for (int i = effects.size()-1; i >= 0; i--) {
			StatusEffect e = effects.get(i);
			if (e == null)
				continue;
			e.update(delta);
			if (e.getRemainingDuration() <= 0) {
				effects.remove(i);
				continue;
			}
		}
	}
	
	public void addEffect(StatusEffect e) {
		if (owner == null)
			return;
		if (isImmune(e.getClass()))
			return;
		e.setAfflicted(owner);
		if (hasEffect(e.getClass())) {
			if (e.getMaxStacks() > 1) {
				int stacks = 0;
				for (int i = effects.size()-1; i >= 0; i--) {
					StatusEffect eff = effects.get(i);
					if (e == null)
						continue;
					if (eff.getClass() == e.getClass() && eff.getMaxStacks() == e.getMaxStacks())
						stacks++;
					if (stacks > e.getMaxStacks())
						return;
				}
			} else {
				return;
			}
		}
		if (e.getEffectType() == StatusEffectType.InstantEffect)
			e.onApply();
		effects.add(e);
	}
	
	public boolean hasEffect(Class<? extends StatusEffect> class1) {
		for (int i = effects.size()-1; i >= 0; i--) {
			StatusEffect e = effects.get(i);
			if (e == null)
				continue;
			if (e.getClass() == class1)
				return true;
		}
		return false;
	}
	
	public ArrayList<StatusEffect> getEffectsOfType(Class<? extends StatusEffect> class1) {
		ArrayList<StatusEffect> list = new ArrayList<StatusEffect>();
		for (int i = effects.size()-1; i >= 0; i--) {
			StatusEffect e = effects.get(i);
			if (e == null)
				continue;
			if (e.getClass() == class1)
				list.add(e);
		}
		return list;
	}
	
	public void removeEffect(Class<? extends StatusEffect> class1) {
		ArrayList<StatusEffect> list = getEffectsOfType(class1);
		for (int i = effects.size()-1; i >= 0; i--) {
			StatusEffect e = effects.get(i);
			if (e == null)
				continue;
			if (list.contains(e)) {
				e.onEnd();
				effects.remove(e);
			}
		}
	}
	
	public ArrayList<StatusEffect> getStatusEffects() {
		return effects;
	}

	public ArrayList<Class<? extends StatusEffect>> getImmunities() {
		return immunities;
	}
	
	public void addImmunity(Class<? extends StatusEffect> immune) {
		if (immunities.contains(immune) == false) {
			immunities.add(immune);
		}
	}
	
	public boolean isImmune(Class<? extends StatusEffect> immune) {
		if (immunities.contains(immune))
			return true;
		return false;
	}
	
	public ArrayList<Image> getVisuals() {
		ArrayList<Image> symbols = new ArrayList<Image>();
		symbols.clear();
		for (int i = effects.size()-1; i >= 0; i--) {
			StatusEffect e = effects.get(i);
			if (e == null)
				continue;
			if (e.getVisual() == null)
				continue;
			if (!symbols.contains(e.getVisual()))
				symbols.add(e.getVisual());
		}
		return symbols;
	}

	public Entity getOwner() {
		return owner;
	}
}
