package com.miskatonicmysteries.common.handler;

import com.miskatonicmysteries.common.feature.InsanityInducer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class InsanityHandler {
    public static void init(){
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            System.out.println(InsanityInducer.INSANITY_INDUCERS.keySet());
            InsanityInducer.INSANITY_INDUCERS.forEach((id, inducer) -> {
                System.out.println(id);
                if (inducer.ingredient.test(stack)){
                    System.out.println("awoo owo");
                }
            });
            return TypedActionResult.pass(player.getStackInHand(hand));
        });
        //register data-driven items applying insanity there ig
        //maybe also data-driven insanity events
    }

    public static void handleInsanityEvents(PlayerEntity player, int sanity, float insanityFactor){
        if (player.getRandom().nextFloat() < (0.1F + (0.2F * insanityFactor))){
        //    InsanityEvent event = findInsanityEvent(player, sanity, insanityFactor);
        }
    }

/*    private static InsanityEvent findInsanityEvent(PlayerEntity player, int sanity, float insanityFactor) {
        return null;
    }*/
}
