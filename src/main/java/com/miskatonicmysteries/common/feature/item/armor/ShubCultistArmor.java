package com.miskatonicmysteries.common.feature.item.armor;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;

public class ShubCultistArmor extends ArmorItem implements Affiliated {

	public ShubCultistArmor(EquipmentSlot slot) {
		super(ArmorMaterials.LEATHER, slot, new Settings().group(Constants.MM_GROUP).maxCount(1));
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return MMAffiliations.SHUB;
	}

	@Override
	public boolean isSupernatural() {
		return false;
	}
}
