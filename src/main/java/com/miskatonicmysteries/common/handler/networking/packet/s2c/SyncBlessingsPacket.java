package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.interfaces.Ascendant;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncBlessingsPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_blessings");

    public static void send(LivingEntity entity, Ascendant caster) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        CompoundTag blessingCompound = CapabilityUtil.writeBlessingData(caster, new CompoundTag());
        data.writeCompoundTag(blessingCompound);
        data.writeInt(entity.getEntityId());
        PacketHandler.sendToPlayers(entity.world, entity, data, ID);
        if (entity instanceof PlayerEntity) {
            PacketHandler.sendToPlayer((PlayerEntity) entity, data, ID);
        }
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        if (client.world != null) {
            CompoundTag tag = packetByteBuf.readCompoundTag();
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            if (entity != null) {
                client.execute(() -> Ascendant.of(entity).ifPresent(ascendant -> CapabilityUtil.readBlessingData(ascendant, tag)));
            }
        }
    }
}
