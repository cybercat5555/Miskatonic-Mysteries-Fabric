package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.recipe.rite.Rite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RiteProcessor implements IComponentProcessor {
    protected Rite rite;

    @Override
    public void setup(IVariableProvider variables) {
        this.rite = Rite.RITES.get(new Identifier(variables.get("rite").asString()));
    }

    @Override
    public IVariable process(String key) {
        switch (key) {
            case "octagram": {
                SpriteIdentifier sprite = ResourceHandler.getMatchingOctagramTexture(rite.octagramAffiliation);
                return IVariable.wrap(new Identifier(sprite.getTextureId().getNamespace(), "textures/" + sprite.getTextureId().getPath() + ".png").toString());
            }
            case "rite_name":
                return IVariable.wrap(I18n.translate(rite.getTranslationString()));
            default: {
                for (int i = 0; i < rite.ingredients.size(); i++) {
                    if (key.equals("ingredient" + (i + 1))) {
                        ItemStack[] stack = rite.ingredients.get(i).getMatchingStacksClient(); //patchouli pls
                        return stack.length > 0 ? IVariable.from(stack[MinecraftClient.getInstance().world.random.nextInt(stack.length)]) : null;
                    }
                }
            }
        }
        return null;
    }
}
