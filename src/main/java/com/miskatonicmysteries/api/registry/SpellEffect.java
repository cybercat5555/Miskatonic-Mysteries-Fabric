package com.miskatonicmysteries.api.registry;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

public abstract class SpellEffect {
    private final Identifier id;
    private final Predicate<LivingEntity> castingPredicate;
    private final int color;

    public SpellEffect(Identifier id, @Nullable Predicate<LivingEntity> castingPredicate, int color) {
        this.id = id;
        this.castingPredicate = castingPredicate == null ? (living) -> true : castingPredicate;
        this.color = color;
    }

    public int getColor(@Nullable LivingEntity caster) {
        return color;
    }

    /**
     * @return if the spell was successfully cast
     */
    public abstract boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium);

    public boolean canCast(LivingEntity caster, SpellMedium medium) {
        return castingPredicate.test(caster);
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getTextureLocation() {
        return new Identifier(id.getNamespace(), "textures/gui/spell_widgets/effect/" + id.getPath() + ".png");
    }

    public static void spawnParticleEffectsOnTarget(LivingEntity caster, SpellEffect effect, Entity target) {
        for (int i = 0; i < 15; i++)
            target.world.addParticle(ParticleTypes.ENTITY_EFFECT,
                    target.getX() + target.world.random.nextGaussian() * target.getWidth(),
                    target.getY() + target.world.random.nextFloat()
                            * target.getHeight(),
                    target.getZ() + target.world.random.nextGaussian() * target.getWidth(),
                    ((effect.getColor(caster) >> 16) & 255) / 255F,
                    ((effect.getColor(caster) >> 8) & 255) / 255F,
                    (effect.getColor(caster) & 255) / 255F);
    }

    public float getCooldownBase(int intensity) {
        return 20 + intensity * 10;
    }

    public int calculateSanityPenalty(Random random, int intensity){
        return random.nextInt(intensity + 1);
    }
}
