package com.miskatonicmysteries.mixin.biomes;

import net.minecraft.world.ChunkSerializer;

import org.spongepowered.asm.mixin.Mixin;

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
