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
/* TODO BIOME
	@Inject(method = "serialize", at = @At("RETURN"), cancellable = true)
	private static void serialize(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir) {
		if (chunk.getBiomeArray() instanceof BiomeMask mask) {
			NbtCompound tag = cir.getReturnValue();
			tag.putIntArray(Constants.NBT.BIOME_MASK, mask.MM_masksToIntArray());
			cir.setReturnValue(tag);
		}
	}

	@Inject(method = "deserialize", at = @At("RETURN"), cancellable = true)
	private static void deserialize(ServerWorld world, PointOfInterestStorage poiStorage, ChunkPos chunkPos, NbtCompound nbt, CallbackInfoReturnable<ProtoChunk> cir) {
		ProtoChunk chunk = cir.getReturnValue();
		if (chunk.getBiomeArray() instanceof BiomeMask mask && nbt.contains(Constants.NBT.BIOME_MASK)) {
			mask.MM_setBiomeMask(world.getRegistryManager().get(Registry.BIOME_KEY), nbt.getIntArray(Constants.NBT.BIOME_MASK));
			cir.setReturnValue(chunk);
		}
	}

 */
}
