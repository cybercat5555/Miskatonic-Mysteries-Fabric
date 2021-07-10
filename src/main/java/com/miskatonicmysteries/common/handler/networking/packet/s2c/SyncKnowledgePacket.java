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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncKnowledgePacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_knowledge");

    public static void send(LivingEntity entity, Knowledge knowledge) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        NbtCompound knowledgeCompound = new NbtCompound();
        NbtList knowledgeList = new NbtList();
        for (String knowledgeId : knowledge.getKnowledge()) {
            knowledgeList.add(NbtString.of(knowledgeId));
        }
        knowledgeCompound.put(Constants.NBT.KNOWLEDGE, knowledgeList);
        data.writeNbt(knowledgeCompound);
        if (entity instanceof ServerPlayerEntity) { //sync only on this client for display purposes
            ServerPlayNetworking.send((ServerPlayerEntity) entity, ID, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        if (client.player != null) {
            NbtList knowledgeList = packetByteBuf.readNbt().getList(Constants.NBT.KNOWLEDGE, 8);
            client.execute(() -> {
                Knowledge.of(client.player).ifPresent(knowledge -> {
                    knowledge.clearKnowledge();
                    for (NbtElement tag : knowledgeList) {
                        knowledge.addKnowledge(tag.asString());
                    }
                });
            });
        }
    }
}
