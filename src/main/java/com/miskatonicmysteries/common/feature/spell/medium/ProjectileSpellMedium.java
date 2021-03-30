package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.entity.SpellProjectileEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ProjectileSpellMedium extends SpellMedium {
    public ProjectileSpellMedium() {
        super(new Identifier(Constants.MOD_ID, "projectile"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity, boolean backfires) {
        if (!world.isClient) {
            SpellProjectileEntity projectile = new SpellProjectileEntity(caster.world, caster, effect, intensity);
            projectile.setProperties(caster, caster.pitch, (float) (caster.headYaw + (backfires ? 40 * caster.getRandom().nextGaussian() : 0)), 0, 1, 0);
            projectile.yaw = caster.headYaw;
            projectile.pitch = caster.pitch;
            return world.spawnEntity(projectile);
        }
        return true;
    }

    @Override
    public float getBurnoutRate(LivingEntity caster) {
        return 0.12F;
    }
}
