package com.miskatonicmysteries.api.banner.loom;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Modifier callback for adjusting the banner pattern limit based
 * on the player.
 */
@FunctionalInterface
public interface PatternLimitModifier {
    Event<PatternLimitModifier> EVENT = EventFactory.createArrayBacked(
    PatternLimitModifier.class,
    modifiers -> (currentLimit, player) -> {
        int limit = currentLimit;

        for (PatternLimitModifier modifier : modifiers) {
            limit = modifier.computePatternLimit(limit, player);
        }

        return limit;
    }
    );

    /**
     * Compute the banner pattern limit for the given player, given the
     * current banner pattern limit. The banner pattern limit is the total
     * number of vanilla banner patterns plus loom patterns on the banner
     * (excluding the "base" vanilla banner pattern).
     */
    int computePatternLimit(int currentLimit, PlayerEntity player);
}