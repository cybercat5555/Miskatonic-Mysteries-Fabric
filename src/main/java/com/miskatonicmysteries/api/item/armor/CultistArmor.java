package com.miskatonicmysteries.api.item.armor;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.client.model.armor.CultistRobesModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public abstract class CultistArmor extends ArmorItem implements Affiliated {
    public CultistArmor(EquipmentSlot slot, Settings settings) {
        super(ArmorMaterials.LEATHER, slot, settings);
    }

    public abstract Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot);

  /*  @Environment(EnvType.CLIENT)
    public BipedEntityModel<LivingEntity> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<LivingEntity> original) {
        return new CultistRobesModel(armorSlot, !entityLiving.getEquippedStack(EquipmentSlot.FEET).isEmpty(),
                entityLiving.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof CultistArmor,
                entityLiving.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof CultistArmor);
    }*///todo

}
