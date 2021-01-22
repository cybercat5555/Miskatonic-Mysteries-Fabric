package com.miskatonicmysteries.common.item.armor;

import com.miskatonicmysteries.client.model.armor.CultistRobesModel;
import com.miskatonicmysteries.common.feature.interfaces.Affiliated;
import com.miskatonicmysteries.common.lib.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

//parts taken from https://github.com/Vazkii/Botania/blob/1.16.x-fabric/src/main/java/vazkii/botania/common/item/equipment/armor/manasteel/ItemManasteelArmor.java because i am lazy
public abstract class CultistArmor extends ArmorItem implements Affiliated {
    public CultistArmor(EquipmentSlot slot) {
        super(ArmorMaterials.LEATHER, slot, new Item.Settings().group(Constants.MM_GROUP).maxCount(1));
    }

    public abstract Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot);

    @Environment(EnvType.CLIENT)
    public BipedEntityModel<LivingEntity> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<LivingEntity> original) {
        return new CultistRobesModel(armorSlot, !entityLiving.getEquippedStack(EquipmentSlot.FEET).isEmpty(),
                entityLiving.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof CultistArmor,
                entityLiving.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof CultistArmor);
    }

}
