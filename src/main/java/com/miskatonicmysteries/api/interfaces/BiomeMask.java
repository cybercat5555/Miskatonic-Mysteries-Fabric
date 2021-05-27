package com.miskatonicmysteries.api.interfaces;

import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public interface BiomeMask {
    void MM_addBiomeMask(int x, int z, Biome biome);

    @Nullable Biome MM_getMaskedBiome(int x, int z);

    int[] MM_masksToIntArray();

    void MM_setBiomeMask(IndexedIterable<Biome> biomesById, int[] mask);
}
