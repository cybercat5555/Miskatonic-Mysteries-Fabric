package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.client.vision.VisionHandler;
import com.miskatonicmysteries.client.vision.VisionSequence;
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

import io.netty.buffer.Unpooled;

public class VisionPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "vision");

	public static void send(ServerPlayerEntity player, Identifier vision) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeIdentifier(vision);
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
							  PacketSender sender) {
		VisionSequence sequence = VisionHandler.getSequence(packetByteBuf.readIdentifier());
		client.execute(() -> VisionHandler.setVisionSequence(client.player, sequence));
	}
}
