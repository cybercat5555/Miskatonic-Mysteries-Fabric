package com.miskatonicmysteries.common.util;

import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.registry.MMRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;

public class NbtUtil {
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
            if (MMRegistries.SPELL_EFFECTS.containsId(id)) {
                caster.getLearnedEffects().add(MMRegistries.SPELL_EFFECTS.get(id));
            }
        });
        caster.getLearnedMediums().clear();
        tag.getList(Constants.NBT.SPELL_MEDIUMS, 8).forEach(mediumString -> {
            Identifier id = new Identifier(mediumString.asString());
            if (MMRegistries.SPELL_MEDIUMS.containsId(id)) {
                caster.getLearnedMediums().add(MMRegistries.SPELL_MEDIUMS.get(id));
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
            if (MMRegistries.BLESSINGS.containsId(id)) {
                ascendant.getBlessings().add(MMRegistries.BLESSINGS.get(id));
            }
        });
    }
}
