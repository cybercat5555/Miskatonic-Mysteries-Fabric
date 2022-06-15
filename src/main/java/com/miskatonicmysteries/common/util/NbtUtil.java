package com.miskatonicmysteries.common.util;

import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

public class NbtUtil {

	public static NbtCompound writeSpellData(SpellCaster caster, NbtCompound tag) {
		NbtList spells = new NbtList();
		caster.getSpells().forEach((spell) -> spells.add(spell == null ? new NbtCompound() : spell.toTag(new NbtCompound())));
		NbtList effects = new NbtList();
		caster.getLearnedEffects().forEach((effect) -> effects.add(NbtString.of(effect.getId().toString())));
		NbtList mediums = new NbtList();
		caster.getLearnedMediums().forEach((medium) -> mediums.add(NbtString.of(medium.getId().toString())));
		tag.put(Constants.NBT.SPELL_LIST, spells);
		tag.put(Constants.NBT.SPELL_EFFECTS, effects);
		tag.put(Constants.NBT.SPELL_MEDIUMS, mediums);
		return tag;
	}

	public static void readSpellData(SpellCaster caster, NbtCompound tag) {
		caster.getSpells().clear();
		for (int i = 0; i < tag.getList(Constants.NBT.SPELL_LIST, 10).size(); i++) {
			caster.getSpells().add(i, Spell.fromTag((NbtCompound) tag.getList(Constants.NBT.SPELL_LIST, 10).get(i)));
		}
		caster.getLearnedEffects().clear();
		tag.getList(Constants.NBT.SPELL_EFFECTS, 8).forEach(effectString -> {
			Identifier id = new Identifier(effectString.asString());
			if (MMRegistries.SPELL_EFFECTS.getIds().contains(id)) {
				caster.getLearnedEffects().add(MMRegistries.SPELL_EFFECTS.get(id));
			}
		});
		caster.getLearnedMediums().clear();
		tag.getList(Constants.NBT.SPELL_MEDIUMS, 8).forEach(mediumString -> {
			Identifier id = new Identifier(mediumString.asString());
			if (MMRegistries.SPELL_MEDIUMS.getIds().contains(id)) {
				caster.getLearnedMediums().add(MMRegistries.SPELL_MEDIUMS.get(id));
			}
		});
		addDefaultSpells(caster);
	}

	private static void addDefaultSpells(SpellCaster caster) {
		caster.getLearnedMediums().add(MMSpellMediums.SELF);
		caster.getLearnedEffects().add(MMSpellEffects.IGNITE);
		caster.getLearnedEffects().add(MMSpellEffects.DAMAGE);
		caster.getLearnedEffects().add(MMSpellEffects.KNOCKBACK);
		caster.getLearnedEffects().add(MMSpellEffects.RESISTANCE);
		caster.getLearnedEffects().add(MMSpellEffects.HEAL);
	}

	public static NbtCompound writeBlessingData(Ascendant ascendant, NbtCompound tag) {
		NbtList blessings = new NbtList();
		ascendant.getBlessings().forEach((blessing) -> blessings.add(NbtString.of(blessing.getId().toString())));
		tag.put(Constants.NBT.BLESSINGS, blessings);
		return tag;
	}

	public static void readBlessingData(Ascendant ascendant, NbtCompound tag) {
		ascendant.getBlessings().clear();
		tag.getList(Constants.NBT.BLESSINGS, 8).forEach(blessings -> {
			Identifier id = new Identifier(blessings.asString());
			if (MMRegistries.BLESSINGS.getIds().contains(id)) {
				ascendant.getBlessings().add(MMRegistries.BLESSINGS.get(id));
			}
		});
	}
}
