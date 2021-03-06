package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class OpenSpellEditorPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "open_spellbook");

    public static void send(PlayerEntity player) {
        ServerPlayNetworking.send((ServerPlayerEntity) player, ID, new PacketByteBuf(Unpooled.buffer()));
    }
}
