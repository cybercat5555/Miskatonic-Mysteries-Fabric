package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.client.particle.CandleFlameParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final DefaultParticleType FLAME = new DefaultParticleType();

    public static void init() {
        Util.register(Registry.PARTICLE_TYPE, "flame", FLAME);
        ParticleFactoryRegistry.getInstance().register(FLAME, CandleFlameParticle.Factory::new);
    }


    public static class DefaultParticleType extends net.minecraft.particle.DefaultParticleType {
        public DefaultParticleType() {
            super(true);
        }
    }
}
