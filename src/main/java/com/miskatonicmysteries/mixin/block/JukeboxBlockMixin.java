package com.miskatonicmysteries.mixin.block;

import com.miskatonicmysteries.common.feature.world.party.MMPartyState;
import com.miskatonicmysteries.common.feature.world.party.Party;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlock.class)
public class JukeboxBlockMixin {

	@Inject(method = "setRecord", at = @At("HEAD"))
	private void onRecordSet(WorldAccess world, BlockPos pos, BlockState state, ItemStack stack, CallbackInfo ci) {
		if (world instanceof ServerWorld s) {
			Party party = MMPartyState.get(s).getParty(pos);
			if (party != null) {
				party.musicSources.add(pos);
			}
		}
	}

	@Inject(method = "removeRecord", at = @At("HEAD"))
	private void onRecordRemoved(World world, BlockPos pos, CallbackInfo ci) {
		if (world instanceof ServerWorld s) {
			Party party = MMPartyState.get(s).getParty(pos);
			if (party != null) {
				party.musicSources.remove(pos);
			}
		}
	}
}
