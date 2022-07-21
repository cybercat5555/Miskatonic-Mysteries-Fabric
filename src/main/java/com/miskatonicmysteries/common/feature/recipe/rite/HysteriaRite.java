package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class HysteriaRite extends TriggeredRite {

	public HysteriaRite() {
		super(new Identifier(Constants.MOD_ID, "hysteria"), null, 0.05F, 60);
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		if (octagram.tickCount < ticksNeeded && octagram.getWorld().isClient) {
			Vec3d position = octagram.getSummoningPos()
				.add(octagram.getWorld().random.nextGaussian() * 5, -0.25 + octagram.getWorld().random
					.nextFloat() * 5, octagram.getWorld().random.nextGaussian() * 5);
			octagram.getWorld().addParticle(MMParticles.AMBIENT, position.x, position.y, position.z, 0.85, 0, 0);
		}
		super.tick(octagram);
	}

	@Override
	public void onFinished(OctagramBlockEntity octagram) {
		octagram.getWorld().playSound(null, octagram
			.getPos(), MMSounds.RITE_RITE_TRIGGERED, SoundCategory.AMBIENT, 1.0F, (float) octagram
			.getWorld().random.nextGaussian() * 0.2F + 1.0F);

		if (octagram.getWorld().isClient) {
			for (int i = 0; i < 25; i++) {
				Vec3d position = octagram.getSummoningPos()
					.add(octagram.getWorld().random.nextGaussian() * 5, -0.25 + octagram.getWorld().random
						.nextFloat() * 5, octagram.getWorld().random.nextGaussian() * 5);
				octagram.getWorld().addParticle(MMParticles.AMBIENT, position.x, position.y, position.z, 0.85, 0, 0);
			}
		} else {
			octagram.getWorld().getEntitiesByClass(LivingEntity.class, octagram.getSelectionBox()
				.expand(7, 7, 7), (livingEntity) -> true)
				.forEach(living -> living
					.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 3600, octagram
						.getWorld().random.nextInt(2), false, true)));
		}
		super.onFinished(octagram);
	}

	@Override
	public boolean isFinished(OctagramBlockEntity octagram) {
		return octagram.triggered && octagram.tickCount >= ticksNeeded;
	}
}
