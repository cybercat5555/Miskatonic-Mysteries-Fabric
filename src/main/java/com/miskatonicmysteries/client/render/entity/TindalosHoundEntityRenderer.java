package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.api.interfaces.Resonating;
import com.miskatonicmysteries.common.feature.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.feature.entity.TindalosHoundEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class TindalosHoundEntityRenderer extends GeoEntityRenderer<TindalosHoundEntity> {

    public TindalosHoundEntityRenderer(EntityRendererFactory.Context context) {
        super(context, null);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(TindalosHoundEntity entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {

    }
}
