package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.common.entity.SpellProjectileEntity;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ProjectileSpellMedium extends SpellMedium {
    public ProjectileSpellMedium() {
        super(new Identifier(Constants.MOD_ID, "projectile"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        if (!world.isClient) {
            SpellProjectileEntity projectile = new SpellProjectileEntity(caster.world, caster, effect, intensity);
            projectile.setProperties(caster, caster.pitch, caster.headYaw, 0, 1, 0F);
            return world.spawnEntity(projectile);
        }
        return true;
    }
}
