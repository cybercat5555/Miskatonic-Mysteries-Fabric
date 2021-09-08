package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.api.item.GunItem;
import com.miskatonicmysteries.common.feature.entity.ProtagonistEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

/**
 * investigator - CyberCat5555
 * Created using Tabula 8.0.0
 */
@Environment(EnvType.CLIENT)
public class ProtagonistEntityModel extends BipedEntityModel<ProtagonistEntity> {
    private final ModelPart root;

    public ProtagonistEntityModel(ModelPart root) {
        super(root);
        this.root = root;
        this.hat.visible = false;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData body = root.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create()
                        .uv(16, 20).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create()
                        .uv(28, 48).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData head = root.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild(EntityModelPartNames.NOSE,
                ModelPartBuilder.create()
                        .uv(24, 0).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("brim",
                ModelPartBuilder.create()
                        .uv(40, 38).cuboid(-5.5F, -5.5F, -7.0F, 11.0F, 11.0F, 1.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.HAT,
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, new Dilation(0.45F, 0.45F, 0.45F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData cigar = head.addChild("cigar",
                ModelPartBuilder.create()
                        .cuboid(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(1.5F, -1.5F, -3.7F, 0.0F, -0.5235988F, 0.0F));
        cigar.addChild("pipe",
                ModelPartBuilder.create()
                        .uv(0, 4).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.of(0.0F, -0.5F, -3.5F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create()
                        .uv(28, 48).mirrored(true).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create()
                        .uv(0, 22).mirrored(true).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData rightLeg = root.addChild(EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create()
                        .uv(0, 22).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        rightLeg.addChild("holster",
                ModelPartBuilder.create()
                        .uv(44, 22).cuboid(-2.4F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        body.addChild("coat",
                ModelPartBuilder.create()
                        .uv(0, 38).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new Dilation(0.5F, 0.5F, 0.5F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void animateModel(ProtagonistEntity livingEntity, float f, float g, float h) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        ItemStack itemStack = livingEntity.getStackInHand(Hand.MAIN_HAND);
        if ((itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof GunItem) && livingEntity.isAttacking()) {
            if (livingEntity.getMainArm() == Arm.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        } else if (itemStack.getItem() == Items.CROSSBOW) {
            if (livingEntity.getMainArm() == Arm.RIGHT) {
                this.rightArmPose = livingEntity.isCharging() ? ArmPose.CROSSBOW_CHARGE : ArmPose.CROSSBOW_HOLD;
            } else {
                this.leftArmPose = livingEntity.isCharging() ? ArmPose.CROSSBOW_CHARGE : ArmPose.CROSSBOW_HOLD;
            }
        }
        if (livingEntity.isLeftHanded()) {
            ArmPose tempPose = rightArmPose;
            rightArmPose = leftArmPose;
            leftArmPose = tempPose;
        }
        super.animateModel(livingEntity, f, g, h);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }


    @Override
    public void setAngles(ProtagonistEntity livingEntity, float f, float g, float h, float i, float j) {
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
