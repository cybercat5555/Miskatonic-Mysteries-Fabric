package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class BiomeConversionRite extends AscensionLockedRite {

	private final Function<ServerWorld, Optional<RegistryEntry<Biome>>> biomeSupplier;

	public BiomeConversionRite(Identifier id, @Nullable Affiliation octagram, Function<ServerWorld, Optional<RegistryEntry<Biome>>> biomeSupplier, String knowledge,
		int stage, Ingredient... ingredients) {
		super(id, octagram, knowledge, 0, stage, ingredients);
		this.biomeSupplier = biomeSupplier;
	}

	@Override
	public void onStart(OctagramBlockEntity octagram) {
		octagram.permanentRiteActive = true;
		octagram.clear();
		octagram.markDirty();
	}

	@Override
	public boolean isFinished(OctagramBlockEntity octagram) {
		return false;
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		super.tick(octagram);
		if (octagram.getWorld() instanceof ServerWorld serverWorld && serverWorld.getTime() % 20 == 0) {
			Random random = serverWorld.getRandom();
			int radius = getRadius(octagram);
			biomeSupplier.apply(serverWorld).ifPresent(biome -> {
				MiskatonicMysteriesAPI.spreadMaskedBiome(serverWorld, octagram.getPos(), radius, 1 + random.nextInt(3), biome);
			});
		}
	}

	@Override
	public void onCancelled(OctagramBlockEntity octagram) {
		super.onCancelled(octagram);
		if (octagram.getWorld() instanceof ServerWorld serverWorld) {
			MMDimensionalWorldState.get(serverWorld).deactivateKnot(octagram.getPos());
		}
	}

	private int getRadius(OctagramBlockEntity octagram) {
		return Math.min((int) (Math.sqrt(octagram.tickCount / 4.0)), MiskatonicMysteries.config.world.simulacrumBiomeRadiusCap);
	}

	@Override
	public boolean isPermanent(OctagramBlockEntity octagram) {
		return true;
	}
}
