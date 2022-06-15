package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.recipe.rite.BiomeConversionRite;
import com.miskatonicmysteries.common.util.Constants;

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
import net.minecraft.util.registry.Registry;

import io.netty.buffer.Unpooled;

public class SyncBiomeSpreadPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_biome_spread");

	public static void send(ServerPlayerEntity player, BlockPos root, int biomeId, int radius) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBlockPos(root);
		data.writeInt(biomeId);
		data.writeInt(radius);
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.world != null) {
			BlockPos root = packetByteBuf.readBlockPos();
			int biomeId = packetByteBuf.readInt();
			int radius = packetByteBuf.readInt();
			client.world.getRegistryManager().get(Registry.BIOME_KEY).getEntry(biomeId).ifPresent(entry -> {
				client.execute(() -> {
					BiomeConversionRite.spreadBiome(client.world, root, radius, entry);
				});
			});
		}
	}
}
