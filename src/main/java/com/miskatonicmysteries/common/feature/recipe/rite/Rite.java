package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.lib.util.InventoryUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Matrix4f;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Rite {
    public static final Map<Identifier, Rite> RITES = new HashMap<>();
    public final List<Ingredient> ingredients = DefaultedList.ofSize(8, Ingredient.EMPTY);
    public final Identifier id;
    public final Affiliation octagramAffiliation;
    public float investigatorChance;

    public Rite(Identifier id, @Nullable Affiliation octagram, float investigatorChance, Ingredient... ingredients) {
        this.id = id;
        this.investigatorChance = investigatorChance;
        for (int i = 0; i < ingredients.length; i++) {
            this.ingredients.set(i, ingredients[i]);
        }
        this.octagramAffiliation = octagram;
    }

    public void onStart(OctagramBlockEntity octagram) {
    }

    public void tick(OctagramBlockEntity octagram) {
        octagram.tickCount++;
    }

    public abstract boolean isFinished(OctagramBlockEntity octagram);

    public void onFinished(OctagramBlockEntity octagram) {
        octagram.clear();
        octagram.markDirty();
    }

    public void onCancelled(OctagramBlockEntity octagram) {
    }

    public boolean isPermanent(OctagramBlockEntity octagram) {
        return false;
    }

    public boolean shouldContinue(OctagramBlockEntity octagram) {
        return true;
    }

    public boolean canCast(OctagramBlockEntity octagram) {
        return (octagramAffiliation == null || octagramAffiliation.equals(octagram.getAffiliation(false))) && InventoryUtil.areItemStackListsExactlyEqual(ingredients, octagram);
    }


    @Environment(EnvType.CLIENT)
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
    }

    @Environment(EnvType.CLIENT)
    public void renderRiteItems(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
    }

    /**
     * Called in {@link com.miskatonicmysteries.client.render.blockentity.OctagramBlockRender}OctagramBlockRender before anything else.
     * Used to set up very special rendering
     * Flags:
     * 2 - Render Items
     * 1 - Render the Octagram
     * 0 - Render None
     * Flags can be combined e.g. 2 | 1 to render both normally
     *
     * @return bitwise flag combination used for rendering, see above
     */
    public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        return 2 | 1;
    }

    public static void renderPortalOctagram(float alpha, float origColors[], OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        Sprite sprite = ResourceHandler.getOctagramMaskTextureFor(entity).getSprite();
        float[] colors = {origColors[0], origColors[1], origColors[2], alpha};
        matrixStack.push();
        matrixStack.translate(0, 0.001F, 0);
        RenderHelper.renderTexturedPlane(3, sprite, matrixStack, sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(alpha < 1 ? RenderHelper.getTransparency() : RenderLayer.getCutout())), light, overlay, new float[]{1, 1, 1, alpha});
        matrixStack.push();
        Matrix4f matrix4f = matrixStack.peek().getModel();
        double distance = entity.getPos().getSquaredDistance(dispatcher.camera.getPos(), true);
        int renderDepth = Math.max(RenderHelper.getDepthFromDistance(distance) - 14, 1);
        matrixStack.translate(1.5, 0, 1.5);
        matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(45));
        matrixStack.translate(-0.4, 0.001F, -0.4);
        for (int i = 0; i < renderDepth; i++) {
            RenderHelper.renderPortalLayer(11 + i, entity.getWorld(), matrix4f, vertexConsumers, 0.8F, colors);
        }
        matrixStack.pop();
        matrixStack.translate(1.5, 0, 1.5);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(((float) entity.getWorld().getTime() + tickDelta) / 20.0F));
        matrixStack.translate(-1.5F, 0.0025F, -1.5F);
        RenderHelper.renderTexturedPlane(3, ResourceHandler.AURA_SPRITE.getSprite(), matrixStack, vertexConsumers.getBuffer(RenderHelper.getAuraGlowLayer()), light, overlay, colors);
        matrixStack.pop();
    }

    public String getTranslationString() {
        return "rite." + id.toString().replaceAll(":", ".");
    }
}
