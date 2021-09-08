package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public abstract class AscensionLockedRite extends Rite {
    private final int stage;
    private final Affiliation ascensionAffiliation;
    private final String knowledge;

    public AscensionLockedRite(Identifier id, @Nullable Affiliation octagram, String knowledge, float investigatorChance, int stage, Ingredient... ingredients) {
        super(id, octagram, investigatorChance, ingredients);
        this.stage = stage;
        this.ascensionAffiliation = octagram;
        this.knowledge = knowledge;
    }

    @Override
    public boolean canCast(OctagramBlockEntity octagram) {
        if (super.canCast(octagram)) {
            if (!octagram.doesCasterHaveKnowledge(knowledge)) {
                if (octagram.getOriginalCaster() != null) {
                    octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.knowledge"), true);
                }
                return false;
            }
            if (Ascendant.of(octagram.getOriginalCaster()).isPresent()) {
                Ascendant ascendant = Ascendant.of(octagram.getOriginalCaster()).get();
                if (ascensionAffiliation != null && !ascensionAffiliation.equals(MiskatonicMysteriesAPI.getNonNullAffiliation(octagram.getOriginalCaster(), false))) {
                    octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.ascension_path_fail"), true);
                    return false;
                }
                if (ascendant.getAscensionStage() < stage) {
                    octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.ascension_stage_fail"), true);
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
