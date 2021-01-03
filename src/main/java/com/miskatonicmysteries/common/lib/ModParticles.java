package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.client.particle.CandleFlameParticle;
import com.miskatonicmysteries.client.particle.MagicParticle;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ModParticles {
    public static final DefaultParticleType FLAME = new DefaultParticleType();
    public static final DefaultParticleType MAGIC = new DefaultParticleType();

    public static void init() {
        RegistryUtil.register(Registry.PARTICLE_TYPE, "flame", FLAME);
        ParticleFactoryRegistry.getInstance().register(FLAME, CandleFlameParticle.Factory::new);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "magic", MAGIC);
        ParticleFactoryRegistry.getInstance().register(MAGIC, MagicParticle.Factory::new);
    }

    public static void spawnCandleParticle(World world, double x, double y, double z, float size, boolean alwaysSpawn) {
        if (alwaysSpawn || world.random.nextBoolean()) {
            world.addParticle(ModParticles.FLAME, x, y, z, size + world.random.nextGaussian() / 20F, 0, 0);
        }
    }

    public static class DefaultParticleType extends net.minecraft.particle.DefaultParticleType {
        public DefaultParticleType() {
            super(true);
        }
    }
}