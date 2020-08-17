package com.miskatonicmysteries.common.feature.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class SpellEffect {
    public static final Map<Identifier, SpellEffect> SPELL_EFFECTS = new HashMap<>();
    private final Identifier id;
    private final Predicate<LivingEntity> castingPredicate;
    private final int color;

    public SpellEffect(Identifier id, @Nullable Predicate<LivingEntity> castingPredicate, int color) {
        this.id = id;
        this.castingPredicate = castingPredicate == null ? (living) -> true : castingPredicate;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public abstract boolean effect(World world, LivingEntity caster, @Nullable Entity target, SpellMedium medium, int intensity);

    public int getMaxDistance(LivingEntity caster) {
        return 16;
    }

    public boolean canCast(LivingEntity caster, SpellMedium medium) {
        return castingPredicate.test(caster);
    }

    public Identifier getId() {
        return id;
    }

    public static void spawnParticleEffectsOnTarget(SpellEffect effect, Entity target) {
        for (int i = 0; i < 15; i++)
            target.world.addParticle(ParticleTypes.ENTITY_EFFECT,
                    target.getX() + target.world.random.nextGaussian() * target.getWidth(),
                    target.getY() + target.world.random.nextFloat()
                            * target.getHeight(),
                    target.getZ() + target.world.random.nextGaussian() * target.getWidth(),
                    ((effect.getColor() >> 16) & 255) / 255F,
                    ((effect.getColor() >> 8) & 255) / 255F,
                    (effect.getColor() & 255) / 255F);
    }
}
