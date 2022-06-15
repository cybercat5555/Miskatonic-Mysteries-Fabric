package com.miskatonicmysteries.common.handler.networking.packet;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.NbtUtil;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import io.netty.buffer.Unpooled;

public class SyncSpellCasterDataPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_spell");

	public static void send(boolean client, PlayerEntity user, SpellCaster caster) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		NbtCompound spellCompound = NbtUtil.writeSpellData(caster, new NbtCompound());
		data.writeNbt(spellCompound);
		if (client) {
			ClientPlayNetworking.send(ID, data);
		} else {
			ServerPlayNetworking.send((ServerPlayerEntity) user, ID, data);
		}
	}

	public static void handleFromClient(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
										PacketByteBuf packetByteBuf, PacketSender sender) {
		NbtCompound tag = packetByteBuf.readNbt();
		server.execute(() -> SpellCaster.of(player).ifPresent(caster -> NbtUtil.readSpellData(caster, tag)));
	}
}
