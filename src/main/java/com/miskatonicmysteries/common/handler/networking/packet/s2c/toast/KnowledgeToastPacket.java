package com.miskatonicmysteries.common.handler.networking.packet.s2c.toast;

import com.miskatonicmysteries.api.interfaces.Knowledge;
import com.miskatonicmysteries.client.gui.toast.KnowledgeToast;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class KnowledgeToastPacket {
	public static final Identifier ID = new Identifier(Constants.MOD_ID, "knowledge_toast");

	public static void send(ServerPlayerEntity player, String knowledge) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeString(knowledge);
		ServerPlayNetworking.send(player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler,
							  PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.player != null) {
			String knowledge = packetByteBuf.readString();
			client.execute(() -> {
				KnowledgeToast.show(client.getToastManager(), knowledge);
			});
		}
	}
}
