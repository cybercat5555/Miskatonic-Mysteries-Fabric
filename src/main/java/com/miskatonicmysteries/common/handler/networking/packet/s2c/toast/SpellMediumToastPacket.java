package com.miskatonicmysteries.common.handler.networking.packet.s2c.toast;

import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.client.gui.toast.SpellMediumToast;
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

public class SpellMediumToastPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "spell_medium_toast");

	public static void send(ServerPlayerEntity player, SpellMedium medium) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeIdentifier(medium.getTextureLocation());
		data.writeString(medium.getTranslationString());
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler,
							  PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.player != null) {
			Identifier medium = packetByteBuf.readIdentifier();
			String translation = packetByteBuf.readString();
			client.execute(() -> {
				SpellMediumToast.show(client.getToastManager(), medium, translation);
			});
		}
	}
}
