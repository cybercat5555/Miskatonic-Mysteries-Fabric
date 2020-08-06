package com.miskatonicmysteries.client.model.block;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * statue_cthulhu - cybercat5555
 * Created using Tabula 8.0.0
 */
@Environment(EnvType.CLIENT)
public class CthulhuStatueModel extends Model {
    public ModelPart plinth;
    public ModelPart body;
    public ModelPart neck;
    public ModelPart Wing01;
    public ModelPart lWing01;
    public ModelPart lLeg01;
    public ModelPart rLeg01;
    public ModelPart lArm01;
    public ModelPart rArm01;
    public ModelPart head;
    public ModelPart tentacleBase;
    public ModelPart tentacle01;
    public ModelPart tentacle02;
    public ModelPart tentacle03;
    public ModelPart tentacle04;
    public ModelPart tentacle05;
    public ModelPart tentacle05_1;
    public ModelPart tentacle06;
    public ModelPart rWing02;
    public ModelPart rWing03;
    public ModelPart rWing04;
    public ModelPart rWing05;
    public ModelPart lWing02;
    public ModelPart lWing03;
    public ModelPart lWing04;
    public ModelPart lWing05;
    public ModelPart lLeg02;
    public ModelPart rLeg02;
    public ModelPart lArm02;
    public ModelPart lArm03;
    public ModelPart rArm02;
    public ModelPart rArm03;

