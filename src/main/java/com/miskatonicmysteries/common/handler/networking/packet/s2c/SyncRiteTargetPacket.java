package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SyncRiteTargetPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_rite_target");

    public static void send(Entity entity, OctagramBlockEntity blockEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getEntityId());
        buf.writeLong(blockEntity.getPos().asLong());
        PacketHandler.sendToPlayers(blockEntity.getWorld(), blockEntity, buf, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Entity entity = client.world.getEntityById(packetByteBuf.readInt());
        BlockPos pos = BlockPos.fromLong(packetByteBuf.readLong());
        if (client.world.getBlockEntity(pos) instanceof OctagramBlockEntity) {
            client.execute(() -> ((OctagramBlockEntity) client.world.getBlockEntity(pos)).targetedEntity = entity);
        }
    }
}
