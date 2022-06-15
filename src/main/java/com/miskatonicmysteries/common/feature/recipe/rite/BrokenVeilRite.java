package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.Objects;

public class BrokenVeilRite extends SpawnerTrapRite {

	public BrokenVeilRite() {
		super(new Identifier(Constants.MOD_ID, "broken_veil"), null,
			  (type) -> type.isIn(Constants.Tags.BROKEN_VEIL_MOBS), ParticleTypes.PORTAL, new float[]{0.65F, 0, 1},
			  Ingredient.ofItems(MMObjects.IRIDESCENT_PEARL), Ingredient.ofItems(Items.PHANTOM_MEMBRANE),
			  Ingredient.ofItems(Items.ENDER_PEARL), Ingredient.ofItems(MMObjects.INFESTED_WHEAT));
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		super.tick(octagram);
		if (octagram.tickCount > 100 && octagram.tickCount % 100 == 10) {
			octagram.getWorld().getEntitiesByClass(LivingEntity.class,
												   new Box(octagram.getSummoningPos().add(-5, -3, -5), octagram.getSummoningPos().add(5, 3, 5)),
												   Objects::nonNull)
				.forEach(entity -> entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 0, true, true)));
		}
	}
}
