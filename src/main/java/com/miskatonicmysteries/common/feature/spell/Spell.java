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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

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


    public boolean cast(LivingEntity caster, boolean backfires) {
        Optional<SpellCaster> spellCaster = SpellCaster.of(caster);
        int intensityMod = 0;
        if (spellCaster.isPresent()) {
            caster.world.playSound(caster.getX(), caster.getY(), caster.getZ(), MMSounds.MAGIC, SoundCategory.PLAYERS, 0.85F, (float) caster.getRandom().nextGaussian() * 0.2F + 1.0F, true);
            float burnout;
            if (!caster.world.isClient && MiskatonicMysteriesAPI.isWardingMarkNearby(caster.world, caster.getBlockPos())){
                caster.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 100, 0));
                if (spellCaster.get().getSpellBurnout() < 0.5F){
                    spellCaster.get().setSpellBurnout(0.5F);
                }
                if (intensity <= 0) {
                    return false;
                }else{
                    intensityMod--;
                }
            }
            if (Ascendant.of(caster).isPresent() && MiskatonicMysteriesAPI.hasBlessing(Ascendant.of(caster).get(), MMBlessings.MAGIC_BOOST)){
                intensityMod++;
            }
            burnout = spellCaster.get().getSpellBurnout() + (medium.getBurnoutRate(caster) * effect.getBurnoutMultiplier(intensity));
            if (burnout > 1) {
                caster.damage(DamageSource.MAGIC, 4);
                spellCaster.get().setSpellBurnout(1);
                return false;
            } else {
                if (caster.getRandom().nextFloat() < burnout) {
                    caster.damage(DamageSource.MAGIC, 2);
                    Sanity.of(caster).ifPresent(sanity -> sanity.setSanity(sanity.getSanity() - (int) (10F * burnout), false));
                }
                spellCaster.get().setSpellBurnout(burnout);
            }
        }
        if (!caster.world.isClient) {
            SpellPacket.send(caster, toTag(new CompoundTag()), backfires);
        }

        return effect.canCast(caster, medium) && medium.cast(caster.world, caster, effect, intensity + intensityMod, backfires);
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
