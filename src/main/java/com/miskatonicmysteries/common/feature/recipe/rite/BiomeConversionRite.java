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
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class BiomeConversionRite extends AscensionLockedRite {
	private static final int RUNNING_TIME = 24000;
	private static final int RANGE = 64;
	private static final int INTERVAL = RUNNING_TIME / RANGE;
	private final Function<World, Optional<RegistryEntry<Biome>>> biomeSupplier;

	public BiomeConversionRite(Identifier id, @Nullable Affiliation octagram, Function<World, Optional<RegistryEntry<Biome>>> biomeSupplier, String knowledge,
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
		if (octagram.tickCount <= RUNNING_TIME && octagram.tickCount % INTERVAL == 0) {
			int radius = getRadius(octagram);
			biomeSupplier.apply(octagram.getWorld()).ifPresent(biome -> {
				MiskatonicMysteriesAPI.spreadMaskedBiome(octagram.getWorld(), octagram.getPos(), radius, biome);
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
		return Math.min(octagram.tickCount / INTERVAL, RANGE);
	}

	@Override
	public boolean isPermanent(OctagramBlockEntity octagram) {
		return true;
	}
}
