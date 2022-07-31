package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.RiteCondition;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;

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
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.netty.buffer.Unpooled;

public class SyncRiteConditionsPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_rite_cond");

	public static void send(PlayerEntity player, boolean clear, BlockPos octagramPos, List<Integer> conditionIndexes, RiteRecipe rite) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeBoolean(clear);
		data.writeBlockPos(octagramPos);
		if (!clear) {
			data.writeIdentifier(rite.rite.getId());
			data.writeInt(conditionIndexes.size());
			for (Integer index : conditionIndexes) {
				data.writeInt(index);
			}
		}
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, data);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
							  PacketSender sender) {
		boolean clear = packetByteBuf.readBoolean();
		BlockPos pos = packetByteBuf.readBlockPos();
		if (client.world.getBlockEntity(pos) instanceof OctagramBlockEntity) {
			if (!clear) {
				Rite rite = MMRegistries.RITES.get(packetByteBuf.readIdentifier());
				LinkedHashMap<RiteCondition, Boolean> conditionMap = new LinkedHashMap<>();
				int size = packetByteBuf.readInt();
				List<Integer> badConditions = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					badConditions.add(packetByteBuf.readInt());
				}
				for (int i = 0; i < rite.startConditions.length; i++) {
					conditionMap.put(rite.startConditions[i], badConditions.contains(i));
				}
				client.execute(() -> {
					OctagramBlockEntity blockEntity = (OctagramBlockEntity) client.world.getBlockEntity(pos);
					blockEntity.clientConditions = conditionMap;
					blockEntity.preparedRite = rite;
				});
			} else {
				client.execute(() -> {
					OctagramBlockEntity blockEntity = (OctagramBlockEntity) client.world.getBlockEntity(pos);
					blockEntity.clientConditions = new LinkedHashMap<>();
					blockEntity.preparedRite = null;
				});
			}
		}
	}
}
