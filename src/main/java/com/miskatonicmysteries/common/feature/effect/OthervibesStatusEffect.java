package com.miskatonicmysteries.common.feature.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;


public class OthervibesStatusEffect extends StatusEffect {

	public OthervibesStatusEffect() {
		super(StatusEffectCategory.NEUTRAL, 0xFFFFFF);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);
        /*
        if(entity instanceof PlayerEntity player){
            if(!player.getWorld().isClient()){
                EndermanEntity endermanEntity = EntityType.ENDERMAN.create(player.getWorld());
                Optional<UUID> targetPlayerUUID = Optional.of(player.getUuid());
                ((OthervibeMobEntityAccessor)endermanEntity).setData(targetPlayerUUID);
                endermanEntity.copyPositionAndRotation(player);
                endermanEntity.setPersistent();
                player.getWorld().spawnEntity(endermanEntity);
            }
        }

         */
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
