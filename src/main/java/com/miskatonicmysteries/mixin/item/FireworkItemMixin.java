package com.miskatonicmysteries.mixin.item;

import com.miskatonicmysteries.common.feature.world.party.MMPartyState;
import com.miskatonicmysteries.common.feature.world.party.Party;

import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
public class FireworkItemMixin {

	@Inject(method = "useOnBlock", at = @At("HEAD"))
	private void onUsedOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (context.getWorld() instanceof ServerWorld s) {
			Party party = MMPartyState.get(s).getParty(context.getBlockPos());
			if (party != null) {
				party.addPartyPower(Party.FIREWORK_BONUS);
			}
		}
	}
}
