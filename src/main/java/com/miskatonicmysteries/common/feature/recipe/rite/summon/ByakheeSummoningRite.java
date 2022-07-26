package com.miskatonicmysteries.common.feature.recipe.rite.summon;

import com.miskatonicmysteries.client.render.entity.ByakheeEntityRenderer;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.ByakheeEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.model.Model;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ByakheeSummoningRite extends SummoningRite<ByakheeEntity> {

	public ByakheeSummoningRite() {
		super(new Identifier(Constants.MOD_ID, "summon_byakhee"), MMAffiliations.HASTUR, MMAffiliations.HASTUR.getId().getPath(), 0.25F, 1,
			  MMEntities.BYAKHEE);
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		World world = octagram.getWorld();
		Vec3d pos = octagram.getSummoningPos();
		if (!octagram.getFlag(0)) {
			if (world.isClient) {
				world.addParticle(MMParticles.DRIPPING_BLOOD, pos.x + world.random.nextGaussian(),
								  pos.y - 0.25F + world.random.nextFloat() * 2, pos.z + world.random.nextGaussian(), 0, 0.1F, 0);
			}
		} else {
			if (octagram.tickCount == 0) {
				world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);
			}
			if (world.isClient) {
				Vec3d particlePos = pos
					.add(world.random.nextGaussian() * 2, -0.25F + world.random.nextFloat() * 5, world.random.nextGaussian() * 2);
				world.addParticle(MMParticles.AMBIENT, particlePos.x, particlePos.y, particlePos.z, 1,
								  0.75 + world.random.nextFloat() * 0.25F, world.random.nextFloat() * 0.1F);
			}
			super.tick(octagram);
		}
	}

	@Override
	public void onFinished(OctagramBlockEntity octagram) {
		World world = octagram.getWorld();
		Vec3d pos = octagram.getSummoningPos();
		octagram.getWorld().playSound(null, pos.x, pos.y, pos.z, MMSounds.RITE_VEIL_SPAWN, SoundCategory.PLAYERS, 1, 1);
		if (world.isClient) {
			for (int i = 0; i < 100; i++) {
				Vec3d particlePos = pos
					.add(world.random.nextGaussian() * 1.5F, -0.25F + world.random.nextFloat() * 5, world.random.nextGaussian() * 1.5F);
				world.addParticle(ParticleTypes.LARGE_SMOKE, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
			}
		}
		super.onFinished(octagram);
	}

	@Environment(EnvType.CLIENT)
	@Override
	protected Model getRenderedModel(OctagramBlockEntity entity) {
		return ByakheeEntityRenderer.byakheeEntityModel;
	}
}
