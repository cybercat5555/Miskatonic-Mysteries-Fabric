package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class DamageSpellEffect extends SpellEffect {
    public DamageSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "damage"), null, 0xF5CE8C);
    }

    @Override
    public int getColor(@Nullable LivingEntity caster) {
        Optional<Affiliated> affiliation = Affiliated.of(caster);
        if (!affiliation.isPresent() || affiliation.get().getAffiliation(true) == null || affiliation.get().getAffiliation(true).equals(MMAffiliations.NONE)) {
            return 0xF9E6A7;
        }
        return affiliation.get().getAffiliation(true).getIntColor();
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium) {
        if (target != null) {
            target.damage(DamageSource.MAGIC, 2.5F * (intensity + 1));
            return true;
        }
        return false;
    }
}
