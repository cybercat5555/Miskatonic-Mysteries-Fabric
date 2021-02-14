package com.miskatonicmysteries.common.feature.recipe.rite.summon;

import com.miskatonicmysteries.client.model.entity.TatteredPrinceRenderer;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.model.Model;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class PrinceSummoningRite extends SummoningRite {
    public PrinceSummoningRite() {
        super(new Identifier(Constants.MOD_ID, "summon_prince"), MMAffiliations.HASTUR, "", 0.75F, 0, MMEntities.TATTERED_PRINCE, Ingredient.ofItems(Items.DIAMOND));
    }

    @Override
    protected Model getRenderedModel(OctagramBlockEntity entity) {
        return TatteredPrinceRenderer.DUMMY_PRINCE_MODEL;
    }
}
