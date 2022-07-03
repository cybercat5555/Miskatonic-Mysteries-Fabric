package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import io.netty.buffer.Unpooled;

public class SmokeEffectPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "smoke");

	public static void send(Entity entity) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeInt(entity.getId());
		PlayerLookup.tracking(entity).forEach(p -> ServerPlayNetworking.send(p, ID, data));
		if (entity instanceof ServerPlayerEntity) {
			ServerPlayNetworking.send((ServerPlayerEntity) entity, ID, data);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
							  PacketSender sender) {
		if (client.world != null) {
			Entity entity = client.world.getEntityById(packetByteBuf.readInt());
			if (entity != null) {
				client.execute(() -> {
					for (int i = 0; i < 10; i++) {
						client.world.addParticle(ParticleTypes.LARGE_SMOKE,
												 entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1),
												 0, 0, 0);
					}
				});
			}
		}
	}
}
