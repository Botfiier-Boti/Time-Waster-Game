package com.botifier.timewaster.statuseffect.effects;

import com.botifier.timewaster.statuseffect.StatusEffect;
import com.botifier.timewaster.statuseffect.StatusEffectType;

public class DamageBoostEffect extends StatusEffect {
	int modifier;
	public DamageBoostEffect(long duration, int modifier) {
		super("Damage Boost", duration, StatusEffectType.InstantEffect);
		this.modifier = modifier;
	}

	@Override
	public void onApply() {
		getAfflicted().getStats().setAtkMod(getAfflicted().getStats().getAtkMod()+modifier);
	}

	@Override
	public void tick() {

	}

	@Override
	public void onEnd() {
		getAfflicted().getStats().setAtkMod(getAfflicted().getStats().getAtkMod()-modifier);
	}

}
