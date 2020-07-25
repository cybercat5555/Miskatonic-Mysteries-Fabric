package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.CommonProxy;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.miskatonicmysteries.lib.Constants.DataTrackers.SANITY_CAP;

public class InsanityHandler {

    public static void init(){
        //currently empty lol
    }

    public static void handleInsanityEvents(PlayerEntity player){
        ISanity sanity = (ISanity) player;
        float insanityFactor = 1F - (sanity.getSanity() / (float) SANITY_CAP);
        if (player.getRandom().nextFloat() < (0.1F + (0.1F * insanityFactor))){
            InsanityEvent event = findInsanityEvent(player,  sanity, insanityFactor);
            if (event != null) event.execute(player, (ISanity) player);
        }
    }

    private static InsanityEvent findInsanityEvent(PlayerEntity player, ISanity sanity, float insanityFactor) {
        List<InsanityEvent> events = InsanityEvent.INSANITY_EVENTS.values().parallelStream().filter(event -> event.test(player, sanity, insanityFactor)).collect(Collectors.toList());
        for (int i = 0; i < CommonProxy.CONFIG.insanityEventAttempts; i++) {
            if (events.isEmpty()) return null;
            InsanityEvent event = events.get(player.getRandom().nextInt(events.size()));
            if (player.getRandom().nextFloat() < event.baseChance) return event;
            events.remove(event);
        }
        return null;
    }
}
