package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.HarrowEntityModel;
import com.miskatonicmysteries.client.model.entity.TentacleModel;
import com.miskatonicmysteries.common.entity.HarrowEntity;
import com.miskatonicmysteries.common.entity.TentacleEntity;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

import java.awt.*;
import java.util.Iterator;

public class HarrowEntityRenderer extends MobEntityRenderer<HarrowEntity, HarrowEntityModel> {
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/harrow/harrow.png");
    public HarrowEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new HarrowEntityModel(), 0F);
    }


    @Override
    protected void scale(HarrowEntity entity, MatrixStack matrices, float amount) {
        float scale = 1;
        if (entity.age < 20){
            scale = (entity.age + MinecraftClient.getInstance().getTickDelta()) / 20F;
        }else if (entity.getLifeTicks() < 20){
            scale = (entity.getLifeTicks() - MinecraftClient.getInstance().getTickDelta()) / 20F;
        }
        matrices.scale(scale, scale, scale);
    }

    @Override
    protected void setupTransforms(HarrowEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        matrices.translate(0, -0.25F, 0);
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
    }

    @Override
    protected int getBlockLight(HarrowEntity entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(HarrowEntity entity) {
        return TEXTURE;
    }
}
