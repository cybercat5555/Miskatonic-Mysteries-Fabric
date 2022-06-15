package com.miskatonicmysteries.common.feature.sanity;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ExoticCravingsInsanityEvent extends InsanityEvent {

	public ExoticCravingsInsanityEvent() {
		super(new Identifier(Constants.MOD_ID, "exotic_cravings"), 0.7F, 550);
	}

	@Override
	public boolean execute(PlayerEntity playerEntity, Sanity sanity) {
		if (playerEntity.world.isClient) {
			playerEntity.playSound(MMSounds.SPELL_SPELL_CAST, 0.5F, 0.7F);
		} else {
			playerEntity.addStatusEffect(new StatusEffectInstance(MMStatusEffects.EXOTIC_CRAVINGS, 4800));
		}
		return super.execute(playerEntity, sanity);
	}

	@Override
	public boolean test(PlayerEntity player, Sanity sanity, float insanityFactor) {
		return super.test(player, sanity, insanityFactor) && !player.hasStatusEffect(MMStatusEffects.EXOTIC_CRAVINGS);
	}
}
