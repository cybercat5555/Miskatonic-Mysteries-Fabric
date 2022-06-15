package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.banner.impl.LoomPatternContainer;
import com.miskatonicmysteries.api.banner.impl.LoomPatternConversions;
import com.miskatonicmysteries.api.banner.impl.LoomPatternData;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityClientMixin extends BlockEntity implements LoomPatternContainer {

	@Shadow
	private List<?> patterns;

	@Unique
	private List<LoomPatternData> mmLoomPatterns = Collections.emptyList();

	private BannerBlockEntityClientMixin() {
		super(null, BlockPos.ORIGIN, null);
	}

	@Override
	public List<LoomPatternData> bannerpp_getLoomPatterns() {
		if (this.patterns == null) {
			NbtList tag = ((LoomPatternContainer.Internal) this).bannerpp_getLoomPatternTag();
			mmLoomPatterns = LoomPatternConversions.makeLoomPatternData(tag);
		}

		return Collections.unmodifiableList(mmLoomPatterns);
	}

	/**
	 * Reads Banner++ loom pattern data from an item stack.
	 */
	@Inject(method = "readFrom", at = @At("RETURN"))
	private void bppReadPatternFromItemStack(ItemStack stack, DyeColor color, CallbackInfo info) {
		((Internal) this).bannerpp_setLoomPatternTag(LoomPatternConversions.getLoomPatternTag(stack));
	}

	/**
	 * Adds Banner++ loom pattern data to the pick block stack.
	 */
	@Inject(method = "getPickStack", at = @At("RETURN"))
	private void putBppPatternsInPickStack(CallbackInfoReturnable<ItemStack> info) {
		ItemStack stack = info.getReturnValue();
		NbtList tag = ((Internal) this).bannerpp_getLoomPatternTag();

		if (tag != null) {
			stack.getOrCreateSubNbt("BlockEntityTag")
				.put(NBT_KEY, tag);
		}
	}
}