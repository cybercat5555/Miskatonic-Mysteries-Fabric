package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.lib.util.Constants;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PacketHandler {
    public static final Identifier SANITY_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_expansion");
    public static final Identifier SANITY_REMOVE_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_remove_expansion");

    public static final Identifier INSANITY_EVENT_PACKET = new Identifier(Constants.MOD_ID, "insanity_event");

    public static void registerC2S(){

    }

    public static void registerS2C(){
        ClientSidePacketRegistry.INSTANCE.register(SANITY_EXPAND_PACKET, (packetContext, packetByteBuf) -> {
            String name = packetByteBuf.readString();
            int amount = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> ((ISanity) packetContext.getPlayer()).addSanityCapExpansion(name, amount));
        });

        ClientSidePacketRegistry.INSTANCE.register(SANITY_REMOVE_EXPAND_PACKET, (packetContext, packetByteBuf) -> {
            String name = packetByteBuf.readString();
            packetContext.getTaskQueue().execute(() -> ((ISanity) packetContext.getPlayer()).removeSanityCapExpansion(name));
        });

        ClientSidePacketRegistry.INSTANCE.register(INSANITY_EVENT_PACKET, ((packetContext, packetByteBuf) -> {
            Identifier id = packetByteBuf.readIdentifier();
            packetContext.getTaskQueue().execute(() -> InsanityEvent.INSANITY_EVENTS.get(id).execute(packetContext.getPlayer(), (ISanity) packetContext.getPlayer()));
        }));
    }

    public static void sendToPlayer(PlayerEntity player, PacketByteBuf data, Identifier packet){
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).networkHandler != null)
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, packet, data);
    }
}
