package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.banner.impl.LoomPatternContainer;
import com.miskatonicmysteries.api.banner.loom.LoomPattern;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;

import net.minecraft.block.Block;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends WallStandingBlockItem {

	@Unique
	private static NbtList mmLoomPatterns;
	@Unique
	private static int mmNextLoomPatternIndex;

    public BannerItemMixin(Block standingBlock, Block wallBlock, Settings settings) {
        super(standingBlock, wallBlock, settings);
    }

    /**
     * Reads in Banner++ loom pattern data and resets current loom pattern index.
     */
    @Inject(
    method = "appendBannerTooltip",
    at = @At("HEAD")
    )
    private static void mm$preAppendLoomPatterns(ItemStack stack, List<Text> lines, CallbackInfo info) {
        mmNextLoomPatternIndex = 0;
        NbtCompound beTag = stack.getSubNbt("BlockEntityTag");

        if (beTag != null && beTag.contains(LoomPatternContainer.NBT_KEY)) {
            mmLoomPatterns = beTag.getList(LoomPatternContainer.NBT_KEY, 10);
        }
    }

    /**
     * Add Banner++ loom patterns to the tooltip.
     */
    @Inject(
    method = "appendBannerTooltip",
    at = @At(
    value = "INVOKE",
    target = "Lnet/minecraft/nbt/NbtList;getCompound(I)Lnet/minecraft/nbt/NbtCompound;",
    ordinal = 0
    )
    )
    private static void mm$appendLoomPatternsInline(ItemStack stack, List<Text> lines, CallbackInfo info) {
        int nextIndex = lines.size() - 1;

		if (mmLoomPatterns != null) {
			while (mmNextLoomPatternIndex < mmLoomPatterns.size()) {
				NbtCompound data = mmLoomPatterns.getCompound(mmNextLoomPatternIndex);

                if (data.getInt("Index") == nextIndex) {
                    mm$addLoomPatternLine(data, lines);
                    mmNextLoomPatternIndex++;
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Add Banner++ loom patterns that occur after all regular banner patterns
     * in the tooltip (this also covers the case where no vanilla banner patterns
     * are present).
     */
    @Inject(method = "appendBannerTooltip", at = @At("RETURN"))
    private static void mm$appendLoomPatternsPost(ItemStack stack, List<Text> lines, CallbackInfo info) {
        if (mmLoomPatterns != null) {
            for (int i = mmNextLoomPatternIndex; i < mmLoomPatterns.size(); i++) {
                NbtCompound data = mmLoomPatterns.getCompound(i);
                mm$addLoomPatternLine(data, lines);
            }

            // allow NBT tag to be garbage collected
            mmLoomPatterns = null;
        }
    }

    @Unique
    private static void mm$addLoomPatternLine(NbtCompound data, List<Text> lines) {
        Identifier id = Identifier.tryParse(data.getString("Pattern"));
        DyeColor color = DyeColor.byId(data.getInt("Color"));

		if (id != null) {
			LoomPattern pattern = LoomPatterns.REGISTRY.get(id);

			if (pattern != null) {
				pattern.addPatternLine(lines, color);
			}
		}
	}
}