    public CthulhuStatueModel() {
        super(RenderLayer::getEntitySolid);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.lLeg02 = new ModelPart(this, 48, 11);
        this.lLeg02.setPivot(1.7F, -0.1F, -4.5F);
        this.lLeg02.addCuboid(-1.59F, -1.3F, -1.6F, 3.0F, 6.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lLeg02, 0.06981317007977318F, 0.0F, 0.0F);
        this.rLeg01 = new ModelPart(this, 44, 0);
        this.rLeg01.mirror = true;
        this.rLeg01.setPivot(-0.8F, -1.5F, 1.3F);
        this.rLeg01.addCuboid(-3.1F, -1.5F, -5.5F, 3.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rLeg01, -0.20943951023931953F, 0.0F, 0.0F);
        this.tentacle06 = new ModelPart(this, 44, 25);
        this.tentacle06.mirror = true;
        this.tentacle06.setPivot(0.9F, -0.5F, -1.4F);
        this.tentacle06.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacle06, -0.7853981633974483F, -0.08726646259971647F, 0.0F);
        this.rWing05 = new ModelPart(this, 48, 23);
        this.rWing05.mirror = true;
        this.rWing05.setPivot(0.0F, 0.0F, -2.8F);
        this.rWing05.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.lWing02 = new ModelPart(this, 49, 23);
        this.lWing02.setPivot(0.1F, 4.9F, 4.3F);
        this.lWing02.addCuboid(-0.5F, -0.2F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lWing02, -0.3490658503988659F, 0.0F, 0.0F);
        this.lLeg01 = new ModelPart(this, 44, 0);
        this.lLeg01.setPivot(0.8F, -1.5F, 1.3F);
        this.lLeg01.addCuboid(0.1F, -1.5F, -5.5F, 3.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lLeg01, -0.20943951023931953F, 0.0F, 0.0F);
        this.tentacle03 = new ModelPart(this, 43, 25);
        this.tentacle03.setPivot(0.8F, -1.3F, -1.7F);
        this.tentacle03.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacle03, -0.7853981633974483F, 0.0F, 0.0F);
        this.rWing03 = new ModelPart(this, 48, 23);
        this.rWing03.mirror = true;
        this.rWing03.setPivot(0.0F, 0.0F, -0.9F);
        this.rWing03.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.Wing01 = new ModelPart(this, 53, 22);
        this.Wing01.mirror = true;
        this.Wing01.setPivot(-0.7F, -8.0F, 1.8F);
        this.Wing01.addCuboid(-0.5F, 0.0F, 0.8F, 1.0F, 5.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Wing01, 0.2617993877991494F, -0.6981317007977318F, -0.08726646259971647F);
        this.rArm01 = new ModelPart(this, 33, 0);
        this.rArm01.mirror = true;
        this.rArm01.setPivot(-1.7F, -7.0F, 1.3F);
        this.rArm01.addCuboid(-2.1F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rArm01, -0.5235987755982988F, 0.0F, 0.0F);
        this.lWing03 = new ModelPart(this, 48, 23);
        this.lWing03.setPivot(0.0F, 0.0F, -0.9F);
        this.lWing03.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.rArm02 = new ModelPart(this, 32, 7);
        this.rArm02.mirror = true;
        this.rArm02.setPivot(-1.2F, 3.7F, -0.3F);
        this.rArm02.addCuboid(-1.0F, -0.5F, -2.8F, 2.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rArm02, 0.3141592653589793F, 0.0F, -0.17453292519943295F);
        this.rLeg02 = new ModelPart(this, 48, 11);
        this.rLeg02.mirror = true;
        this.rLeg02.setPivot(-1.7F, -0.1F, -4.5F);
        this.rLeg02.addCuboid(-1.39F, -1.3F, -1.6F, 3.0F, 6.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rLeg02, 0.06981317007977318F, 0.0F, 0.0F);
        this.tentacle05_1 = new ModelPart(this, 44, 25);
        this.tentacle05_1.setPivot(-0.9F, -0.5F, -1.4F);
        this.tentacle05_1.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacle05_1, -0.7853981633974483F, 0.08726646259971647F, 0.0F);
        this.lArm02 = new ModelPart(this, 32, 7);
        this.lArm02.setPivot(1.2F, 3.7F, -0.3F);
        this.lArm02.addCuboid(-1.0F, -0.5F, -2.8F, 2.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lArm02, 0.3141592653589793F, 0.0F, 0.17453292519943295F);
        this.tentacle05 = new ModelPart(this, 43, 24);
        this.tentacle05.setPivot(0.5F, -0.5F, -1.4F);
        this.tentacle05.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacle05, -0.8028514559173915F, 0.0F, 0.0F);
        this.lWing01 = new ModelPart(this, 53, 22);
        this.lWing01.setPivot(0.7F, -8.0F, 1.8F);
        this.lWing01.addCuboid(-0.5F, 0.0F, 0.8F, 1.0F, 5.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lWing01, 0.2617993877991494F, 0.6981317007977318F, 0.08726646259971647F);
        this.lWing04 = new ModelPart(this, 49, 23);
        this.lWing04.setPivot(0.0F, 0.0F, -1.9F);
        this.lWing04.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tentacle01 = new ModelPart(this, 43, 26);
        this.tentacle01.setPivot(-0.8F, -1.3F, -1.7F);
        this.tentacle01.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacle01, -0.7853981633974483F, 0.0F, 0.0F);
        this.body = new ModelPart(this, 0, 18);
        this.body.setPivot(0.0F, 16.4F, 0.0F);
        this.body.addCuboid(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(body, 0.13962634015954636F, 0.0F, 0.0F);
        this.tentacle02 = new ModelPart(this, 43, 24);
        this.tentacle02.setPivot(0.0F, -1.3F, -1.7F);
        this.tentacle02.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacle02, -0.9599310885968813F, 0.0F, 0.0F);
        this.rWing02 = new ModelPart(this, 49, 23);
        this.rWing02.mirror = true;
        this.rWing02.setPivot(0.1F, 4.9F, 4.3F);
        this.rWing02.addCuboid(-0.5F, -0.2F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rWing02, -0.3490658503988659F, 0.0F, 0.0F);
        this.rWing04 = new ModelPart(this, 49, 23);
        this.rWing04.mirror = true;
        this.rWing04.setPivot(0.0F, 0.0F, -1.9F);
        this.rWing04.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.neck = new ModelPart(this, 5, 21);
        this.neck.setPivot(0.0F, -7.5F, 0.4F);
        this.neck.addCuboid(-1.5F, -1.4F, -1.7F, 3.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(neck, 0.3490658503988659F, 0.0F, 0.0F);
        this.lArm03 = new ModelPart(this, 32, 13);
        this.lArm03.setPivot(0.0F, 0.0F, -3.0F);
        this.lArm03.addCuboid(-1.0F, -0.5F, -0.8F, 2.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.plinth = new ModelPart(this, 0, 0);
        this.plinth.setPivot(0.0F, 24.0F, 0.0F);
        this.plinth.addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.lWing05 = new ModelPart(this, 48, 23);
        this.lWing05.setPivot(0.0F, 0.0F, -2.8F);
        this.lWing05.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tentacleBase = new ModelPart(this, 37, 18);
        this.tentacleBase.setPivot(0.0F, -2.1F, -1.4F);
        this.tentacleBase.addCuboid(-1.5F, -1.8F, -2.0F, 3.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacleBase, 0.6981317007977318F, 0.0F, 0.0F);
        this.rArm03 = new ModelPart(this, 32, 13);
        this.rArm03.mirror = true;
        this.rArm03.setPivot(0.0F, 0.0F, -3.0F);
        this.rArm03.addCuboid(-1.0F, -0.5F, -0.8F, 2.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tentacle04 = new ModelPart(this, 43, 24);
        this.tentacle04.setPivot(-0.5F, -0.5F, -1.4F);
        this.tentacle04.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tentacle04, -0.8726646259971648F, 0.0F, 0.0F);
        this.lArm01 = new ModelPart(this, 33, 0);
        this.lArm01.setPivot(1.7F, -7.0F, 1.3F);
        this.lArm01.addCuboid(0.2F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lArm01, -0.5235987755982988F, 0.0F, 0.0F);
        this.head = new ModelPart(this, 25, 23);
        this.head.setPivot(0.0F, -1.0F, 0.0F);
        this.head.addCuboid(-2.0F, -3.6F, -2.5F, 4.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(head, -0.3490658503988659F, 0.0F, 0.0F);
        this.lLeg01.addChild(this.lLeg02);
        this.body.addChild(this.rLeg01);
        this.tentacleBase.addChild(this.tentacle06);
        this.rWing02.addChild(this.rWing05);
        this.lWing01.addChild(this.lWing02);
        this.body.addChild(this.lLeg01);
        this.tentacleBase.addChild(this.tentacle03);
        this.rWing02.addChild(this.rWing03);
        this.body.addChild(this.Wing01);
        this.body.addChild(this.rArm01);
        this.lWing02.addChild(this.lWing03);
        this.rArm01.addChild(this.rArm02);
        this.rLeg01.addChild(this.rLeg02);
        this.tentacleBase.addChild(this.tentacle05_1);
        this.lArm01.addChild(this.lArm02);
        this.tentacleBase.addChild(this.tentacle05);
        this.body.addChild(this.lWing01);
        this.lWing02.addChild(this.lWing04);
        this.tentacleBase.addChild(this.tentacle01);
        this.tentacleBase.addChild(this.tentacle02);
        this.Wing01.addChild(this.rWing02);
        this.rWing02.addChild(this.rWing04);
        this.body.addChild(this.neck);
        this.lArm02.addChild(this.lArm03);
        this.lWing02.addChild(this.lWing05);
        this.head.addChild(this.tentacleBase);
        this.rArm02.addChild(this.rArm03);
        this.tentacleBase.addChild(this.tentacle04);
        this.body.addChild(this.lArm01);
        this.neck.addChild(this.head);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.body, this.plinth).forEach((modelRenderer) -> {
            modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }
}
