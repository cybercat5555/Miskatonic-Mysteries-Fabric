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
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

import java.awt.*;
import java.util.Iterator;

public class HarrowEntityRenderer extends LivingEntityRenderer<HarrowEntity, HarrowEntityModel> {
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/harrow/harrow.png");
    public HarrowEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new HarrowEntityModel(), 0.5F);
        this.shadowRadius = 0;
    }

    @Override
    protected boolean hasLabel(HarrowEntity harrowEntity) {
        return super.hasLabel(harrowEntity) && (harrowEntity.shouldRenderName() || harrowEntity.hasCustomName() && harrowEntity == this.dispatcher.targetedEntity);
    }

    @Override
    public void render(HarrowEntity harrowEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        this.model.handSwingProgress = this.getHandSwingProgress(harrowEntity, g);
        this.model.riding = harrowEntity.hasVehicle();
        this.model.child = harrowEntity.isBaby();
        float h = MathHelper.lerpAngleDegrees(g, harrowEntity.prevBodyYaw, harrowEntity.bodyYaw);
        float j = MathHelper.lerpAngleDegrees(g, harrowEntity.prevHeadYaw, harrowEntity.headYaw);
        float k = j - h;
        float o;
        if (harrowEntity.hasVehicle() && harrowEntity.getVehicle() instanceof LivingEntity) {
            LivingEntity livingEntity2 = (LivingEntity)harrowEntity.getVehicle();
            h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
            k = j - h;
            o = MathHelper.wrapDegrees(k);
            if (o < -85.0F) {
                o = -85.0F;
            }

            if (o >= 85.0F) {
                o = 85.0F;
            }

            h = j - o;
            if (o * o > 2500.0F) {
                h += o * 0.2F;
            }

            k = j - h;
        }

        float m = MathHelper.lerp(g, harrowEntity.prevPitch, harrowEntity.pitch);
        float p;
        if (harrowEntity.getPose() == EntityPose.SLEEPING) {
            Direction direction = harrowEntity.getSleepingDirection();
            if (direction != null) {
                p = harrowEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                matrixStack.translate((float)(-direction.getOffsetX()) * p, 0.0D, (float)(-direction.getOffsetZ()) * p);
            }
        }

        o = this.getAnimationProgress(harrowEntity, g);
        this.setupTransforms(harrowEntity, matrixStack, o, h, g);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(harrowEntity, matrixStack, g);
        matrixStack.translate(0.0D, -1.5010000467300415D, 0.0D);
        p = 0.0F;
        float q = 0.0F;
        if (!harrowEntity.hasVehicle() && harrowEntity.isAlive()) {
            p = MathHelper.lerp(g, harrowEntity.lastLimbDistance, harrowEntity.limbDistance);
            q = harrowEntity.limbAngle - harrowEntity.limbDistance * (1.0F - g);
            if (harrowEntity.isBaby()) {
                q *= 3.0F;
            }

            if (p > 1.0F) {
                p = 1.0F;
            }
        }

        this.model.animateModel(harrowEntity, q, p, g);
        this.model.setAngles(harrowEntity, q, p, o, k, m);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = this.isVisible(harrowEntity);
        boolean bl2 = !bl && !harrowEntity.isInvisibleTo(minecraftClient.player);
        boolean bl3 = minecraftClient.hasOutline(harrowEntity);
        RenderLayer renderLayer = this.getRenderLayer(harrowEntity, bl, bl2, bl3);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            int r = getOverlay(harrowEntity, this.getAnimationCounter(harrowEntity, g));
            float transparency = 1;
            if (harrowEntity.age < 20){
                transparency = harrowEntity.age / 20F;
            }else if (harrowEntity.getLifeTicks() < 20){
                transparency = harrowEntity.getLifeTicks() / 20F;
            }
            this.model.render(matrixStack, vertexConsumer, i, r, 1.0F, 1.0F, 1.0F, transparency);
        }
        matrixStack.pop();
        super.render(harrowEntity, f, g, matrixStack, vertexConsumerProvider, i);
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
