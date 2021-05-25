package com.miskatonicmysteries.client.render.entity.feature;

import com.miskatonicmysteries.client.model.entity.ByakheeEntityModel;
import com.miskatonicmysteries.common.entity.ByakheeEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ByakheeSaddleFeatureRenderer extends FeatureRenderer<ByakheeEntity, ByakheeEntityModel> {
    private static final ByakheeEntityModel SADDLE_MODEL = new ByakheeEntityModel();
    public static final Identifier SADDLE_TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/byakhee/byakhee_saddle.png");

    public ByakheeSaddleFeatureRenderer(FeatureRendererContext<ByakheeEntity, ByakheeEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ByakheeEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isSaddled()) {
            render(getContextModel(), SADDLE_MODEL, getTexture(entity), matrices, vertexConsumers, light, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, tickDelta, 1, 1, 1);
        }
    }


    @Override
    protected Identifier getTexture(ByakheeEntity entity) {
        return SADDLE_TEXTURE;
    }
}
