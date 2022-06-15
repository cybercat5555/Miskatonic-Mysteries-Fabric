package com.miskatonicmysteries.common.handler.networking.packet.c2s;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import io.netty.buffer.Unpooled;

public class ClientRiteInputPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "rite_input");

	public static void send(ClientPlayerEntity player) {
		for (BlockPos blockPos : BlockPos.iterateOutwards(player.getBlockPos(), 1, 2, 1)) {
			player.world.getBlockEntity(player.getBlockPos(), MMObjects.OCTAGRAM_BLOCK_ENTITY_TYPE).ifPresent(blockEntity -> {
				PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
				data.writeBlockPos(blockPos);
				ClientPlayNetworking.send(ID, data);
			});
		}
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
							  PacketByteBuf packetByteBuf, PacketSender sender) {
		BlockPos pos = packetByteBuf.readBlockPos();
		server.execute(() -> {
			BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
			if (blockEntity instanceof OctagramBlockEntity octagramBlockEntity) {
				octagramBlockEntity.setFlag(1, true);
				octagramBlockEntity.sync(player.world, pos);
			}
		});
	}
}
