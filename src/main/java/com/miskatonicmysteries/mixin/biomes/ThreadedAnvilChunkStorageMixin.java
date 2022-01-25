package com.miskatonicmysteries.mixin.biomes;

import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncBiomeMaskPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {

	@Inject(method = "sendChunkDataPackets", at = @At("TAIL"))
	private void sendChunkDataPackets(ServerPlayerEntity player, MutableObject<ChunkDataS2CPacket> cachedDataPacket, WorldChunk chunk, CallbackInfo ci) {
		SyncBiomeMaskPacket.send(player, chunk, false);
	}
}
