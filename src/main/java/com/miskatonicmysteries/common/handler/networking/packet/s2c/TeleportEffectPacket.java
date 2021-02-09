package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.registry.MMMiscRegistries;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TeleportEffectPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "teleport");

    public static void send(Entity entity, BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getEntityId());
        PacketHandler.sendToPlayers(entity.world, pos, buf, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Entity entity = client.world.getEntityById(packetByteBuf.readInt());
        client.execute(() -> {
            if (entity instanceof LivingEntity) {
                client.world.playSound(entity.getBlockPos(), MMMiscRegistries.Sounds.TELEPORT_SOUND, SoundCategory.PLAYERS, 0.5F, 0.8F, false);
            }
            for (int i = 0; i < 5; i++) {
                client.world.addParticle(ParticleTypes.PORTAL, entity.getX() + client.world.getRandom().nextGaussian() * 0.5F * entity.getWidth(), entity.getY() + client.world.getRandom().nextGaussian() * 0.5F * entity.getHeight(), entity.getZ() + client.world.getRandom().nextGaussian() * 0.5F * entity.getWidth(), 0, 0, 0);
            }
        });
    }
}
