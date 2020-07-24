package com.miskatonicmysteries.common.handler;

import net.minecraft.entity.player.PlayerEntity;

public class InsanityHandler {
    public static void init(){
        //currently empty lol
    }

    public static void handleInsanityEvents(PlayerEntity player, int sanity, float insanityFactor){
        if (player.getRandom().nextFloat() < (0.1F + (0.2F * insanityFactor))){
        //    InsanityEvent event = findInsanityEvent(player, sanity, insanityFactor);
        }
    }

   /* private static InsanityEvent findInsanityEvent(PlayerEntity player, int sanity, float insanityFactor) {
        return null;
    }*/
}
