package com.miskatonicmysteries.common.feature.sanity;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class SpookySoundInsanityEvent extends InsanityEvent {

	public SpookySoundInsanityEvent() {
		super(new Identifier(Constants.MOD_ID, "spooky_sound"), 0.2F, 920);
	}

	@Override
	public boolean execute(PlayerEntity playerEntity, Sanity sanity) {
		if (playerEntity.world.isClient) {
			playerEntity.playSound(MMSounds.AMBIENT_SCARY, 0.8F + (float) playerEntity.getRandom().nextGaussian() * 0.2F,
				(float) (0.8F + playerEntity.getRandom().nextGaussian() * 0.4F));
		} else if (sanity.getSanity() < 500 && playerEntity.getRandom().nextBoolean()) {
			playerEntity.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 1200, 0, true, false));
		}
		return super.execute(playerEntity, sanity);
	}

	@Override
	public boolean test(PlayerEntity player, Sanity sanity, float insanityFactor) {
		return super.test(player, sanity, insanityFactor) && !player.hasStatusEffect(MMStatusEffects.MANIA);
	}
}
