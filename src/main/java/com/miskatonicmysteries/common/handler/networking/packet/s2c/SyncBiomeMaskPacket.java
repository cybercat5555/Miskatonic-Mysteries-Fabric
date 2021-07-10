package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.BiomeMask;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.WorldChunk;

public class SyncBiomeMaskPacket {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "sync_biome_mask");

    public static void send(ServerPlayerEntity player, WorldChunk chunk) {
        if (chunk.getBiomeArray() instanceof BiomeMask) {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeLong(chunk.getPos().toLong());
            data.writeIntArray(((BiomeMask) chunk.getBiomeArray()).MM_masksToIntArray());
            ServerPlayNetworking.send(player, ID, data);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf, PacketSender sender) {
        if (client.world != null) {
            ChunkPos pos = new ChunkPos(packetByteBuf.readLong());
            int[] biomeMasks = packetByteBuf.readIntArray();
            client.execute(() -> ((BiomeMask) client.world.getChunk(pos.x, pos.z).getBiomeArray()).MM_setBiomeMask(client.world.getRegistryManager().get(Registry.BIOME_KEY), biomeMasks));
        }
    }
}
