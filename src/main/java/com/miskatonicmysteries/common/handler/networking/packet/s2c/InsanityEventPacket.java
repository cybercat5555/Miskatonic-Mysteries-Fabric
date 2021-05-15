package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InsanityEventPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "insanity_event");

    public static void send(PlayerEntity player, Identifier event) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeIdentifier(event);
        ServerPlayNetworking.send((ServerPlayerEntity) player, ID, data);
    }
}
