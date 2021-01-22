package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.client.particle.AmbientMagicParticle;
import com.miskatonicmysteries.client.particle.CandleFlameParticle;
import com.miskatonicmysteries.client.particle.LeakParticle;
import com.miskatonicmysteries.client.particle.ShrinkingMagicParticle;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class MMParticles {
    public static final DefaultParticleType FLAME = FabricParticleTypes.simple(true);
    public static final DefaultParticleType AMBIENT = FabricParticleTypes.simple(true);
    public static final DefaultParticleType SHRINKING_MAGIC = FabricParticleTypes.simple(true);
    public static final DefaultParticleType DRIPPING_BLOOD = FabricParticleTypes.simple(true);

    public static void init() {
        RegistryUtil.register(Registry.PARTICLE_TYPE, "flame", FLAME);
        ParticleFactoryRegistry.getInstance().register(FLAME, CandleFlameParticle.Factory::new);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "magic_upwards", AMBIENT);
        ParticleFactoryRegistry.getInstance().register(AMBIENT, AmbientMagicParticle.Factory::new);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "magic_shrinking", SHRINKING_MAGIC);
        ParticleFactoryRegistry.getInstance().register(SHRINKING_MAGIC, ShrinkingMagicParticle.Factory::new);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "blood", DRIPPING_BLOOD);
        ParticleFactoryRegistry.getInstance().register(DRIPPING_BLOOD, LeakParticle.BloodFactory::new);
    }

    public static void spawnCandleParticle(World world, double x, double y, double z, float size, boolean alwaysSpawn) {
        if (alwaysSpawn || world.random.nextBoolean()) {
            world.addParticle(MMParticles.FLAME, x, y, z, size + world.random.nextGaussian() / 20F, 0, 0);
        }
    }
}