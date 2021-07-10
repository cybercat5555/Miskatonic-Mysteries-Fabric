package com.miskatonicmysteries.common.handler.networking.packet;

import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SpellPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "spell");

    public static void send(LivingEntity caster, NbtCompound spellTag, int intensityMod) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeNbt(spellTag);
        data.writeInt(intensityMod);
        data.writeInt(caster.getId());
        PlayerLookup.tracking(caster).forEach(p -> ServerPlayNetworking.send(p, ID, data));
        if (caster instanceof ServerPlayerEntity){
            ServerPlayNetworking.send((ServerPlayerEntity) caster, ID, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendFromClientPlayer(ClientPlayerEntity caster, NbtCompound spellTag) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeNbt(spellTag);
        data.writeInt(caster.getId());
        ClientPlayNetworking.send(ID, data);
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Spell spell = Spell.fromTag(packetByteBuf.readNbt());
        spell.intensity += packetByteBuf.readInt();
        Entity entity = client.world.getEntityById(packetByteBuf.readInt());
        if (entity instanceof LivingEntity) {
            client.execute(() -> spell.cast((LivingEntity) entity));
        }
    }

    public static void handleFromClient(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packetByteBuf, PacketSender sender) {
        Spell spell = Spell.fromTag(packetByteBuf.readNbt());
        if (spell != null) {
            server.execute(() -> spell.cast(player));
        }
    }
}
