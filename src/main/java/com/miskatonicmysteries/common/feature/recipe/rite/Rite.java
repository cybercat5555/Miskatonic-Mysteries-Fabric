package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.lib.util.InventoryUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Rite {
    public static final Map<Identifier, Rite> RITES = new HashMap<>();
    public final List<Ingredient> ingredients = DefaultedList.ofSize(8, Ingredient.EMPTY);
    public final Identifier id;

    public Rite(Identifier id, Ingredient... ingredients) {
        this.id = id;
        for (int i = 0; i < ingredients.length; i++) {
            this.ingredients.set(i, ingredients[i]);
        }
    }

    public void onStart(OctagramBlockEntity octagramBlockEntity) {

    }

    public void tick(OctagramBlockEntity octagram) {
        octagram.tickCount++;
    }

    public abstract boolean isFinished(OctagramBlockEntity octagram);

    public abstract void onFinished(OctagramBlockEntity octagram);

    public void onCancelled(OctagramBlockEntity octagram) {
    }

    public boolean shouldContinue(OctagramBlockEntity octagram) {
        return true;
    }

    public boolean canCast(OctagramBlockEntity octagram) {
        return InventoryUtil.areItemStackListsExactlyEqual(ingredients, octagram);
    }

    /**
     * Called in {@link com.miskatonicmysteries.client.render.blockentity.OctagramBlockRender}OctagramBlockRender
     * Flags:
     * 2 - Render Items
     * 1 - Render the Octagram
     * 0 - Render None
     * Flags can be combined e.g. 2 | 1 to render both normally
     *
     * @return bitwise flag combination used for rendering, see above
     */
    @Environment(EnvType.CLIENT)
    public byte renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        return 2 | 1;
    }
}
