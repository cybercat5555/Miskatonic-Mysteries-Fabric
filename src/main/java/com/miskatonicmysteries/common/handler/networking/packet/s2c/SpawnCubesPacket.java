package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import io.netty.buffer.Unpooled;

public class SpawnCubesPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "cubes_effect_packet");

	public static void send(Entity entity) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeDouble((entity.getX()));
		data.writeDouble(entity.getY());
		data.writeDouble(entity.getZ());
		if (entity.world instanceof ServerWorld) {
			PlayerLookup.tracking(entity).forEach(p -> ServerPlayNetworking.send(p, ID, data));
		}
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
							  PacketSender sender) {
		Vec3d pos = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
		client.execute(() -> {
			for (int i = 0; i < 5; i++) {
				client.world.addParticle(MMParticles.WEIRD_CUBE, pos.x, pos.y, pos.z,
										 client.world.random.nextGaussian() * 0.1,
										 client.world.random.nextGaussian() * 0.1,
										 client.world.random.nextGaussian() * 0.1);
			}
		});
	}
}
