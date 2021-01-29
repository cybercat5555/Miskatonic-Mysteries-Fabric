package com.miskatonicmysteries.common.feature.blessing;

import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class RoyalEntourageBlessing extends Blessing {
    public RoyalEntourageBlessing() {
        super(new Identifier(Constants.MOD_ID, "royal_entourage"), Affiliation.HASTUR);
    }

    @Override
    public void onAcquired(LivingEntity entity) {
        super.onAcquired(entity);
        SpellCaster.of(entity).ifPresent(caster -> caster.learnMedium(SpellMedium.GROUP));
    }

    @Override
    public void onRemoved(LivingEntity entity) {
        super.onRemoved(entity);
        SpellCaster.of(entity).ifPresent(caster -> caster.getLearnedMediums().remove(SpellMedium.GROUP));
    }
}
