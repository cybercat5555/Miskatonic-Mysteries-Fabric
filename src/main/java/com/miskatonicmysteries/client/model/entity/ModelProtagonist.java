package com.miskatonicmysteries.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.miskatonicmysteries.common.entity.EntityProtagonist;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

/**
 * investigator - CyberCat5555
 * Created using Tabula 8.0.0
 */
@Environment(EnvType.CLIENT)
public class ModelProtagonist extends BipedEntityModel<EntityProtagonist> {
    public ModelPart coat;
    public ModelPart nose;
    public ModelPart hat;
    public ModelPart brim;
    public ModelPart cigar;
    public ModelPart pipe;
    public ModelPart holster;

    public ModelProtagonist() {
        super(1, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.holster = new ModelPart(this, 44, 22);
        this.holster.setPivot(0.0F, 0.0F, 0.0F);
        this.holster.addCuboid(-2.4F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.nose = new ModelPart(this, 24, 0);
        this.nose.setPivot(0.0F, -2.0F, 0.0F);
        this.nose.addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.rightArm = new ModelPart(this, 28, 48);
        this.rightArm.setPivot(-5.0F, 2.0F, 0.0F);
        this.rightArm.addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.torso = new ModelPart(this, 16, 20);
        this.torso.setPivot(0.0F, 0.0F, 0.0F);
        this.torso.addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.brim = new ModelPart(this, 40, 38);
        this.brim.setPivot(0.0F, 0.0F, 0.0F);
        this.brim.addCuboid(-5.5F, -5.5F, -7.0F, 11.0F, 11.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(brim, -1.5707963267948966F, 0.0F, 0.0F);
        this.head = new ModelPart(this, 0, 0);
        this.head.setPivot(0.0F, 0.0F, 0.0F);
        this.head.addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.leftArm = new ModelPart(this, 28, 48);
        this.leftArm.mirror = true;
        this.leftArm.setPivot(5.0F, 2.0F, 0.0F);
        this.leftArm.addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.leftLeg = new ModelPart(this, 0, 22);
        this.leftLeg.mirror = true;
        this.leftLeg.setPivot(2.0F, 12.0F, 0.0F);
        this.leftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.hat = new ModelPart(this, 32, 0);
        this.hat.setPivot(0.0F, 0.0F, 0.0F);
        this.hat.addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0.45F, 0.45F, 0.45F);
        this.pipe = new ModelPart(this, 0, 4);
        this.pipe.setPivot(0.0F, -0.5F, -3.5F);
        this.pipe.addCuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.rightLeg = new ModelPart(this, 0, 22);
        this.rightLeg.setPivot(-2.0F, 12.0F, 0.0F);
        this.rightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.coat = new ModelPart(this, 0, 38);
        this.coat.setPivot(0.0F, 0.0F, 0.0F);
        this.coat.addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, 0.5F, 0.5F);
        this.cigar = new ModelPart(this, 0, 0);
        this.cigar.setPivot(1.5F, -1.5F, -3.7F);
        this.cigar.addCuboid(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(cigar, 0.0F, -0.5235987755982988F, 0.0F);
        this.rightLeg.addChild(this.holster);
        this.head.addChild(this.nose);
        this.torso.addChild(this.rightArm);
        this.head.addChild(this.brim);
        this.torso.addChild(this.head);
        this.torso.addChild(this.leftArm);
        this.torso.addChild(this.leftLeg);
        this.head.addChild(this.hat);
        this.cigar.addChild(this.pipe);
        this.torso.addChild(this.rightLeg);
        this.torso.addChild(this.coat);
        this.head.addChild(this.cigar);
        helmet.visible = false;
    }

    @Override
    public void animateModel(EntityProtagonist livingEntity, float f, float g, float h) {
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        ItemStack itemStack = livingEntity.getStackInHand(Hand.MAIN_HAND);
        if (itemStack.getItem() == Items.BOW && livingEntity.isAttacking()) {
            if (livingEntity.getMainArm() == Arm.RIGHT) {
                this.rightArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
            }
        }

        super.animateModel(livingEntity, f, g, h);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.torso).forEach((ModelPart) -> {
            ModelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }


    @Override
    public void setAngles(EntityProtagonist livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelPart, float x, float y, float z) {
        modelPart.pitch = x;
        modelPart.yaw = y;
        modelPart.roll = z;
    }
}
