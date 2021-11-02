package com.miskatonicmysteries.common.handler.networking.packet.s2c.toast;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.client.gui.toast.SpellEffectToast;
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

public class SpellEffectToastPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "spell_effect_toast");

	public static void send(ServerPlayerEntity player, SpellEffect effect) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeIdentifier(effect.getTextureLocation());
		data.writeString(effect.getTranslationString());
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler,
		PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.player != null) {
			Identifier effect = packetByteBuf.readIdentifier();
			String translation = packetByteBuf.readString();
			client.execute(() -> {
				SpellEffectToast.show(client.getToastManager(), effect, translation);
			});
		}
	}
}
