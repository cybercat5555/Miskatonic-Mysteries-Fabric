package com.miskatonicmysteries.common.mixin;

import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityInducer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack", at = @At("HEAD"))
    private void induceSanityAfterUse(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> info){
        if (entity instanceof ISanity && (getUseAction(stack) == UseAction.BLOCK || getUseAction(stack) == UseAction.DRINK || getUseAction(stack) == UseAction.EAT)){
            InsanityInducer.INSANITY_INDUCERS.forEach((id, inducer) -> {
                if (inducer.ingredient.test(stack)) {
                   inducer.induceInsanity(world, entity, (ISanity) entity);
                }
            });
        }
    }

    @Shadow public abstract UseAction getUseAction(ItemStack stack);

}
