package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RemoveExpansionPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sanity_remove_expansion");

	public static void send(PlayerEntity player, String name) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeString(name);
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
		PacketSender sender) {
		String name = packetByteBuf.readString();
		client.execute(() -> ((Sanity) client.player).removeSanityCapExpansion(name));
	}
}
