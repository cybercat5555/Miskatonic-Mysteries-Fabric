package com.miskatonicmysteries.mixin.biomes;

import com.miskatonicmysteries.api.interfaces.BiomeMask;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {
    @Inject(method = "serialize", at = @At("RETURN"), cancellable = true)
    private static void serialize(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir){
        if (chunk.getBiomeArray() instanceof BiomeMask){
            NbtCompound tag = cir.getReturnValue();
            tag.putIntArray(Constants.NBT.BIOME_MASK, ((BiomeMask) chunk.getBiomeArray()).MM_masksToIntArray());
            cir.setReturnValue(tag);
        }
    }

    @Inject(method = "deserialize", at = @At("RETURN"), cancellable = true)
    private static void deserialize(ServerWorld world, StructureManager structureManager, PointOfInterestStorage poiStorage, ChunkPos pos, NbtCompound tag, CallbackInfoReturnable<ProtoChunk> cir){
        ProtoChunk chunk = cir.getReturnValue();
        if (chunk.getBiomeArray() instanceof BiomeMask && tag.contains(Constants.NBT.BIOME_MASK)){
            ((BiomeMask) chunk.getBiomeArray()).MM_setBiomeMask(world.getRegistryManager().get(Registry.BIOME_KEY), tag.getIntArray(Constants.NBT.BIOME_MASK));
            cir.setReturnValue(chunk);
        }
    }
}
