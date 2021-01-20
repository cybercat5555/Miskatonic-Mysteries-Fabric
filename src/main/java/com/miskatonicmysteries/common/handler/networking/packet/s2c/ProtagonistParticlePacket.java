package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMEntities;
import com.miskatonicmysteries.common.lib.MMParticles;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ProtagonistParticlePacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "protag_particle");

    public static void send(ProtagonistEntity entity) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeDouble(entity.getX());
        data.writeDouble(entity.getY());
        data.writeDouble(entity.getZ());
        PacketHandler.sendToPlayers(entity.world, entity, data, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Vec3d pos = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
        client.execute(() -> {
            for (int i = 0; i < 10; i++)
                client.world.addParticle(MMParticles.FLAME, pos.x + client.world.random.nextGaussian() * MMEntities.PROTAGONIST.getWidth(), pos.y + client.world.random.nextFloat() * MMEntities.PROTAGONIST.getHeight(), pos.z + client.world.random.nextGaussian() * MMEntities.PROTAGONIST.getWidth(), 1, 0, 0);
            for (int i = 0; i < 15; i++)
                client.world.addParticle(ParticleTypes.LARGE_SMOKE, pos.x + client.world.random.nextGaussian() * MMEntities.PROTAGONIST.getWidth(), pos.y + client.world.random.nextFloat() * MMEntities.PROTAGONIST.getHeight(), pos.z + client.world.random.nextGaussian() * MMEntities.PROTAGONIST.getWidth(), 0, 0, 0);
        });
    }
}
