package com.miskatonicmysteries.api.interfaces;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.VillagerEntity;

public interface VillagerPartyDrug {

	default boolean canDrug(VillagerEntity villager) {
		return !villager.hasStatusEffect(getStatusEffect(villager).getEffectType());
	}

	StatusEffectInstance getStatusEffect(VillagerEntity villager);
}
