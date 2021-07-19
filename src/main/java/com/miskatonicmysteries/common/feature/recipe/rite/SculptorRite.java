package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.model.block.HasturStatueModel;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.registry.*;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class SculptorRite extends Rite {
    private final int ticksNeeded = 140;
    private final String knowledge;

    public SculptorRite() {
        super(new Identifier(Constants.MOD_ID, "sculptor_rite"), MMAffiliations.HASTUR, 0.2F,
                Ingredient.ofItems(Blocks.CLAY), Ingredient.ofItems(Items.YELLOW_DYE), Ingredient.ofItems(MMObjects.IRIDESCENT_PEARL), Ingredient.ofItems(Items.TERRACOTTA, Items.STONE), Ingredient.ofItems(Items.TERRACOTTA, Items.STONE), Ingredient.ofItems(Items.TERRACOTTA, Items.STONE), Ingredient.ofItems(MMObjects.IRIDESCENT_PEARL), Ingredient.ofItems(MMObjects.OCEANIC_GOLD));
        this.knowledge = MMAffiliations.HASTUR.getId().getPath();
    }

    @Override
    public boolean canCast(OctagramBlockEntity octagram) {
        if (super.canCast(octagram)) {
            if (!octagram.doesCasterHaveKnowledge(knowledge)) {
                if (octagram.getOriginalCaster() != null) {
                    octagram.getOriginalCaster().sendMessage(new TranslatableText("message.miskatonicmysteries.rite_fail.knowledge"), true);
                }
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (octagram.tickCount > 0 && octagram.tickCount % 40 == 0 && octagram.tickCount <= 120) {
            World world = octagram.getWorld();
            if (!world.isClient) {
                octagram.getOriginalCaster().damage(DamageSource.MAGIC, octagram.getOriginalCaster().getMaxHealth() / 3F);
                octagram.getOriginalCaster().addStatusEffect(new StatusEffectInstance(MMStatusEffects.BLEED, 400, 1, true, false, false));
                world.playSound(null, octagram.getPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 0.8F, 1.0F);
            } else {
                Vec3d pos = octagram.getSummoningPos().add(0, 0.25F * octagram.tickCount / 40F, 0);
                Random random = world.random;
                for (int i = 0; i < 7; i++) {
                    MMParticles.spawnCandleParticle(world, pos.x + random.nextGaussian() / 4F, pos.y + random.nextGaussian() / 4F, pos.z + random.nextGaussian() / 4F, 1, true);
                    world.addParticle(MMParticles.DRIPPING_BLOOD, pos.x + random.nextGaussian() / 2F, pos.y + random.nextGaussian() / 2, pos.z + random.nextGaussian() / 2, 0, 0.05F, 0);
                }
            }
        }
        List<HasturCultistEntity> cultists = octagram.getWorld().getEntitiesByClass(HasturCultistEntity.class, octagram.getSelectionBox().expand(10, 5, 10), cultist -> !cultist.isAttacking());
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
        return octagram.getOriginalCaster() != null && !octagram.getOriginalCaster().isDead();
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
    @Environment(EnvType.CLIENT)
    public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
        return 1;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
        VertexConsumer vertexConsumer = ResourceHandler.STATUE_SPRITES.get(getStatueForIngredients(entity)).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        matrixStack.translate(1.5F, 0, 1.5F);
        matrixStack.push();
        HasturStatueModel model = ResourceHandler.HASTUR_STATUE_MODEL;
        model.plinth.visible = entity.tickCount > 40;
        model.body.visible = entity.tickCount > 80;
        model.head.visible = entity.tickCount > 120;
        matrixStack.translate(0, 1.5, 0);
        matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        matrixStack.pop();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderRiteItems(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
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
            matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(0.125F * i * 360F));
            matrixStack.translate(0, 0, -1.1);
            matrixStack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(i), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers);
            matrixStack.pop();
        }
    }
}
