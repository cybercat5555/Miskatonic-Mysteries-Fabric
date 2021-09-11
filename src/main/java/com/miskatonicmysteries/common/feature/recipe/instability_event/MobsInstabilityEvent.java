package com.miskatonicmysteries.common.feature.recipe.instability_event;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.entity.HarrowEntity;
import com.miskatonicmysteries.common.feature.entity.TentacleEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class MobsInstabilityEvent extends InstabilityEvent {
    public MobsInstabilityEvent() {
        super(new Identifier(Constants.MOD_ID, "mobs"), 0.4F, 0.2F);
    }

    @Override
    public boolean cast(OctagramBlockEntity blockEntity, float instability) {
        World world = blockEntity.getWorld();
        Random random = world.getRandom();
        for (int i = 0; i < 1 + random.nextInt(2) * instability; i++) {
            Vec3d pos = blockEntity.getSummoningPos()
                    .add(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
            if (random.nextBoolean()) {
                HarrowEntity harrow = MMEntities.HARROW.create(world);
                harrow.setPosition(pos);
                harrow.setLifeTicks((int) (200 + instability * 100));
                harrow.setTarget(blockEntity.getOriginalCaster());
                world.spawnEntity(harrow);
            }else {
                TentacleEntity tentacle = MMEntities.GENERIC_TENTACLE.create(world);
                tentacle.setPosition(pos);
                tentacle.setMaxAge((int) (200 + instability * 100));
                tentacle.setSpecificTarget(blockEntity.getOriginalCaster());
                world.spawnEntity(tentacle);
            }
        }
        return instability > 0.8F && random.nextFloat() < instability - 0.6F;
    }
}
