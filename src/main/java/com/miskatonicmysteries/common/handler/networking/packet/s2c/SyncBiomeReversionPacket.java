package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.recipe.rite.BiomeReversionRite;
import com.miskatonicmysteries.common.util.BiomeUtil;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.Unpooled;

public class SyncBiomeReversionPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_biome_reversion");

	public static void send(ServerWorld world, BlockPos tracking, List<BlockPos> poses) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBoolean(true);
		data.writeInt(poses.size());
		for (BlockPos pos : poses) {
			data.writeBlockPos(pos);
		}
		PlayerLookup.tracking(world, tracking).forEach(player -> ServerPlayNetworking.send(player, ID, data));
	}

	public static void send(ServerWorld world, BlockPos tracking, int radius) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBoolean(false);
		data.writeInt(radius);
		data.writeBlockPos(tracking);
		PlayerLookup.tracking(world, tracking).forEach(player -> ServerPlayNetworking.send(player, ID, data));
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
		if (client.world != null) {
			boolean usesBlockPoses = packetByteBuf.readBoolean();
			List<BlockPos> poses = new ArrayList<>();
			int i = packetByteBuf.readInt();;
			if (usesBlockPoses) {
				for (int j = 0; j < i; j++) {
					poses.add(packetByteBuf.readBlockPos());
				}
			}else{
				poses.add(packetByteBuf.readBlockPos());
			}
			client.execute(() -> {
				if (usesBlockPoses) {
					for (BlockPos pos : poses) {
						BiomeUtil.revertBiome(client.world, pos);
					}
					BiomeUtil.updateBiomeColor(client.world, poses);
				}else {
					BiomeReversionRite.revertBiome(client.world, poses.get(0), i);
				}
			});
		}
	}
}
