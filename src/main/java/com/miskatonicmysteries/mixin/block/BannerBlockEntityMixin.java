package com.miskatonicmysteries.mixin.block;

import com.miskatonicmysteries.api.banner.impl.LoomPatternContainer;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

import static java.util.Comparator.comparingInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Adds loom pattern data fields to the banner block entity. The actual pattern parsing is done client side in the banner's client-only methods.
 */
@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntity implements LoomPatternContainer.Internal {

	@Unique
	private NbtList mmLoomPatternsTag = new NbtList();

	private BannerBlockEntityMixin() {
		super(null, BlockPos.ORIGIN, null);
	}

	/**
	 * Add Banner++ patterns to the pattern count.
	 */
	@Inject(method = "getPatternCount", at = @At("RETURN"), cancellable = true)
	private static void modifyPatternCount(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		NbtCompound beTag = stack.getSubNbt("BlockEntityTag");

		if (beTag != null && beTag.contains(LoomPatternContainer.NBT_KEY)) {
			int count = beTag.getList(LoomPatternContainer.NBT_KEY, 10).size();
			info.setReturnValue(info.getReturnValueI() + count);
		}
	}

	/**
	 * Handles removing Banner++ loom patterns instead of vanilla loom patterns when a banner is cleaned in a cauldron. Yes, this is an
	 * "inject-and-cancel" callback. Let me know if there are incompatibilities.
	 */
	@Inject(method = "loadFromItemStack", at = @At("HEAD"), cancellable = true)
	private static void cleanBppLoomPattern(ItemStack stack, CallbackInfo info) {
		NbtCompound beTag = stack.getSubNbt("BlockEntityTag");

		if (beTag != null) {
			NbtList loomPatterns = beTag.getList(LoomPatternContainer.NBT_KEY, 10);
			NbtList patterns = beTag.getList("Patterns", 10);
			boolean cleaned = false;

			if (!loomPatterns.isEmpty()) {
				// determine if the last loom pattern is the topmost
				int lastIndex = loomPatterns.getCompound(loomPatterns.size() - 1).getInt("Index");

				if (lastIndex >= patterns.size()) {
					loomPatterns.remove(loomPatterns.size() - 1);
					cleaned = true;
				}
			}

			if (!cleaned && !patterns.isEmpty()) {
				patterns.remove(patterns.size() - 1);
			}

			if (loomPatterns.isEmpty()) {
				if (patterns.isEmpty()) {
					stack.removeSubNbt("BlockEntityTag");
				} else {
					beTag.remove(LoomPatternContainer.NBT_KEY);
				}
			} else if (patterns.isEmpty()) {
				beTag.remove("Patterns");
			}
		}

		info.cancel();
	}

	@Override
	public NbtList bannerpp_getLoomPatternTag() {
		return mmLoomPatternsTag;
	}

	@Override
	public void bannerpp_setLoomPatternTag(NbtList tag) {
		mmLoomPatternsTag = tag;

		if (mmLoomPatternsTag != null) {
			// validate NBT data, removing and/or resetting invalid data
			for (Iterator<NbtElement> itr = mmLoomPatternsTag.iterator(); itr.hasNext(); ) {
				NbtCompound element = (NbtCompound) itr.next();
				Identifier id = Identifier.tryParse(element.getString("Pattern"));
				int colorId = element.getInt("Color");
				int index = element.getInt("Index");

				if (id == null || !LoomPatterns.REGISTRY.getIds().contains(id)) {
					itr.remove();
				} else {
					int rtColorId = DyeColor.byId(colorId).getId();

					if (rtColorId != colorId) {
						element.putInt("Color", rtColorId);
					}

					if (index < 0) {
						element.putInt("Index", 0);
					}
				}
			}

			// the Java API requires that this sort be stable
			mmLoomPatternsTag.sort(comparingInt(t -> ((NbtCompound) t).getInt("Index")));
		}
	}

	/**
	 * Write Banner++ data to tag.
	 */
	@Inject(method = "writeNbt", at = @At("RETURN"))
	private void addBppPatternData(NbtCompound nbt, CallbackInfo ci) {
		if (nbt != null) {
			nbt.put(LoomPatternContainer.NBT_KEY, mmLoomPatternsTag);
		}
	}

	/**
	 * Read Banner++ data from tag.
	 */
	@Inject(method = "readNbt", at = @At("RETURN"))
	private void readBppPatternData(NbtCompound tag, CallbackInfo info) {
		bannerpp_setLoomPatternTag(tag.getList(LoomPatternContainer.NBT_KEY, 10));
	}
}