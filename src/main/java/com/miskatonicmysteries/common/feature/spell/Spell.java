package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.registry.MMBlessings;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class Spell {
    public SpellEffect effect;
    public SpellMedium medium;
    public int intensity;

    public Spell(SpellMedium medium, SpellEffect effect, int intensity) {
        this.medium = medium;
        this.effect = effect;
        this.intensity = intensity;
    }


    public boolean cast(LivingEntity caster) {
        Optional<SpellCaster> spellCaster = SpellCaster.of(caster);

        if (spellCaster.isPresent()) {
            caster.world.playSound(caster.getX(), caster.getY(), caster.getZ(), MMSounds.MAGIC, SoundCategory.PLAYERS, 0.85F, (float) caster.getRandom().nextGaussian() * 0.2F + 1.0F, true);

        }
        if (!caster.world.isClient) {
            int intensityMod = 0;
            int cooldownMod = 0;
            if (MiskatonicMysteriesAPI.isWardingMarkNearby(caster.world, caster.getBlockPos())){
                caster.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 100, 0));
                cooldownMod += 80;
                if (intensity <= 0) {
                    return false;
                }else{
                    intensityMod--;
                }
            }
            if (caster.hasStatusEffect(MMStatusEffects.RESONANCE)){
                StatusEffectInstance instance = caster.getStatusEffect(MMStatusEffects.RESONANCE);
                cooldownMod -= (instance.getAmplifier() + 1) * 20;
                intensityMod += Math.round(instance.getAmplifier() * 0.75F);
            }
            if (Ascendant.of(caster).isPresent() && MiskatonicMysteriesAPI.hasBlessing(Ascendant.of(caster).get(), MMBlessings.MAGIC_BOOST)){
                intensityMod++;
            }
            int burnout = MathHelper.clamp(spellCaster.get().getSpellCooldown() + Math.round(medium.getCooldownModifier(caster) * effect.getCooldownBase(intensity)) + cooldownMod, 0, 999);
            spellCaster.get().setSpellCooldown(burnout);
            Sanity.of(caster).ifPresent(sanity -> sanity.setSanity(sanity.getSanity() - effect.calculateSanityPenalty(caster.getRandom(), intensity), false));
            SpellPacket.send(caster, toTag(new CompoundTag()), intensityMod);
        }

        return effect.canCast(caster, medium) && medium.cast(caster.world, caster, effect, intensity);
    }

    public CompoundTag toTag(CompoundTag tag) {
        tag.putString(Constants.NBT.SPELL_EFFECT, effect.getId().toString());
        tag.putString(Constants.NBT.SPELL_MEDIUM, medium.getId().toString());
        tag.putInt(Constants.NBT.INTENSITY, intensity);
        return tag;
    }

    public static Spell fromTag(CompoundTag tag) {
        return tag.isEmpty() ? null : new Spell(MMRegistries.SPELL_MEDIUMS.get(new Identifier(tag.getString(Constants.NBT.SPELL_MEDIUM))), MMRegistries.SPELL_EFFECTS.get(new Identifier(tag.getString(Constants.NBT.SPELL_EFFECT))), tag.getInt(Constants.NBT.INTENSITY));
    }
}
