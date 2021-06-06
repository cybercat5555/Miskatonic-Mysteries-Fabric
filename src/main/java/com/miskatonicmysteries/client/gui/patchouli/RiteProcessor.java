package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.registry.MMRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RiteProcessor implements IComponentProcessor {
    protected Rite rite;

    @Override
    public void setup(IVariableProvider variables) {
        this.rite = MMRegistries.RITES.get(new Identifier(variables.get("rite").asString()));
    }

    @Override
    public IVariable process(String key) {
        switch (key) {
            case "octagram": {
                SpriteIdentifier sprite = ResourceHandler.getMatchingOctagramTexture(rite.getOctagramAffiliation());
                return IVariable.wrap(new Identifier(sprite.getTextureId().getNamespace(), "textures/" + sprite.getTextureId().getPath() + ".png").toString());
            }
            case "rite_name":
                return IVariable.wrap(I18n.translate(rite.getTranslationString()));
            default: {
                for (int i = 0; i < rite.getIngredients().size(); i++) {
                    if (key.equals("ingredient" + (i + 1))) {
                        ItemStack[] stacks = rite.getIngredients().get(i).getMatchingStacksClient();
                        return IVariable.from(stacks);
                    }
                }
            }
        }
        return null;
    }
}
