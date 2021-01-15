package com.miskatonicmysteries.common.feature.sanity;

import com.miskatonicmysteries.common.handler.PacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class InsanityEvent {
    public static final Map<Identifier, InsanityEvent> INSANITY_EVENTS = new HashMap<>();
    public final float baseChance;
    public final int insanityThreshold;
    public final Identifier id;

    public InsanityEvent(Identifier id, float baseChance, int insanityThreshold) {
        this.id = id;
        this.baseChance = baseChance;
        this.insanityThreshold = insanityThreshold;
    }

    public boolean execute(PlayerEntity playerEntity, Sanity sanity) {
        if (!playerEntity.world.isClient) {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeIdentifier(id);
            PacketHandler.sendToPlayer(playerEntity, data, PacketHandler.INSANITY_EVENT_PACKET);
        }
        return true;
    }

    public boolean test(PlayerEntity player, Sanity sanity, float insanityFactor) {
        return sanity.getSanity() <= insanityThreshold && player.getRandom().nextFloat() < baseChance;
    }
}
