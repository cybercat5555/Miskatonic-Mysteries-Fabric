package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.handler.networking.packet.s2c.BloodParticlePacket;
import com.miskatonicmysteries.common.registry.MMStatusEffects;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.spawner.PhantomSpawner;

import java.util.Random;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class GreenFairyStatusEffect extends StatusEffect {

	public GreenFairyStatusEffect() {
		super(StatusEffectCategory.NEUTRAL, 0x00AA00);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity p && p.world instanceof ServerWorld serverWorld) {
			Random random = p.getRandom();
			if (serverWorld.getTime() % 200 == 0 && random.nextInt((amplifier + 1) * 10) >= 9) {
				BlockPos potentialPos = entity.getBlockPos()
					.up(20 + random.nextInt(15))
					.east(-10 + random.nextInt(21))
					.south(-10 + random.nextInt(21));
				if (SpawnHelper.isClearForSpawn(serverWorld, potentialPos, serverWorld.getBlockState(potentialPos),
												serverWorld.getFluidState(potentialPos), EntityType.PHANTOM)) {
					int amount = random.nextInt(2 + Math.min(amplifier, 3));
					for (int i = 0; i < amount; i++) {
						PhantomEntity phantomEntity = EntityType.PHANTOM.create(serverWorld);
						phantomEntity.refreshPositionAndAngles(potentialPos, 0.0f, 0.0f);
						phantomEntity.initialize(serverWorld, serverWorld.getLocalDifficulty(p.getBlockPos()), SpawnReason.NATURAL, null, null);
						serverWorld.spawnEntityAndPassengers(phantomEntity);
						phantomEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 24000, 0, false, false, false));
					}
				}
			}
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
