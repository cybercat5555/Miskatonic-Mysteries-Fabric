package com.miskatonicmysteries.common.handler.networking.packet.c2s;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.registry.MMMiscRegistries;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InvokeManiaPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "invoke_mania");

    public static void send(int amplifier, int duration) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(amplifier);
        data.writeInt(duration);
        ClientPlayNetworking.send(ID, data);
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packetByteBuf, PacketSender sender) {
        int amplifier = packetByteBuf.readInt();
        int duration = packetByteBuf.readInt();
        server.execute(() -> {
                    player.addStatusEffect(new StatusEffectInstance(MMMiscRegistries.StatusEffects.MANIA, duration, amplifier, false, true));
                    ((Sanity) player).setSanity(((Sanity) player).getSanity() - 5, false);
                    ((Sanity) player).setShocked(true);
                }
        );
    }
}
