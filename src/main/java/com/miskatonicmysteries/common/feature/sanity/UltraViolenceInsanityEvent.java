package com.miskatonicmysteries.common.feature.sanity;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class UltraViolenceInsanityEvent extends InsanityEvent {

	public UltraViolenceInsanityEvent() {
		super(new Identifier(Constants.MOD_ID, "ultra_violence"), 0.5F, 660);
	}

	@Override
	public boolean execute(PlayerEntity playerEntity, Sanity sanity) {
		if (!playerEntity.world.isClient) {
			playerEntity.addStatusEffect(new StatusEffectInstance(MMStatusEffects.ULTRA_VIOLENCE, 4800,
				(Constants.DataTrackers.SANITY_CAP - sanity.getSanity()) / 200, true, true));
		}
		return super.execute(playerEntity, sanity);
	}

	@Override
	public boolean test(PlayerEntity player, Sanity sanity, float insanityFactor) {
		return super.test(player, sanity, insanityFactor) && !player.hasStatusEffect(MMStatusEffects.ULTRA_VIOLENCE);
	}
}
