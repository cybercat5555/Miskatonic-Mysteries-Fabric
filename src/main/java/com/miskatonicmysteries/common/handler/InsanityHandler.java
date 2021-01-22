package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.feature.interfaces.Sanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.miskatonicmysteries.common.lib.Constants.DataTrackers.SANITY_CAP;

public class InsanityHandler {

    public static void init() {
        //currently empty lol
    }

    public static void resetProgress(PlayerEntity player) {
        Sanity sanity = (Sanity) player;
        sanity.getSanityCapExpansions().keySet().forEach(sanity::removeSanityCapExpansion);
        sanity.setSanity(sanity.getMaxSanity(), true);
        sanity.setShocked(true);
        //remove other stuff
    }

    public static boolean hasSanityCapExpansion(PlayerEntity player, String expansion) {
        Sanity sanity = (Sanity) player;
        return sanity.getSanityCapExpansions().containsKey(expansion);
    }

    public static void handleInsanityEvents(PlayerEntity player) {
        Sanity sanity = (Sanity) player;
        float insanityFactor = 1F - calculateSanityFactor(sanity);
        if (player.getRandom().nextFloat() < (0.1F + (0.1F * insanityFactor))) {
            InsanityEvent event = findInsanityEvent(player, sanity, insanityFactor);
            if (event != null) event.execute(player, (Sanity) player);
        }
    }

    private static InsanityEvent findInsanityEvent(PlayerEntity player, Sanity sanity, float insanityFactor) {
        List<InsanityEvent> events = InsanityEvent.INSANITY_EVENTS.values().parallelStream().filter(event -> event.test(player, sanity, insanityFactor)).collect(Collectors.toList());
        for (int i = 0; i < MiskatonicMysteries.config.sanity.insanityEventAttempts; i++) {
            if (events.isEmpty()) return null;
            InsanityEvent event = events.get(player.getRandom().nextInt(events.size()));
            if (player.getRandom().nextFloat() < event.baseChance) return event;
            events.remove(event);
        }
        return null;
    }

    public static float calculateSanityFactor(Sanity sanity) {
        return sanity.getSanity() / (float) SANITY_CAP;
    }

    public static void handleClientSideBlockChange(ClientPlayerEntity player, World world, BlockState state, BlockPos pos, Random random) {
        Sanity sanity = (Sanity) player;
        if (sanity.getSanity() < 750 && random.nextFloat() < 0.25F)
            if (state.getBlock().equals(Blocks.BIRCH_LOG)) {
                world.setBlockState(pos, Blocks.OAK_LOG.getDefaultState(), 1);
            }
    }
}
