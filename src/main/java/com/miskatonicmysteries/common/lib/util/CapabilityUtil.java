package com.miskatonicmysteries.common.lib.util;

import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.blessing.Blessing;
import com.miskatonicmysteries.common.feature.interfaces.Affiliated;
import com.miskatonicmysteries.common.feature.interfaces.Ascendant;
import com.miskatonicmysteries.common.feature.interfaces.Sanity;
import com.miskatonicmysteries.common.feature.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.Constants;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Optional;

public class CapabilityUtil {
    public static void resetProgress(PlayerEntity player) {
        Sanity.of(player).ifPresent(sanity -> {
            sanity.getSanityCapExpansions().keySet().forEach(sanity::removeSanityCapExpansion);
            sanity.setSanity(sanity.getMaxSanity(), true);
            sanity.setShocked(true);
            sanity.syncSanityData();
        });
        Ascendant.of(player).ifPresent(ascendant -> {
            ascendant.setStage(0);
            ascendant.getBlessings().clear();
            ascendant.syncBlessingData();
        });
        SpellCaster.of(player).ifPresent(caster -> {
            caster.getSpells().clear();
            caster.getLearnedMediums().clear();
            caster.getLearnedEffects().clear();
            caster.setMaxSpells(0);
            caster.setPowerPool(0);
            caster.syncSpellData();
        });
    }

    public static void guaranteePower(int power, SpellCaster caster) {
        if (caster.getPowerPool() < power) {
            caster.setPowerPool(power);
        }
    }

    public static boolean isAffiliated(Entity entity) {
        return entity instanceof Affiliated && ((Affiliated) entity).getAffiliation(true) != Affiliation.NONE;
    }

    public static Affiliation getAffiliation(Entity entity, boolean apparent) {
        return isAffiliated(entity) ? ((Affiliated) entity).getAffiliation(apparent) : Affiliation.NONE;
    }

    public static boolean shouldRecognizeAffiliation(Entity entity) {
        return isAffiliated(entity) && ((Affiliated) entity).isSupernatural();
    }


    public static int getStage(PlayerEntity player) {
        Optional<Ascendant> ascendant = Ascendant.of(player);
        return ascendant.map(Ascendant::getStage).orElse(0);
    }

    public static Affiliation getApparentAffiliationFromEquipment(@Nullable ItemStack exclude, PlayerEntity player) {
        Inventory trinkets = TrinketsApi.getTrinketsInventory(player);
        for (int i = 0; i < trinkets.size(); i++) {
            ItemStack stack = trinkets.getStack(i);
            if (stack.equals(exclude)) {
                continue;
            }
            if (stack.getItem() instanceof Affiliated) {
                return ((Affiliated) stack.getItem()).getAffiliation(true);
            }
        }
        return Affiliation.NONE;
    }

    public static boolean hasBlessing(Ascendant ascendant, Blessing blessing) {
        return ascendant.getBlessings().contains(blessing);
    }

    public static CompoundTag writeSpellData(SpellCaster caster, CompoundTag tag) {
        ListTag spells = new ListTag();
        caster.getSpells().forEach((spell) -> spells.add(spell == null ? new CompoundTag() : spell.toTag(new CompoundTag())));
        ListTag effects = new ListTag();
        caster.getLearnedEffects().forEach((effect) -> effects.add(StringTag.of(effect.getId().toString())));
        ListTag mediums = new ListTag();
        caster.getLearnedMediums().forEach((medium) -> mediums.add(StringTag.of(medium.getId().toString())));
        tag.put(Constants.NBT.SPELL_LIST, spells);
        tag.put(Constants.NBT.SPELL_EFFECTS, effects);
        tag.put(Constants.NBT.SPELL_MEDIUMS, mediums);
        return tag;
    }

    public static void readSpellData(SpellCaster caster, CompoundTag tag) {
        caster.getSpells().clear();
        for (int i = 0; i < tag.getList(Constants.NBT.SPELL_LIST, 10).size(); i++) {
            caster.getSpells().add(i, Spell.fromTag((CompoundTag) tag.getList(Constants.NBT.SPELL_LIST, 10).get(i)));
        }
        caster.getLearnedEffects().clear();
        tag.getList(Constants.NBT.SPELL_EFFECTS, 8).forEach(effectString -> {
            Identifier id = new Identifier(effectString.asString());
            if (SpellEffect.SPELL_EFFECTS.containsKey(id)) {
                caster.getLearnedEffects().add(SpellEffect.SPELL_EFFECTS.get(id));
            }
        });
        caster.getLearnedMediums().clear();
        tag.getList(Constants.NBT.SPELL_MEDIUMS, 8).forEach(mediumString -> {
            Identifier id = new Identifier(mediumString.asString());
            if (SpellMedium.SPELL_MEDIUMS.containsKey(id)) {
                caster.getLearnedMediums().add(SpellMedium.SPELL_MEDIUMS.get(id));
            }
        });
    }

    public static CompoundTag writeBlessingData(Ascendant ascendant, CompoundTag tag) {
        ListTag blessings = new ListTag();
        ascendant.getBlessings().forEach((blessing) -> blessings.add(StringTag.of(blessing.getId().toString())));
        tag.put(Constants.NBT.BLESSINGS, blessings);
        return tag;
    }

    public static void readBlessingData(Ascendant ascendant, CompoundTag tag) {
        ascendant.getBlessings().clear();
        tag.getList(Constants.NBT.BLESSINGS, 8).forEach(blessings -> {
            Identifier id = new Identifier(blessings.asString());
            if (Blessing.BLESSINGS.containsKey(id)) {
                ascendant.getBlessings().add(Blessing.BLESSINGS.get(id));
            }
        });
    }

}
