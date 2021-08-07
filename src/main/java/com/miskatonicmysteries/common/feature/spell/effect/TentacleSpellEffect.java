package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.entity.GenericTentacleEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TentacleSpellEffect extends SpellEffect {
    public TentacleSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "tentacles"), null, 0xf9df1b);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium) {
        if (pos != null) {
            boolean flag = false;
            if (!world.isClient) {
                for (int i = 0; i < (intensity + 1); i++) {
                    GenericTentacleEntity tentacle = MMEntities.GENERIC_TENTACLE.create(world);
                    tentacle.refreshPositionAndAngles(pos.x + world.random.nextGaussian(), pos.y, pos.z + world.random.nextGaussian(), caster.getRandom().nextInt(360), 0);
                    tentacle.setMaxAge(200 + caster.getRandom().nextInt(80) + intensity * 100);
                    if (target instanceof LivingEntity && (target != caster || (target instanceof TameableEntity && ((TameableEntity) target).getOwner() != caster))) {
                        tentacle.setTarget((LivingEntity) target);
                    }
                    world.spawnEntity(tentacle);
                    flag = true;
                }
            }
            return flag;
        }
        return false;
    }

    @Override
    public float getCooldownBase(int intensity) {
        return 60 + intensity * 20;
    }
}
