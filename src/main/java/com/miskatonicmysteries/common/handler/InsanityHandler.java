package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.CommonProxy;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

import static com.miskatonicmysteries.lib.util.Constants.DataTrackers.SANITY_CAP;

public class InsanityHandler {

    public static void init(){
        //currently empty lol
    }

    public static void handleInsanityEvents(PlayerEntity player){
        ISanity sanity = (ISanity) player;
        float insanityFactor = 1F - calculateSanityFactor(sanity);
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

    public static float calculateSanityFactor(ISanity sanity){
        return sanity.getSanity() / (float) SANITY_CAP;
    }
}
