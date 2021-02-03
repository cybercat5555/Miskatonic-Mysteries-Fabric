package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.recipe.rite.Rite;
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
                        return stack.length > 0 ? IVariable.from(stack[0]) : null;
                    }
                }

            }
        }
        return null;
    }


   /* @Override
    public String process(String s) {
        switch (s) {
            case "octagram":
                return ClientProxy.OCTAGRAM_TEXTURES.getOrDefault(rite.octagram, new ResourceLocation(MiskatonicMysteries.MODID, "textures/blocks/octagram_generic.png")).toString();
            case "power":
                return String.valueOf(rite.focusPower);
            case "overflow_tolerance":
                return String.valueOf(rite.overflowTolerance);
            case "ticks":
                return String.valueOf(rite.ticksNeeded);
            case "knowledge":
                return rite.unlockBook.getName();
            case "rite_name":
                return I18n.format(rite.getUnlocalizedName());
        }
        for (int i = 0; i < rite.ingredients.size(); i++) {
            if (s.equals("ingredient" + (i + 1))) {
                if (rite.ingredients.size() > i) {
                    return PatchouliAPI.instance.serializeIngredient(rite.ingredients.get(i));
                }
                return PatchouliAPI.instance.serializeItemStack(ItemStack.EMPTY);
            }
        }
        return null;
    }*/
}
