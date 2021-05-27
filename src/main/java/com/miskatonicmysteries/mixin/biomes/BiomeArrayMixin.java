package com.miskatonicmysteries.mixin.biomes;

import com.miskatonicmysteries.api.interfaces.BiomeMask;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeArray.class)
public class BiomeArrayMixin implements BiomeMask {
    @Shadow @Final private IndexedIterable<Biome> field_25831;
    @Unique private static final int HORIZONTAL_SECTION_COUNT = (int)Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
    @Unique private Biome[] mmBiomeMasks;
    @Inject(method = "<init>(Lnet/minecraft/util/collection/IndexedIterable;[Lnet/minecraft/world/biome/Biome;)V", at = @At("TAIL"))
    private void init(IndexedIterable<Biome> indexedIterable, Biome[] biomes, CallbackInfo ci){
        mmBiomeMasks = new Biome[biomes.length];
    }
    @Inject(method = "getBiomeForNoiseGen", at = @At("HEAD"), cancellable = true)
    private void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir){
        Biome maskedBiome = MM_getMaskedBiome(biomeX, biomeZ);
        if (maskedBiome != null){
            cir.setReturnValue(maskedBiome);
        }
    }

    @Override
    public void MM_addBiomeMask(int biomeX, int biomeZ, Biome biome) {
        int i = biomeX & BiomeArray.HORIZONTAL_BIT_MASK;
        int j = biomeZ & BiomeArray.HORIZONTAL_BIT_MASK;
        this.mmBiomeMasks[j << HORIZONTAL_SECTION_COUNT | i] = biome;
    }

    @Override
    public @Nullable Biome MM_getMaskedBiome(int biomeX, int biomeZ) {
        int i = biomeX & BiomeArray.HORIZONTAL_BIT_MASK;
        int j = biomeZ & BiomeArray.HORIZONTAL_BIT_MASK;
        return this.mmBiomeMasks[j << HORIZONTAL_SECTION_COUNT | i];
    }

    @Override
    public int[] MM_masksToIntArray() {
        int[] is = new int[this.mmBiomeMasks.length];

        for(int i = 0; i < this.mmBiomeMasks.length; ++i) {
            Biome biome = this.mmBiomeMasks[i];
            if (biome == null){
                is[i] = -1;
            }else {
                is[i] = this.field_25831.getRawId(this.mmBiomeMasks[i]);
            }
        }

        return is;
    }

    @Override
    public void MM_setBiomeMask(IndexedIterable<Biome> biomesById, int[] mask) {
        for(int i = 0; i < mask.length; ++i) {
            this.mmBiomeMasks[i] = biomesById.get(mask[i]);
        }
    }
}
