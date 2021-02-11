package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.MobSpellPacket;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MobTargetMedium extends SpellMedium {
    public MobTargetMedium() {
        super(new Identifier(Constants.MOD_ID, "mob_target"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        if (caster instanceof MobEntity && caster.getAttacking() != null && !world.isClient) {
            if (caster.canSee(caster.getAttacking())) {
                MobSpellPacket.send(caster, effect, intensity);
                ((MobEntity) caster).lookAtEntity(caster.getAttacking(), 60, 60);
                return !caster.isDead() && effect.effect(caster.world, caster, caster.getAttacking(), caster.getAttacking().getPos(), this, intensity, caster);
            }
        }
        if (caster instanceof PlayerEntity) {
            return MMSpellMediums.BOLT.cast(world, caster, effect, intensity);
        }
        return false;
    }

    @Override
    public float getBurnoutRate(LivingEntity caster) {
        return 0F;
    }
}
