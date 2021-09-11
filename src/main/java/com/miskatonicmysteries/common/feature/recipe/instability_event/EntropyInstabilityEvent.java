package com.miskatonicmysteries.common.feature.recipe.instability_event;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Random;

public class EntropyInstabilityEvent extends InstabilityEvent {
    public EntropyInstabilityEvent() {
        super(new Identifier(Constants.MOD_ID, "entropy"), 0.3F, 0.3F);
    }

    @Override
    public boolean cast(OctagramBlockEntity blockEntity, float instability) {
        World world = blockEntity.getWorld();
        Random random = world.getRandom();
        if (instability > 0.6 && random.nextFloat() < instability - 0.3F) {
            Vec3i pos = blockEntity.getPos().add(random.nextInt(5) - 2, 0, random.nextInt(5) - 2);
            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
            lightning.setPos(pos.getX(), pos.getY(), pos.getZ());
            world.spawnEntity(lightning);
            return random.nextFloat() < 0.25F;
        } else {
            Vec3d pos = blockEntity.getSummoningPos()
                    .add(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
            world.createExplosion(null, pos.x, pos.y, pos.z, 2 * instability, random.nextFloat() < instability,
                    instability > 0.9 ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE);
            return instability > 0.6F && random.nextFloat() < instability - 0.6F;
        }
    }
}
