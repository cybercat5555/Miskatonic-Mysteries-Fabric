package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.NbtUtil;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncBlessingsPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_blessings");

    public static void send(LivingEntity entity, Ascendant caster) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        CompoundTag blessingCompound = NbtUtil.writeBlessingData(caster, new CompoundTag());
        data.writeCompoundTag(blessingCompound);
        data.writeInt(entity.getEntityId());
        PlayerLookup.tracking(entity).forEach(p -> ServerPlayNetworking.send(p, ID, data));
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayNetworking.send((ServerPlayerEntity) entity, ID, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        if (client.world != null) {
            CompoundTag tag = packetByteBuf.readCompoundTag();
            Entity entity = client.world.getEntityById(packetByteBuf.readInt());
            if (entity != null) {
                client.execute(() -> Ascendant.of(entity).ifPresent(ascendant -> NbtUtil.readBlessingData(ascendant, tag)));
            }
        }
    }
}
