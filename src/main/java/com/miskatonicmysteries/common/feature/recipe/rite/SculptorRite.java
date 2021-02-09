package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.model.block.HasturStatueModel;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class SculptorRite extends Rite {
    private static final HasturStatueModel MODEL = new HasturStatueModel();
    private final int ticksNeeded = 140;
    private final String knowledge;

    public SculptorRite() {
        super(new Identifier(Constants.MOD_ID, "sculptor_rite"), MMAffiliations.HASTUR, 0.2F,
                Ingredient.ofItems(Items.CLAY_BALL), Ingredient.ofItems(Items.YELLOW_DYE), Ingredient.ofItems(Items.TERRACOTTA, Items.STONE), Ingredient.ofItems(Items.TERRACOTTA, Items.STONE), Ingredient.ofItems(Items.TERRACOTTA, Items.STONE));
        this.knowledge = MMAffiliations.HASTUR.getId().getPath();
    }

    @Override
    public boolean canCast(OctagramBlockEntity octagram) {
        if (octagram.doesNearestAltarHaveKnowledge(knowledge)) {
            return super.canCast(octagram);
        }
        return false;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (octagram.tickCount > 0 && octagram.tickCount % 40 == 0) {
            octagram.getWorld().playSound(null, octagram.getPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 0.8F, 1.0F);
            Vec3d pos = octagram.getSummoningPos().add(0, 0.25F * octagram.tickCount / 40F, 0);
            Random random = octagram.getWorld().random;
            for (int i = 0; i < 7; i++) {
                MMParticles.spawnCandleParticle(octagram.getWorld(), pos.x + random.nextGaussian() / 4F, pos.y + random.nextGaussian() / 4F, pos.z + random.nextGaussian() / 4F, 1, true);
            }
        }
        List<HasturCultistEntity> cultists = octagram.getWorld().getEntitiesByClass(HasturCultistEntity.class, octagram.getSelectionBox().expand(10, 5, 10), cultist -> cultist.getTarget() == null);
        Vec3d pos = octagram.getSummoningPos();
        for (HasturCultistEntity cultist : cultists) {
            cultist.getNavigation().startMovingTo(pos.x, pos.y, pos.z, 0.8F);
            if (cultist.getPos().distanceTo(pos) < 5) {
                cultist.getNavigation().stop();
                cultist.currentSpell = null;
                cultist.setCastTime(20);
            }
        }
        super.tick(octagram);
    }

    @Override
    public boolean shouldContinue(OctagramBlockEntity octagram) {
        return octagram.getOriginalCaster() != null;
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        World world = octagram.getWorld();
        world.playSound(null, octagram.getPos(), MMSounds.MAGIC, SoundCategory.PLAYERS, 0.8F, 1.0F);
        Vec3d pos = octagram.getSummoningPos().add(0, 0.5F, 0);
        if (!world.isClient) {
            ItemEntity result = new ItemEntity(world, pos.x, pos.y, pos.z, StatueBlock.setCreator(new ItemStack(getStatueForIngredients(octagram)), octagram.getOriginalCaster()));
            result.setVelocity(0, 0, 0);
            result.setNoGravity(true);
            world.spawnEntity(result);
        } else {
            for (int i = 0; i < 20; i++) {
                MMParticles.spawnCandleParticle(world, pos.x + world.random.nextGaussian(), pos.y + world.random.nextGaussian(), pos.z + world.random.nextGaussian(), 1, true);
            }
        }
        super.onFinished(octagram);
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.triggered && octagram.tickCount >= ticksNeeded;
    }

    private static StatueBlock getStatueForIngredients(OctagramBlockEntity octagram) {
        for (ItemStack item : octagram.getItems()) {
            if (item.getItem() == Items.STONE) {
                return MMObjects.HASTUR_STATUE_STONE;
            } else if (item.getItem() == Items.TERRACOTTA) {
                return MMObjects.HASTUR_STATUE_TERRACOTTA;
            }
        }
        return MMObjects.HASTUR_STATUE_STONE;
    }

    @Override
    public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        return 1;
    }

    @Override
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        VertexConsumer vertexConsumer = ResourceHandler.STATUE_SPRITES.get(getStatueForIngredients(entity)).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        matrixStack.translate(1.5F, 0, 1.5F);
        matrixStack.push();
        MODEL.plinth.visible = entity.tickCount > 40;
        MODEL.body.visible = entity.tickCount > 80;
        MODEL.head.visible = entity.tickCount > 120;
        matrixStack.translate(0, 1.5, 0);
        matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(180));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
        MODEL.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        matrixStack.pop();
    }

    @Override
    public void renderRiteItems(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        int count = 0;
        int maxAllowedCount = 3 - (entity.tickCount / 40);
        for (int i = 0; i < entity.size(); i++) {
            ItemStack stack = entity.getStack(i);
            if ((stack.getItem() == Items.STONE || stack.getItem() == Items.TERRACOTTA)) {
                count++;
                if (count > maxAllowedCount) {
                    continue;
                }
            }
            matrixStack.push();
            matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(0.125F * i * 360F));
            matrixStack.translate(0, 0, -1.1);
            matrixStack.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(90));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(i), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers);
            matrixStack.pop();
        }
    }
}
