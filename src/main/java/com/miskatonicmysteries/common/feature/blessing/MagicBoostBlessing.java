package com.miskatonicmysteries.common.feature.blessing;

import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.interfaces.SpellCaster;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class MagicBoostBlessing extends Blessing {
    public MagicBoostBlessing() {
        super(new Identifier(Constants.MOD_ID, "magic_boost"), Affiliation.HASTUR);
    }

    @Override
    public void onAcquired(LivingEntity entity) {
        super.onAcquired(entity);
        SpellCaster.of(entity).ifPresent(caster -> caster.setPowerPool(caster.getPowerPool() + 3));
    }

    @Override
    public void onRemoved(LivingEntity entity) {
        super.onRemoved(entity);
        SpellCaster.of(entity).ifPresent(caster -> caster.setPowerPool(caster.getPowerPool() - 3));
    }
}
