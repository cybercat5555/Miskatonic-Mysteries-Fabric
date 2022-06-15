package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.util.BiomeUtil;
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

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.Unpooled;

public class SyncBiomeReversionPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_biome_reversion");

	public static void send(ServerPlayerEntity player, List<BlockPos> poses) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeInt(poses.size());
		for (BlockPos pos : poses) {
			data.writeBlockPos(pos);
		}
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.world != null) {
			List<BlockPos> poses = new ArrayList<>();
			int size = packetByteBuf.readInt();
			for (int i = 0; i < size; i++) {
				poses.add(packetByteBuf.readBlockPos());
			}
			client.execute(() -> {
				for (BlockPos pos : poses) {
					BiomeUtil.revertBiome(client.world, pos);
				}
				BiomeUtil.updateBiomeColor(client.world, poses);
			});
		}
	}
}
