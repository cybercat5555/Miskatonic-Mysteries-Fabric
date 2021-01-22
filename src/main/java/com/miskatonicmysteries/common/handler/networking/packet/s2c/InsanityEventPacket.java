package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.interfaces.Sanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class InsanityEventPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "insanity_event");

    public static void send(PlayerEntity playerEntity, Identifier event) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeIdentifier(event);
        PacketHandler.sendToPlayer(playerEntity, data, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Identifier id = packetByteBuf.readIdentifier();
        client.execute(() -> InsanityEvent.INSANITY_EVENTS.get(id).execute(client.player, (Sanity) client.player));
    }
}
