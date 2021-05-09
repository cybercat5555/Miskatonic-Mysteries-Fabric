package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.entity.GenericTentacleEntity;
import com.miskatonicmysteries.common.entity.HarrowEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.TickDurationMonitor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HarrowSpellEffect extends SpellEffect {
    public HarrowSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "harrow"), null, 0xf9df1b);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium) {
        if (pos != null) {
            boolean flag = false;
            if (!world.isClient){
                for (int i = 0; i < (intensity + 1); i++) {
                    HarrowEntity harrow = MMEntities.HARROW.create(world);
                    harrow.refreshPositionAndAngles(caster.getParticleX(1), caster.getRandomBodyY(), caster.getParticleZ(1), caster.getHeadYaw(), caster.getPitch(1));
                    harrow.setLifeTicks(400 + intensity * 400);
                    if (target instanceof LivingEntity && (target != caster || (target instanceof TameableEntity && ((TameableEntity) target).getOwner() != caster))) {
                        harrow.setTarget((LivingEntity) target);
                    }
                    harrow.setOwner(caster);
                    world.spawnEntity(harrow);
                    flag = true;
                }
            }
            return flag;
        }
        return false;
    }

    @Override
    public float getCooldownBase(int intensity) {
        return 100 + intensity * 80;
    }
}
