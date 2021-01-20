package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class EffectParticlePacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "effect_particle");

    public static void send(HasturCultistEntity entity) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeDouble(entity.getX());
        data.writeDouble(entity.getY() + 2.3F);
        data.writeDouble(entity.getZ());
        data.writeInt(entity.currentSpell.effect.getColor(entity));
        PacketHandler.sendToPlayers(entity.world, entity, data, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Vec3d pos = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
        Vec3d rgb = Vec3d.unpackRgb(packetByteBuf.readInt());
        client.execute(() -> {
            client.world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.x + client.world.random.nextGaussian() * 0.75F, pos.y + client.world.random.nextGaussian(), pos.z + client.world.random.nextGaussian() * 0.75F, rgb.x, rgb.y, rgb.z);
        });
    }
}
