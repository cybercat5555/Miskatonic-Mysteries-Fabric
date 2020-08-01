package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.CommonProxy;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class StatusEffectMania extends StatusEffect {
    public StatusEffectMania() {
        super(StatusEffectType.HARMFUL, 0xA42100);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof MobEntity && ((MobEntity) entity).getTarget() == null && entity.age % 60 == 0) {
            if (entity.getRandom().nextFloat() < (0.1 * amplifier))
                onApplied(entity, entity.getAttributes(), amplifier);
        }
        if (entity instanceof PlayerEntity) {
            insanityDeath(entity, (ISanity) entity, amplifier);
            if (entity.age % 120 == 20 && entity.getRandom().nextFloat() < 0.05 * (amplifier + 1))
                InsanityHandler.handleInsanityEvents((PlayerEntity) entity);
        }
    }

    private void insanityDeath(LivingEntity entity, ISanity sanity, int amplifier) {
        if (sanity.getSanity() < CommonProxy.CONFIG.deadlyInsanityThreshold && entity.age % Math.min(60 - amplifier * 3, 20) == 0 && entity.getRandom().nextFloat() > (sanity.getSanity() / (float) CommonProxy.CONFIG.deadlyInsanityThreshold)){
            entity.damage(Constants.DamageSources.INSANITY, 666);
            sanity.setSanity(CommonProxy.CONFIG.deadlyInsanityThreshold + 50);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof MobEntity) {
            Optional<Entity> entityOptional = entity.world.getEntities(entity, entity.getBoundingBox().expand(8, 3, 8), target -> target instanceof LivingEntity).stream().findAny();
            if (entityOptional.isPresent())
                ((MobEntity) entity).setTarget((LivingEntity) entityOptional.get());
        }
    }
}
