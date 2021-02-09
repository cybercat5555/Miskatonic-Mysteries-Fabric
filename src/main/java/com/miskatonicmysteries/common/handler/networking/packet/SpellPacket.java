package com.miskatonicmysteries.common.handler.networking.packet;

import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SpellPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "spell");

    public static void send(LivingEntity caster, CompoundTag spellTag) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeCompoundTag(spellTag);
        data.writeInt(caster.getEntityId());
        PacketHandler.sendToPlayers(caster.world, caster, data, ID);
    }

    public static void sendFromClientPlayer(ClientPlayerEntity caster, CompoundTag spellTag) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeCompoundTag(spellTag);
        data.writeInt(caster.getEntityId());
        ClientPlayNetworking.send(ID, data);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        CompoundTag spellTag = packetByteBuf.readCompoundTag();
        Entity entity = client.world.getEntityById(packetByteBuf.readInt());
        if (entity instanceof LivingEntity) {
            client.execute(() -> Spell.fromTag(spellTag).cast((LivingEntity) entity));
        }
    }

    public static void handleFromClient(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Spell spell = Spell.fromTag(packetByteBuf.readCompoundTag());
        if (spell != null) {
            server.execute(() -> spell.cast(player));
        }
    }
}
