package com.miskatonicmysteries.common.item.armor;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ShubCultistArmor extends CultistArmor {
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/model/armor/dark_robes.png");

    public ShubCultistArmor(EquipmentSlot slot) {
        super(slot);
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        return TEXTURE;
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
