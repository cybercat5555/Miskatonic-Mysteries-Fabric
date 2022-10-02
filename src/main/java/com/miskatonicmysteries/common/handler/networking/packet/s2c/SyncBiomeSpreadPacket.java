package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import io.netty.buffer.Unpooled;

public class SyncBiomeSpreadPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_biome_spread");

	public static void send(ServerWorld world, BlockPos root, int biomeId, int radius) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBlockPos(root);
		data.writeInt(biomeId);
		data.writeInt(radius);
		PlayerLookup.tracking(world, root).forEach(player -> ServerPlayNetworking.send(player, ID, data));
	}
}
