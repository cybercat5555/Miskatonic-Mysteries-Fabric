package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

import io.netty.buffer.Unpooled;

public class InfestWheatPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "infest");

	public static void send(ServerWorld world, BlockPos pos) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBlockPos(pos);
		PlayerLookup.tracking(world, pos).forEach(p -> ServerPlayNetworking.send(p, ID, data));
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
							  PacketSender sender) {
		if (client.world != null) {
			BlockPos sourcePos = packetByteBuf.readBlockPos();
			ClientWorld world = client.world;
			Random random = world.random;
			client.execute(() -> {
				world.playSound(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), MMSounds.ITEM_INFESTED_WHEAT_USE, SoundCategory.BLOCKS,
								1.0f, 1.0f, false);
				for (int i = 0; i < 10; i++) {
					world.addParticle(new DustParticleEffect(
										  new Vec3f(0, MathHelper.nextFloat(random, 0, 0.125F), MathHelper.nextFloat(random, 0, 0.2F)),
										  MathHelper.nextFloat(random, 1.5F, 2F)
									  ),
									  sourcePos.getX() + random.nextFloat(),
									  sourcePos.getY() + random.nextFloat(),
									  sourcePos.getZ() + random.nextFloat(),
									  0, 0, 0
					);
				}
			});
		}
	}
}
