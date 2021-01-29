package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.blessing.Blessing;
import com.miskatonicmysteries.common.feature.interfaces.Ascendant;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ModifyBlessingPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "modify_blessings");

    public static void send(PlayerEntity player, Blessing blessing, boolean add) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeString(blessing.getId().toString());
        data.writeBoolean(add);
        PacketHandler.sendToPlayer(player, data, ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Blessing blessing = Blessing.BLESSINGS.getOrDefault(new Identifier(packetByteBuf.readString()), null);
        boolean add = packetByteBuf.readBoolean();
        if (blessing != null) {
            client.execute(() -> Ascendant.of(client.player).ifPresent(ascendant -> {
                if (add) {
                    ascendant.addBlessing(blessing);
                } else {
                    ascendant.removeBlessing(blessing);
                }
            }));
        }
    }
}
