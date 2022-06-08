package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.util.BiomeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI.S;

public class BiomeConversionRite extends AscensionLockedRite {
	private static final int RUNNING_TIME = 24000 / 60;
	private static final int RANGE = 16;
	private static final int INTERVAL = RUNNING_TIME / RANGE;
	protected final Function<World, Optional<RegistryEntry<Biome>>> biomeSupplier;

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
		biomeSpreadTick(octagram, octagram.tickCount);
	}

	protected void biomeSpreadTick(OctagramBlockEntity octagram, int tickCount) {
		if (tickCount <= RUNNING_TIME && tickCount % INTERVAL == 0) {
			int radius = getRadius(octagram);
			biomeSupplier.apply(octagram.getWorld()).ifPresent(biome -> {
				spreadBiome(octagram.getWorld(), octagram.getPos(), radius, biome);
			});
			if (octagram.getWorld() instanceof ServerWorld serverWorld) {
				MMDimensionalWorldState.get(serverWorld).setBiomeKnot(octagram.getPos(), radius * 4, true, true);
			}
		}
	}

	protected int getRadius(OctagramBlockEntity octagram) {
		return Math.min(octagram.tickCount / INTERVAL, RANGE);
	}

	@Override
	public void onCancelled(OctagramBlockEntity octagram) {
		super.onCancelled(octagram);
		if (octagram.getWorld() instanceof ServerWorld serverWorld) {
			MMDimensionalWorldState.get(serverWorld).setBiomeKnot(octagram.getPos(), getRadius(octagram) * 4, false, true);
		}
	}

	@Override
	public boolean isPermanent(OctagramBlockEntity octagram) {
		return true;
	}

	public static void spreadBiome(World world, BlockPos root, int radius, RegistryEntry<Biome> biome) {
		double radiusPower = Math.pow(radius, 2);
		List<BlockPos> changedBlocks = new ArrayList<>();
		int biomeX = BiomeCoords.fromBlock(root.getX());
		int biomeY = BiomeCoords.fromBlock(root.getY());
		int biomeZ = BiomeCoords.fromBlock(root.getZ());
		for (int x = -radius; x < radius; x++) {
			for (int z = -radius; z < radius; z++) {
				for (int y = -radius; y < radius; y++) {
					BlockPos changedPos = root.add(x * 4, y * 4, z * 4);
					double sqD = x * x + y * y + z * z;
					if (sqD <= radiusPower) {
						BiomeUtil.setBiome(world, world.getWorldChunk(changedPos), biomeX + x, biomeY + y, biomeZ + z, biome);
						changedBlocks.add(changedPos);
					}
				}
			}
		}
		BiomeUtil.updateBiomeColor(world, changedBlocks);
	}
}
