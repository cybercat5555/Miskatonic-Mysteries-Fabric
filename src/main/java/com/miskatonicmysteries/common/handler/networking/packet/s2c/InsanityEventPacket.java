package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
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

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Identifier id = packetByteBuf.readIdentifier();
        if (client.player != null) {
            client.execute(() -> MMRegistries.INSANITY_EVENTS.get(id).execute(client.player, (Sanity) client.player));
        }
    }
}
