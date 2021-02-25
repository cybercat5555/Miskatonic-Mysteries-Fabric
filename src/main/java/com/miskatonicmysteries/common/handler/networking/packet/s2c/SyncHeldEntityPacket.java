package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.entity.EntityHolder;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncHeldEntityPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_held_entity");

    public static <T extends LivingEntity & EntityHolder> void send(T mob) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(mob.getEntityId());
        buf.writeInt(mob.getHeldEntity() == null ? -1 : mob.getHeldEntity().getEntityId());
        PacketHandler.sendToPlayers(mob.world, mob, buf, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Entity holdingEntity = client.world.getEntityById(packetByteBuf.readInt());
        int heldEntityId = packetByteBuf.readInt();
        Entity heldEntity = heldEntityId == -1 ? null : client.world.getEntityById(heldEntityId);
        if (holdingEntity instanceof EntityHolder) {
            client.execute(() -> ((EntityHolder) holdingEntity).setHeldEntity(heldEntity));
        }
    }
}
