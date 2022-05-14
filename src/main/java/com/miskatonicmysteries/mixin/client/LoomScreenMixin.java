package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.banner.impl.*;
import com.miskatonicmysteries.api.banner.loom.LoomPattern;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;
import com.miskatonicmysteries.api.banner.loom.PatternLimitModifier;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(LoomScreen.class)
public abstract class LoomScreenMixin extends HandledScreen<LoomScreenHandler> {
    @Shadow
    private boolean hasTooManyPatterns;
    @Shadow
    private List<?> bannerPatterns;

    @Unique
    private List<LoomPatternData> loomPatterns = Collections.emptyList();

    private LoomScreenMixin() {
        super(null, null, null);
    }

    /**
     * Adds the number of rows corresponding to Banner++ loom patterns
     * to the loom GUI.
     */
    @Redirect(
    method = "<clinit>",
    at = @At(
    value = "FIELD",
    target = "Lnet/minecraft/block/entity/BannerPattern;COUNT:I"
    )
    )
    private static int takeBppIntoAccountForRowCount() {
        return BannerPattern.COUNT + LoomPatternsInternal.dyeLoomPatternCount();
    }

    /**
     * Modifies the banner pattern count to include the number of
     * dye loom patterns.
     */
    @Redirect(
    method = "drawBackground",
    at = @At(
    value = "FIELD",
    target = "Lnet/minecraft/block/entity/BannerPattern;COUNT:I"
    )
    )
    private int modifyDyePatternCount() {
        return BannerPattern.COUNT + LoomPatternsInternal.dyeLoomPatternCount();
    }

    @Redirect(
    method = "drawBackground",
    at = @At(
    value = "INVOKE",
    target = "Lnet/minecraft/screen/LoomScreenHandler;getSelectedPattern()I",
    ordinal = 0
    )
    )
    private int negateBppLoomPatternForCmp(LoomScreenHandler self) {
        int res = self.getSelectedPattern();

        if (res < 0) {
            res = -res;
        }

        return res;
    }

    @ModifyConstant(method = "onInventoryChanged", constant = @Constant(intValue = 6))
    private int disarmVanillaPatternLimitCheck(int limit) {
        return Integer.MAX_VALUE;
    }

    @Inject(
    method = "onInventoryChanged",
    at = @At(
    value = "FIELD",
    target = "Lnet/minecraft/client/gui/screen/ingame/LoomScreen;hasTooManyPatterns:Z",
    opcode = Opcodes.GETFIELD,
    ordinal = 0
    )
    )
    private void addBppLoomPatternsToFullCond(CallbackInfo info) {
        ItemStack banner = (this.handler).getBannerSlot().getStack();
        int patternLimit = PatternLimitModifier.EVENT.invoker().computePatternLimit(6, MinecraftClient.getInstance().player);
        this.hasTooManyPatterns |= BannerBlockEntity.getPatternCount(banner) >= patternLimit;
    }

    @Inject(method = "onInventoryChanged", at = @At("RETURN"))
    private void saveLoomPatterns(CallbackInfo info) {
        if (this.bannerPatterns != null) {
            ItemStack banner = (this.handler).getOutputSlot().getStack();
            NbtList ls = LoomPatternConversions.getLoomPatternTag(banner);
            loomPatterns = LoomPatternConversions.makeLoomPatternData(ls);
        } else {
            loomPatterns = Collections.emptyList();
        }
    }

    @Unique
    private int loomPatternIndex;

    /**
     * Prevents an ArrayIndexOutOfBoundsException from occuring when the vanilla
     * code tries to index BannerPattern.values() with an index representing
     * a Banner++ loom pattern (which is negative).
     */
    @ModifyVariable(
    method = "drawBanner",
    at = @At(value = "LOAD", ordinal = 0),
    ordinal = 0
    )
    private int disarmBppIndexForVanilla(int patternIndex) {
        loomPatternIndex = patternIndex;

        if (patternIndex < 0) {
            patternIndex = 0;
        }

        return patternIndex;
    }

    @Unique
    private static final List<LoomPatternData> singleBppPattern = new ArrayList<>();

    /**
     * If the pattern index indicates a Banner++ pattern, put the Banner++
     * pattern in the item NBT instead of a vanilla pattern.
     */
    @Redirect(
    method = "drawBanner",
    at = @At(
    value = "INVOKE",
    target = "Lnet/minecraft/nbt/NbtCompound;put(Ljava/lang/String;Lnet/minecraft/nbt/NbtElement;)Lnet/minecraft/nbt/NbtElement;",
    ordinal = 0
    )
    )
    private NbtElement proxyPutPatterns(NbtCompound nbt, String key, NbtElement patterns) {
        singleBppPattern.clear();

        if (loomPatternIndex < 0) {
            int loomPatternIdx = -loomPatternIndex - (1 + BannerPattern.LOOM_APPLICABLE_COUNT);
            LoomPattern pattern = LoomPatternsInternal.byLoomIndex(loomPatternIdx);
            NbtList loomPatterns = new NbtList();
            NbtCompound patternNbtElement = new NbtCompound();
            patternNbtElement.putString("Pattern", LoomPatterns.REGISTRY.getId(pattern).toString());
            patternNbtElement.putInt("Color", 0);
            patternNbtElement.putInt("Index", 1);
            loomPatterns.add(patternNbtElement);
            // pop dummy vanilla banner pattern
            NbtList vanillaPatterns = (NbtList) patterns;
            assert vanillaPatterns.size() == 2 : vanillaPatterns.size();
            vanillaPatterns.remove(1);
            nbt.put(LoomPatternContainer.NBT_KEY, loomPatterns);
            singleBppPattern.add(new LoomPatternData(pattern, DyeColor.WHITE, 1));
        }

        LoomPatternRenderContext.setLoomPatterns(singleBppPattern);
        return nbt.put(key, patterns);
    }

    @Inject(
    method = "drawBackground",
    at = @At(
    value = "INVOKE",
    target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;)V"
    )
    )
    private void setEmptyBppPattern(CallbackInfo info) {
        LoomPatternRenderContext.setLoomPatterns(loomPatterns);
    }

    /**
     * The dye pattern loop has positive indices, we negate the indices that
     * represent Banner++ loom patterns before passing them to method_22692.
     */
    @ModifyArg(
    method = "drawBackground",
    at = @At(
    value = "INVOKE",
    target = "Lnet/minecraft/client/gui/screen/ingame/LoomScreen;drawBanner(III)V",
    ordinal = 0
    ),
    index = 0
    )
    private int modifyBppPatternIdxArg(int patternIdx) {
        if (patternIdx > BannerPattern.LOOM_APPLICABLE_COUNT) {
            patternIdx = -patternIdx;
        }

        return patternIdx;
    }
}