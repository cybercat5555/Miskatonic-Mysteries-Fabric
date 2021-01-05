package com.miskatonicmysteries.common.item.armor;

import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HasturCultistArmor extends CultistArmor {
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/model/armor/yellow_robes.png");

    public HasturCultistArmor(EquipmentSlot slot) {
        super(slot);
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        return TEXTURE;
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return Affiliation.HASTUR;
    }

    @Override
    public boolean isSupernatural() {
        return false;
    }
}
