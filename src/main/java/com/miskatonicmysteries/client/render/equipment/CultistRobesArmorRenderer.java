package com.miskatonicmysteries.client.render.equipment;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.armor.CultistRobesModel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CultistRobesArmorRenderer implements ArmorRenderer {

	private static CultistRobesModel armorModel;

	private final Identifier texture;

	public CultistRobesArmorRenderer(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity,
					   EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		if (armorModel == null) {
			armorModel = new CultistRobesModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(MMModels.ROBES));
		}
		contextModel.setAttributes(armorModel);
		armorModel.setVisible(false);
		armorModel.head.visible = slot == EquipmentSlot.HEAD;
		armorModel.body.visible = true;
		armorModel.hoodShawl.visible = slot == EquipmentSlot.HEAD;
		armorModel.armorBody.visible = slot == EquipmentSlot.CHEST;
		armorModel.leftArm.visible = slot == EquipmentSlot.CHEST;
		armorModel.rightArm.visible = slot == EquipmentSlot.CHEST;
		armorModel.leftLeg.visible = slot == EquipmentSlot.LEGS;
		armorModel.rightLeg.visible = slot == EquipmentSlot.LEGS;
		armorModel.lowerLeftSkirt.visible = entity.getEquippedStack(EquipmentSlot.FEET).isEmpty();
		armorModel.lowerRightSkirt.visible = armorModel.lowerLeftSkirt.visible;
		ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, armorModel, texture);
	}
}
