package com.miskatonicmysteries.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;

/**
 * cultist_Hastur - cybercat5555
 * Created using Tabula 8.0.0
 */
@Environment(EnvType.CLIENT)
public class HasturCultistEntityModel extends BipedEntityModel<HasturCultistEntity> {
    protected final ModelPart rightArmFolded;
    protected final ModelPart leftArmFolded;
    protected final ModelPart middleArmFolded;
    private final ModelPart root;

    public HasturCultistEntityModel(ModelPart root) {
        super(root);
        this.root = root;
        this.rightArmFolded = root.getChild("rightArmFolded");
        this.leftArmFolded = rightArmFolded.getChild("leftArmFolded");
        this.middleArmFolded = root.getChild("middleArmFolded");
        this.rightArmFolded.visible = false;
        this.middleArmFolded.visible = false;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        root.addChild(EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create()
                        .uv(40, 46).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create()
                        .uv(0, 22).mirrored(true).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create()
                        .uv(16, 20).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData head = root.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("hoodFringeL01",
                ModelPartBuilder.create()
                        .uv(64, 40).cuboid(0.2F, -0.8F, -5.09F, 1.0F, 10.0F, 9.0F),
                ModelTransform.of(2.3F, -9.2F, 0.1F, 0.0F, 0.0F, -0.2268928F));
        head.addChild("hoodFringeR03",
                ModelPartBuilder.create()
                        .uv(88, 39).mirrored(true).cuboid(-3.2F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F),
                ModelTransform.of(-0.6F, -9.9F, 0.1F, 0.0F, 0.0F, -0.2617994F));
        head.addChild("hoodFringeR01",
                ModelPartBuilder.create()
                        .uv(64, 40).mirrored(true).cuboid(-1.9F, -0.6F, -5.09F, 1.0F, 10.0F, 9.0F),
                ModelTransform.of(-1.7F, -9.2F, 0.1F, 0.0F, 0.0F, 0.2268928F));
        head.addChild("hoodFringeL02",
                ModelPartBuilder.create()
                        .uv(88, 39).cuboid(-3.6F, -0.6F, -5.08F, 4.0F, 1.0F, 9.0F),
                ModelTransform.of(3.3F, -9.2F, 0.1F, 0.0F, 0.0F, 0.2617994F));
        head.addChild("hoodRSide02",
                ModelPartBuilder.create()
                        .uv(79, 51).mirrored(true).cuboid(-0.6F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F),
                ModelTransform.of(-5.15F, 0.1F, 0.3F, 0.0F, 0.0F, 0.31415927F));
        head.addChild("leftMaskPlate",
                ModelPartBuilder.create()
                        .uv(68, 0).cuboid(-0.1F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F),
                ModelTransform.of(0.0F, -4.4F, -4.9F, -0.12217305F, -0.13962634F, 0.0F));
        ModelPartData hoodPipe01 = head.addChild("hoodPipe01",
                ModelPartBuilder.create()
                        .uv(75, 23).cuboid(-4.5F, -2.5F, 0.0F, 9.0F, 10.7F, 2.0F),
                ModelTransform.of(0.0F, -7.3F, 2.7F, 0.0F, 0.0F, 0.0F));
        hoodPipe01.addChild("hoodPipe02",
                ModelPartBuilder.create()
                        .uv(109, 33).cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F),
                ModelTransform.of(0.0F, -0.3F, 1.0F, -0.4537856F, 0.0F, 0.0F));
        head.addChild("nose",
                ModelPartBuilder.create()
                        .uv(24, 0).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        head.addChild("rightMaskPlate",
                ModelPartBuilder.create()
                        .uv(68, 0).mirrored(true).cuboid(-3.9F, -2.5F, -0.5F, 4.0F, 7.0F, 1.0F),
                ModelTransform.of(0.0F, -4.4F, -4.9F, -0.12217305F, 0.13962634F, 0.0F));
        head.addChild("hoodLSide02",
                ModelPartBuilder.create()
                        .uv(79, 51).cuboid(-5.5F, -0.9F, -5.25F, 6.0F, 2.0F, 9.0F),
                ModelTransform.of(5.15F, 0.1F, 0.3F, 0.0F, 0.0F, -0.31415927F));
        root.addChild(EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create()
                        .uv(0, 22).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild("robe",
                ModelPartBuilder.create()
                        .uv(0, 38).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new Dilation(0.5F, 0.5F, 0.5F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create()
                        .uv(40, 46).mirrored(true).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData rightArmFolded = root.addChild("rightArmFolded",
                ModelPartBuilder.create()
                        .uv(44, 22).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                ModelTransform.of(0.0F, 3.0F, -1.0F, -0.749968F, 0.0F, 0.0F));
        rightArmFolded.addChild("leftArmFolded",
                ModelPartBuilder.create()
                        .uv(44, 22).mirrored(true).cuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild("middleArmFolded",
                ModelPartBuilder.create()
                        .uv(40, 38).cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F),
                ModelTransform.of(0.0F, 3.0F, -1.0F, -0.749968F, 0.0F, 0.0F));
        root.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.NONE);
        return TexturedModelData.of(data, 128, 64);
    }

    @Override
    public void animateModel(HasturCultistEntity livingEntity, float f, float g, float h) {
        rightArmPose = livingEntity.getMainHandStack().isEmpty() ? ArmPose.EMPTY : livingEntity.isBlocking() && livingEntity.getMainHandStack().getItem().equals(Items.SHIELD) ? ArmPose.BLOCK : ArmPose.ITEM;
        leftArmPose = livingEntity.getOffHandStack().isEmpty() ? ArmPose.EMPTY : livingEntity.isBlocking() && livingEntity.getOffHandStack().getItem().equals(Items.SHIELD) ? ArmPose.BLOCK : ArmPose.ITEM;
        if (livingEntity.isLeftHanded()) {
            ArmPose tempPose = rightArmPose;
            rightArmPose = leftArmPose;
            leftArmPose = tempPose;
        }
        if (rightArmPose == ArmPose.EMPTY && leftArmPose == ArmPose.EMPTY && !livingEntity.isCasting()) {
            middleArmFolded.visible = true;
            rightArmFolded.visible = true;
            leftArm.visible = false;
            rightArm.visible = false;
        } else {
            middleArmFolded.visible = false;
            rightArmFolded.visible = false;
            leftArm.visible = true;
            rightArm.visible = true;
        }
        super.animateModel(livingEntity, f, g, h);
    }

    @Override
    public void setAngles(HasturCultistEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        if (livingEntity.getHeadRollingTimeLeft() > 0) {
            this.head.roll = 0.3F * MathHelper.sin(0.45F * animationProgress);
            this.head.pitch = 0.4F;
        } else {
            this.head.roll = 0.0F;
        }

        if (livingEntity.isCasting()) {
            this.rightArm.pivotZ = 0.0F;
            this.rightArm.pivotX = -5.0F;
            this.leftArm.pivotZ = 0.0F;
            this.leftArm.pivotX = 5.0F;
            this.rightArm.pitch = MathHelper.cos(animationProgress * 0.6662F) * 0.25F;
            this.leftArm.pitch = MathHelper.cos(animationProgress * 0.6662F) * 0.25F;
            this.rightArm.roll = 2.3561945F;
            this.leftArm.roll = -2.3561945F;
            this.rightArm.yaw = 0.0F;
            this.leftArm.yaw = 0.0F;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.root).forEach((part) -> {
            part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }
}
