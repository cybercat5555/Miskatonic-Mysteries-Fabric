package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.api.interfaces.Hallucination;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MobEntityRenderer.class)
public abstract class MobEntityRendererMixin<T extends MobEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    private MobEntityRendererMixin(EntityRendererFactory.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    private void shouldRender(T mobEntity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            Hallucination hallucination = Hallucination.of(mobEntity).get();
            if (hallucination.getHallucinationTarget().isPresent()) {
                cir.setReturnValue(MinecraftClient.getInstance().player != null && hallucination.getHallucinationTarget().get().equals(MinecraftClient.getInstance().player.getUuid()));
            }
        }
    }
}
