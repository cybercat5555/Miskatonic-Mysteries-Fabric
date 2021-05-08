package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.HarrowEntityModel;
import com.miskatonicmysteries.client.model.entity.TentacleModel;
import com.miskatonicmysteries.common.entity.HarrowEntity;
import com.miskatonicmysteries.common.entity.TentacleEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

import java.awt.*;

public class HarrowEntityRenderer extends MobEntityRenderer<HarrowEntity, HarrowEntityModel> {
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/harrow/harrow.png");
    public HarrowEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new HarrowEntityModel(), 0.5F);
        this.shadowRadius = 0;
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
