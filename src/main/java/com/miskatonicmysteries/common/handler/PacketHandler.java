package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.lib.util.Constants;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PacketHandler {
    public static final Identifier SANITY_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_expansion");
    public static final Identifier SANITY_REMOVE_EXPAND_PACKET = new Identifier(Constants.MOD_ID, "sanity_remove_expansion");

    public static final Identifier INSANITY_EVENT_PACKET = new Identifier(Constants.MOD_ID, "insanity_event");

    public static final Identifier SPELL_PACKET = new Identifier(Constants.MOD_ID, "spell");

    public static void registerC2S() {

    }

    public static void registerS2C() {
        ClientSidePacketRegistry.INSTANCE.register(SANITY_EXPAND_PACKET, (packetContext, packetByteBuf) -> {
            String name = packetByteBuf.readString();
            int amount = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> ((ISanity) packetContext.getPlayer()).addSanityCapExpansion(name, amount));
        });

        ClientSidePacketRegistry.INSTANCE.register(SANITY_REMOVE_EXPAND_PACKET, (packetContext, packetByteBuf) -> {
            String name = packetByteBuf.readString();
            packetContext.getTaskQueue().execute(() -> ((ISanity) packetContext.getPlayer()).removeSanityCapExpansion(name));
        });

        ClientSidePacketRegistry.INSTANCE.register(INSANITY_EVENT_PACKET, ((packetContext, packetByteBuf) -> {
            Identifier id = packetByteBuf.readIdentifier();
            packetContext.getTaskQueue().execute(() -> InsanityEvent.INSANITY_EVENTS.get(id).execute(packetContext.getPlayer(), (ISanity) packetContext.getPlayer()));
        }));

        ClientSidePacketRegistry.INSTANCE.register(SPELL_PACKET, ((packetContext, packetByteBuf) -> {
            CompoundTag spellTag = packetByteBuf.readCompoundTag();
            Entity entity = packetContext.getPlayer().world.getEntityById(packetByteBuf.readInt());
            if (entity instanceof LivingEntity)
                packetContext.getTaskQueue().execute(() -> Spell.fromTag(spellTag).cast((LivingEntity) entity));
        }));
    }

    public static void sendToPlayer(PlayerEntity player, PacketByteBuf data, Identifier packet) {
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).networkHandler != null)
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, packet, data);
    }

    public static void sendToPlayers(World world, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }
}
