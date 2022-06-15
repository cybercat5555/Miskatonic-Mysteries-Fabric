package com.miskatonicmysteries.common.feature.recipe.instability_event;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class PotionInstabilityEvent extends InstabilityEvent {

	public PotionInstabilityEvent() {
		super(new Identifier(Constants.MOD_ID, "potion"), 0.1F, 0.5F);
	}

	@Override
	public boolean cast(OctagramBlockEntity blockEntity, float instability) {
		World world = blockEntity.getWorld();
		Random random = world.getRandom();
		StatusEffectInstance effect = new StatusEffectInstance(pickStatusEffect(random, instability), (int) (200 + 100 * instability),
															   (int) (instability / 0.5F));
		Vec3d pos = blockEntity.getSummoningPos().add(random.nextGaussian(), -0.2, random.nextGaussian());
		AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, pos.x, pos.y, pos.z);
		cloud.addEffect(effect);
		cloud.setRadius(0.5F + random.nextFloat() * 2 * instability);
		cloud.setPos(pos.x, pos.y, pos.z);
		cloud.setDuration(200);
		world.spawnEntity(cloud);
		return false;
	}

	private StatusEffect pickStatusEffect(Random random, float instability) {
		int selectionNum = (int) (random.nextInt(4) * (0.5F + instability));
		return switch (selectionNum) {
			case 1 -> random.nextBoolean() ? StatusEffects.NAUSEA : StatusEffects.BLINDNESS;
			case 2 -> random.nextBoolean() ? StatusEffects.POISON : StatusEffects.WITHER;
			case 3 -> random.nextBoolean() ? StatusEffects.GLOWING : MMStatusEffects.CLAIRVOYANCE;
			default -> MMStatusEffects.MANIA;
		};
	}
}
