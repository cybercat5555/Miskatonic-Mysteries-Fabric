package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.miskatonicmysteries.common.feature.interfaces.SpellCaster;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenSpellEditorPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "open_spellbook");

    public static void send(PlayerEntity player) {
        PacketHandler.sendToPlayer(player, new PacketByteBuf(Unpooled.buffer()), ID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        client.execute(() -> client.openScreen(new EditSpellScreen((SpellCaster) client.player)));
    }
}
