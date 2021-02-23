package com.botifier.timewaster.statuseffect.effects;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.statuseffect.StatusEffect;
import com.botifier.timewaster.statuseffect.StatusEffectType;

public class SlowEffect extends StatusEffect {
	int modifier;
	
	public SlowEffect(long duration, int modifier, int maxstacks) {
		super("Slow", duration, StatusEffectType.InstantEffect);
		this.modifier = modifier;
		this.maxstacks = maxstacks;
		symbol = MainGame.getImage("Slow");
	}

	@Override
	public void onApply() {
		getAfflicted().getStats().setSpdMod(getAfflicted().getStats().getSpdMod()-modifier);
	}

	@Override
	public void tick() {

	}

	@Override
	public void onEnd() {

		getAfflicted().getStats().setSpdMod(getAfflicted().getStats().getSpdMod()+modifier);
	}

}
