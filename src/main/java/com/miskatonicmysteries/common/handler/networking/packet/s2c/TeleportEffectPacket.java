package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class TeleportEffectPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "teleport");

    public static void send(Entity entity) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(entity.getId());
        PlayerLookup.tracking(entity).forEach(p -> ServerPlayNetworking.send(p, ID, data));
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayNetworking.send((ServerPlayerEntity) entity, ID, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        if (client.world != null) {
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            if (entity != null) {
                client.execute(() -> {
                    if (entity instanceof LivingEntity) {
                        client.world.playSound(entity
                                .getBlockPos(), MMSounds.TELEPORT_SOUND, SoundCategory.PLAYERS, 0.5F, 0.8F, false);
                    }
                    for (int i = 0; i < 5; i++) {
                        client.world.addParticle(ParticleTypes.PORTAL, entity.getX() + client.world.getRandom()
                                .nextGaussian() * 0.5F * entity.getWidth(), entity.getY() + client.world.getRandom()
                                .nextGaussian() * 0.5F * entity.getHeight(), entity.getZ() + client.world.getRandom()
                                .nextGaussian() * 0.5F * entity.getWidth(), 0, 0, 0);
                    }
                });
            }
        }
    }
}
