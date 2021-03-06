package com.miskatonicmysteries.common.item.armor;

import com.miskatonicmysteries.api.item.armor.CultistArmor;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HasturCultistArmor extends CultistArmor {
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/model/armor/yellow_robes.png");

    public HasturCultistArmor(EquipmentSlot slot) {
        super(slot, new Settings().group(Constants.MM_GROUP).maxCount(1));
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        return TEXTURE;
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return MMAffiliations.HASTUR;
    }

    @Override
    public boolean isSupernatural() {
        return false;
    }
}
