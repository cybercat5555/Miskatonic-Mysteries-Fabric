package com.miskatonicmysteries.common.handler.networking.packet.s2c.toast;

import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.client.gui.toast.BlessingToast;
import com.miskatonicmysteries.common.registry.MMRegistries;
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

public class BlessingToastPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "blessing_toast");

	public static void send(ServerPlayerEntity player, Blessing blessing) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeIdentifier(blessing.getId());
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler,
							  PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.player != null) {
			Blessing blessing = MMRegistries.BLESSINGS.get(packetByteBuf.readIdentifier());
			client.execute(() -> {
				BlessingToast.show(client.getToastManager(), blessing);
			});
		}
	}
}
