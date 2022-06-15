package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BurnedVeilRite extends SpawnerTrapRite {

	public BurnedVeilRite() {
		super(new Identifier(Constants.MOD_ID, "burned_veil"), null,
			  (type) -> type.isIn(Constants.Tags.BURNED_VEIL_MOBS), ParticleTypes.FLAME, new float[]{1, 0.25F, 0},
			  Ingredient.ofItems(MMObjects.IRIDESCENT_PEARL), Ingredient.ofItems(Items.CRIMSON_FUNGUS), Ingredient.ofItems(Items.ENDER_PEARL),
			  Ingredient.ofItems(Items.BLAZE_POWDER));
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		super.tick(octagram);
		if (octagram.tickCount > 120 && octagram.tickCount % 60 == 0) {
			World world = octagram.getWorld();
			for (BlockPos iterateOutward : BlockPos.iterateOutwards(octagram.getPos().add(0, 2, 0), 4, 4, 4)) {
				if (world.random.nextFloat() < 0.01F && world.getBlockState(iterateOutward)
					.isSolidBlock(octagram.getWorld(), iterateOutward) && world.getBlockState(iterateOutward.up()).isAir()) {
					world.setBlockState(iterateOutward.up(), Blocks.FIRE.getDefaultState());
					for (int i = 0; i < 5; i++) {
						octagram.getWorld().addParticle(ParticleTypes.FLAME, iterateOutward.getX() + world.random.nextFloat(),
														iterateOutward.getY() + 1 + world.random.nextFloat(),
														iterateOutward.getZ() + world.random.nextFloat(), 0, 0,
														0);
					}
				}
			}
		}
	}

	@Override
	public void trigger(OctagramBlockEntity octagram, Entity triggeringEntity) {
		super.trigger(octagram, triggeringEntity);
		World world = octagram.getWorld();
		for (BlockPos iterateOutward : BlockPos.iterateOutwards(octagram.getPos().add(0, 2, 0), 4, 4, 4)) {
			if (world.random.nextFloat() < 0.1F && world.getBlockState(iterateOutward).isSolidBlock(octagram.getWorld(), iterateOutward)
				&& world.getBlockState(iterateOutward.up()).isAir()) {
				world.setBlockState(iterateOutward.up(), Blocks.FIRE.getDefaultState());
				for (int i = 0; i < 5; i++) {
					octagram.getWorld().addParticle(ParticleTypes.FLAME, iterateOutward.getX() + world.random.nextFloat(),
													iterateOutward.getY() + 1 + world.random.nextFloat(),
													iterateOutward.getZ() + world.random.nextFloat(), 0, 0, 0);
				}
			}
		}
	}
}
