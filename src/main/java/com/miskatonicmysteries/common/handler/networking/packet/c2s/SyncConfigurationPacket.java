package com.miskatonicmysteries.common.handler.networking.packet.c2s;

import com.miskatonicmysteries.api.interfaces.HasConfigurablePredicate;
import com.miskatonicmysteries.common.handler.predicate.ConfigurablePredicate;
import com.miskatonicmysteries.common.handler.predicate.ConfigurableTargetPredicate;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import io.netty.buffer.Unpooled;

public class SyncConfigurationPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_config");

	@Environment(EnvType.CLIENT)
	public static <T extends Entity & HasConfigurablePredicate> void send(T configurable, ConfigurablePredicate predicate) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeInt(configurable.getId());
		data.writeNbt(predicate.writeNbt(new NbtCompound()));
		ClientPlayNetworking.send(ID, data);
	}

	public static void handleFromClient(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
										PacketByteBuf packetByteBuf, PacketSender sender) {

		int id = packetByteBuf.readInt();
		ConfigurablePredicate config = ConfigurableTargetPredicate.fromNbt(packetByteBuf.readNbt());
		Entity entity = player.world.getEntityById(id);
		if (entity instanceof HasConfigurablePredicate h) {
			server.execute(() -> h.setPredicate(config));
		}
	}
}
