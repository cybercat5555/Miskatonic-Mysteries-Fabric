package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.banner.impl.LoomPatternContainer;
import com.miskatonicmysteries.api.banner.loom.LoomPattern;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends WallStandingBlockItem {
    private BannerItemMixin() {
        super(null, null, null);
    }

    @Unique
    private static NbtList loomPatterns;

    @Unique
    private static int nextLoomPatternIndex;

    /**
     * Reads in Banner++ loom pattern data and resets current loom pattern index.
     */
    @Inject(
    method = "appendBannerTooltip",
    at = @At("HEAD")
    )
    private static void preAppendBppLoomPatterns(ItemStack stack, List<Text> lines, CallbackInfo info) {
        nextLoomPatternIndex = 0;
        NbtCompound beTag = stack.getSubNbt("BlockEntityTag");

        if (beTag != null && beTag.contains(LoomPatternContainer.NBT_KEY)) {
            loomPatterns = beTag.getList(LoomPatternContainer.NBT_KEY, 10);
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
    private static void appendBppLoomPatternsInline(ItemStack stack, List<Text> lines, CallbackInfo info) {
        int nextIndex = lines.size() - 1;

        if (loomPatterns != null) {
            while (nextLoomPatternIndex < loomPatterns.size()) {
                NbtCompound data = loomPatterns.getCompound(nextLoomPatternIndex);

                if (data.getInt("Index") == nextIndex) {
                    addLoomPatternLine(data, lines);
                    nextLoomPatternIndex++;
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
    private static void appendBppLoomPatternsPost(ItemStack stack, List<Text> lines, CallbackInfo info) {
        if (loomPatterns != null) {
            for (int i = nextLoomPatternIndex; i < loomPatterns.size(); i++) {
                NbtCompound data = loomPatterns.getCompound(i);
                addLoomPatternLine(data, lines);
            }

            // allow NBT tag to be garbage collected
            loomPatterns = null;
        }
    }

    @Unique
    private static void addLoomPatternLine(NbtCompound data, List<Text> lines) {
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