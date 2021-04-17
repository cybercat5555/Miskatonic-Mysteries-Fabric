package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.entity.GenericTentacleEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TentacleSpellEffect extends SpellEffect {
    public TentacleSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "tentacles"), null, 0xf9df1b);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium, boolean backfires) {
        if (pos != null) {
            if (!world.isClient){
                for (int i = 0; i < intensity; i++) {
                    GenericTentacleEntity tentacle = MMEntities.GENERIC_TENTACLE.create(world);
                    tentacle.setOwner(backfires && target instanceof LivingEntity ? (LivingEntity) target : caster);
                    tentacle.refreshPositionAndAngles(pos.x + world.random.nextGaussian(), pos.y, pos.z + world.random.nextGaussian(), caster.getRandom().nextInt(360), 0);
                    tentacle.setMaxAge(40 + caster.getRandom().nextInt(40) + intensity * 40);
                    if (target instanceof LivingEntity && !backfires) {
                        tentacle.setTarget((LivingEntity) target);
                    }
                    world.spawnEntity(tentacle);
                }
            }
        }
        return false;
    }

    @Override
    public float getBurnoutMultiplier(int intensity) {
        return 1.2F + intensity / 8F;
    }
}
