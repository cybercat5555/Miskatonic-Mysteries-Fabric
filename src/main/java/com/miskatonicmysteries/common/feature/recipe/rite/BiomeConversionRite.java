package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Function;

public class BiomeConversionRite extends AscensionLockedRite {
    private final Function<ServerWorld, Biome> biomeSupplier;

    public BiomeConversionRite(Identifier id, @Nullable Affiliation octagram, Function<ServerWorld, Biome> biomeSupplier, String knowledge, int stage, Ingredient... ingredients) {
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
        if (octagram.getWorld() instanceof ServerWorld serverWorld /*&& serverWorld.getTime() % 20 == 0*/) {
            Random random = serverWorld.getRandom();
            int radius = getRadius(octagram);
            Biome biome = biomeSupplier.apply(serverWorld);
            BlockPos center = octagram.getPos();
            Iterable<BlockPos> blockPosIterable = BlockPos
                    .iterateRandomly(random, 1 + random.nextInt(3), center.getX() - radius, center.getY(), center
                            .getZ() - radius, center.getX() + radius, center.getY(), center.getZ() + radius);
            for (BlockPos targetPos : blockPosIterable) {
                MiskatonicMysteriesAPI.setBiomeMask(serverWorld, targetPos, biome);
            }
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
