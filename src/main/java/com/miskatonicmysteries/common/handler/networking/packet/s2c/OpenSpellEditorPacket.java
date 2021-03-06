package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class OpenSpellEditorPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "open_spellbook");

    public static void send(PlayerEntity player) {
        ServerPlayNetworking.send((ServerPlayerEntity) player, ID, new PacketByteBuf(Unpooled.buffer()));
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        client.execute(() -> client.openScreen(new EditSpellScreen((SpellCaster) client.player)));
    }
}
