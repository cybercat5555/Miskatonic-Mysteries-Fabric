package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.banner.impl.LoomPatternContainer;
import com.miskatonicmysteries.api.banner.impl.LoomPatternData;
import com.miskatonicmysteries.api.banner.impl.LoomPatternRenderContext;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;

@Mixin(BannerBlockEntityRenderer.class)
public abstract class BannerBlockEntityRendererMixin {
    @Unique
    private static List<LoomPatternData> loomPatterns;

    @Unique
    private static int nextLoomPatternIndex;

    /**
     * Saves Banner++ loom pattens in a field for rendering.
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void preBppPatternRender(
    BannerBlockEntity banner,
    float f1,
    MatrixStack stack,
    VertexConsumerProvider provider,
    int i,
    int j,
    CallbackInfo info) {
        LoomPatternRenderContext.setLoomPatterns(((LoomPatternContainer) banner).bannerpp_getLoomPatterns());
    }

    @Inject(
    method = "renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V",
    at = @At("HEAD")
    )
    private static void bppResetLocalCtx(CallbackInfo info) {
        nextLoomPatternIndex = 0;
        loomPatterns = LoomPatternRenderContext.getLoomPatterns();
    }

    /**
     * Renders Banner++ loom patterns in line with vanilla banner patterns.
     */
    @Inject(
    method = "renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V",
    at = @At(
    value = "INVOKE",
    target = "Ljava/util/List;get(I)Ljava/lang/Object;",
    ordinal = 0,
    remap = false
    ),
    locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void bppPatternRenderInline(
    MatrixStack stack,
    VertexConsumerProvider provider,
    int light,
    int overlay,
    ModelPart canvas,
    SpriteIdentifier baseSprite,
    boolean isBanner,
    List<Pair<BannerPattern, DyeColor>> patterns,
    boolean glint,
    CallbackInfo info,
    int idx) {
        while (nextLoomPatternIndex < loomPatterns.size()) {
            LoomPatternData data = loomPatterns.get(nextLoomPatternIndex);

            if (data.index() == idx - 1) {
                renderBppLoomPattern(data, stack, provider, canvas, light, overlay, isBanner);
                nextLoomPatternIndex++;
            } else {
                break;
            }
        }
    }

    /**
     * Renders Banner++ loom patterns that occur after all vanilla banner patterns.
     */
    @Inject(
    method = "renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V",
    at = @At("RETURN")
    )
    private static void bppPatternRenderPost(
    MatrixStack stack,
    VertexConsumerProvider provider,
    int light,
    int overlay,
    ModelPart canvas,
    SpriteIdentifier baseSprite,
    boolean isBanner,
    List<Pair<BannerPattern, DyeColor>> patterns,
    boolean glint,
    CallbackInfo info) {
        for (int i = nextLoomPatternIndex; i < loomPatterns.size(); i++) {
            renderBppLoomPattern(loomPatterns.get(i), stack, provider, canvas, light, overlay, isBanner);
        }

        loomPatterns = Collections.emptyList();
    }

    @Unique
    private static void renderBppLoomPattern(
    LoomPatternData data,
    MatrixStack stack,
    VertexConsumerProvider provider,
    ModelPart canvas,
    int light,
    int overlay,
    boolean notShield) {
        Identifier spriteId = data.pattern().getSpriteId(notShield ? "banner" : "shield");
        SpriteIdentifier realSpriteId = new SpriteIdentifier(notShield ? TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE : TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, spriteId);
        float[] color = data.color().getColorComponents();
        canvas.render(stack, realSpriteId.getVertexConsumer(provider, RenderLayer::getEntityNoOutline), light, overlay, color[0], color[1], color[2], 1.0f);
    }
}