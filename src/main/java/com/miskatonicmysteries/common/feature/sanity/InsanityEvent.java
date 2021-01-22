package com.miskatonicmysteries.common.feature.sanity;

import com.miskatonicmysteries.common.feature.interfaces.Sanity;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.InsanityEventPacket;
import net.minecraft.entity.player.PlayerEntity;
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
            InsanityEventPacket.send(playerEntity, id);
        }
        return true;
    }

    public boolean test(PlayerEntity player, Sanity sanity, float insanityFactor) {
        return sanity.getSanity() <= insanityThreshold && player.getRandom().nextFloat() < baseChance;
    }
}
