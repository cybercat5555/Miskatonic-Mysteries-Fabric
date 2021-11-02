package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.interfaces.BiomeMask;
import com.miskatonicmysteries.common.util.Constants;
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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.WorldChunk;

public class SyncBiomeMaskPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_biome_mask");

	public static void send(ServerPlayerEntity player, WorldChunk chunk, boolean resetColor) {
		if (chunk.getBiomeArray() instanceof BiomeMask) {
			PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
			data.writeLong(chunk.getPos().toLong());
			data.writeIntArray(((BiomeMask) chunk.getBiomeArray()).MM_masksToIntArray());
			data.writeBoolean(resetColor);
			ServerPlayNetworking.send(player, ID, data);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
		PacketSender sender) {
		if (client.world != null) {
			ChunkPos pos = new ChunkPos(packetByteBuf.readLong());
			int[] biomeMasks = packetByteBuf.readIntArray();
			boolean resetColor = packetByteBuf.readBoolean();
			client.execute(() -> {
				((BiomeMask) client.world.getChunk(pos.x, pos.z).getBiomeArray())
					.MM_setBiomeMask(client.world.getRegistryManager().get(Registry.BIOME_KEY), biomeMasks);
				if (resetColor) {
					client.world.resetChunkColor(pos);
				}
				for (int k = client.world.getBottomSectionCoord(); k < client.world.getTopSectionCoord(); ++k) {
					client.world.scheduleBlockRenders(pos.x, k, pos.z);
				}
			});
		}
	}
}
