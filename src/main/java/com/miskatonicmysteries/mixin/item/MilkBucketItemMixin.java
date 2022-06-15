package com.miskatonicmysteries.mixin.item;

import com.miskatonicmysteries.common.registry.MMStatusEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {

	@Unique
	private static final StatusEffect[] persistentStatusEffects = {MMStatusEffects.MANIA, MMStatusEffects.OVERMEDICATED,
		MMStatusEffects.EXOTIC_CRAVINGS};

	@Inject(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"), cancellable = true)
	private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		for (StatusEffect persistentStatusEffect : persistentStatusEffects) {
			if (user.hasStatusEffect(persistentStatusEffect)) {
				StatusEffectInstance persistent = user.getStatusEffect(persistentStatusEffect);
				user.clearStatusEffects();
				user.addStatusEffect(persistent);
				cir.setReturnValue(stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack);
			}
		}
	}
}
