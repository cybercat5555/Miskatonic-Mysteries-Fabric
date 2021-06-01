package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.Knowledge;
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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncKnowledgePacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_knowledge");

    public static void send(LivingEntity entity, Knowledge knowledge) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        CompoundTag knowledgeCompound = new CompoundTag();
        ListTag knowledgeList = new ListTag();
        for (String knowledgeId : knowledge.getKnowledge()) {
            knowledgeList.add(StringTag.of(knowledgeId));
        }
        knowledgeCompound.put(Constants.NBT.KNOWLEDGE, knowledgeList);
        data.writeCompoundTag(knowledgeCompound);
        if (entity instanceof ServerPlayerEntity) { //sync only on this client for display purposes
            ServerPlayNetworking.send((ServerPlayerEntity) entity, ID, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        if (client.player != null) {
            ListTag knowledgeList = packetByteBuf.readCompoundTag().getList(Constants.NBT.KNOWLEDGE, 8);
            client.execute(() -> {
                Knowledge.of(client.player).ifPresent(knowledge -> {
                    knowledge.clearKnowledge();
                    for (Tag tag : knowledgeList) {
                        knowledge.addKnowledge(tag.asString());
                    }
                });
            });
        }
    }
}
