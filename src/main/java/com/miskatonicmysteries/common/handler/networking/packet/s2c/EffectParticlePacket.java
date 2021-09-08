package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.entity.util.CastingMob;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class EffectParticlePacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "effect_packet");

    public static <T extends PathAwareEntity & CastingMob> void send(T castingMob) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeDouble((castingMob.getX()));
        data.writeDouble(castingMob.getY() + 2.3F);
        data.writeDouble(castingMob.getZ());
        data.writeInt(castingMob.getCurrentSpell().effect.getColor(castingMob));
        if (castingMob.world instanceof ServerWorld) {
            PlayerLookup.tracking(castingMob).forEach(p -> ServerPlayNetworking.send(p, ID, data));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Vec3d pos = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
        Vec3d rgb = Vec3d.unpackRgb(packetByteBuf.readInt());
        client.execute(() -> {
            client.world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.x + client.world.random.nextGaussian() * 0.75F, pos.y + client.world.random.nextGaussian(), pos.z + client.world.random.nextGaussian() * 0.75F, rgb.x, rgb.y, rgb.z);
        });
    }
}
