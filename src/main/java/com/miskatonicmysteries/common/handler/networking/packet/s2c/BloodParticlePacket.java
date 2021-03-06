package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.registry.MMParticles;
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
import net.minecraft.util.Identifier;

public class BloodParticlePacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "blood_particle");

    public static void send(LivingEntity entity) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(entity.getEntityId());
        PlayerLookup.tracking(entity).forEach(p -> ServerPlayNetworking.send(p, ID, data));
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        if (client.world != null) {
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            if (entity != null) {
                client.execute(() -> client.world.addParticle(MMParticles.DRIPPING_BLOOD, entity.getX() + client.world.getRandom().nextGaussian() * 0.5F * entity.getWidth(), entity.getY() + client.world.getRandom().nextGaussian() * 0.5F * entity.getHeight(), entity.getZ() + client.world.getRandom().nextGaussian() * 0.5F * entity.getWidth(), 0, 0, 0));
            }
        }
    }
}
