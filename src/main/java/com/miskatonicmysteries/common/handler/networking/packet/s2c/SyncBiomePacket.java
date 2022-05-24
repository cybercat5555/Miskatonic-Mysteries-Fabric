package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.mixin.biomes.ChunkSectionAccessor;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkSection;

public class SyncBiomePacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_biome");

	public static void send(ServerPlayerEntity player, ChunkSectionAccessor accessor, BlockPos pos) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBlockPos(pos);
		accessor.getBiomeContainer().writePacket(data);
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.world != null) {
			BlockPos pos = packetByteBuf.readBlockPos();
			ChunkPos chunkPos = new ChunkPos(pos);
			ChunkSection section = client.world.getChunk(pos).getSection(client.world.getSectionIndex(pos.getY()));
			((ChunkSectionAccessor) section).getBiomeContainer().readPacket(packetByteBuf);
			if (MiskatonicMysteries.config.client.forceChunkColorUpdates) {
				client.execute(() -> {
					client.world.resetChunkColor(chunkPos);
					for (int k = client.world.getBottomSectionCoord(); k < client.world.getTopSectionCoord(); ++k) {
						client.world.scheduleBlockRenders(chunkPos.x, k, chunkPos.z);
					}
				});
			}
		}
	}
}
