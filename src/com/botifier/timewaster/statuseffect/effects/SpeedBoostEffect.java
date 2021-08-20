package com.botifier.timewaster.statuseffect.effects;

import com.botifier.timewaster.statuseffect.StatusEffect;
import com.botifier.timewaster.statuseffect.StatusEffectType;

public class SpeedBoostEffect extends StatusEffect {
	int modifier;
	public SpeedBoostEffect(long duration, int modifier) {
		super("Speed Boost", duration, StatusEffectType.InstantEffect);
		this.modifier = modifier;
		this.maxstacks = 1;
	}

	@Override
	public void onApply() {
		getAfflicted().getStats().setSpdMod(getAfflicted().getStats().getSpdMod()+modifier);
	}

	@Override
	public void tick() {

	}

	@Override
	public void onEnd() {
		getAfflicted().getStats().setSpdMod(getAfflicted().getStats().getSpdMod()-modifier);
	}

}
