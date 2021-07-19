package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.item.IncantationYogItem;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TeleportRite extends Rite {
    private final int ticksNeeded;

    public TeleportRite() {
        super(new Identifier(Constants.MOD_ID, "teleport"), null, 0, Ingredient.ofItems(MMObjects.INCANTATION_YOG), Ingredient.ofItems(Items.ENDER_PEARL), Ingredient.ofItems(Items.ENDER_EYE), Ingredient.ofItems(MMObjects.OCEANIC_GOLD));
        ticksNeeded = 60;
    }

    @Override
    public boolean canCast(OctagramBlockEntity octagram) {
        if (super.canCast(octagram)) {
            if (octagram.getWorld().isClient) {
                return true;
            }
            PlayerEntity caster = octagram.getOriginalCaster();
            ItemStack incantation = octagram.getStack(MMObjects.INCANTATION_YOG);
            if (!incantation.isEmpty() && IncantationYogItem.getPosition(incantation) != null && IncantationYogItem.getWorld((ServerWorld) octagram.getWorld(), incantation) != null) {
                BlockPos octagramPos = IncantationYogItem.getPosition(incantation);
                ServerWorld boundWorld = IncantationYogItem.getWorld((ServerWorld) octagram.getWorld(), incantation);
                if (!(boundWorld.getBlockEntity(octagramPos) instanceof OctagramBlockEntity)) {
                    caster.sendMessage(new TranslatableText("message.miskatonicmysteries.invalid_octagram.not_present"), true);
                    return false;
                }
                if (octagramPos.equals(octagram.getPos())) {
                    caster.sendMessage(new TranslatableText("message.miskatonicmysteries.invalid_connection.self_reference"), true);
                    return false;
                }
                if (((OctagramBlockEntity) boundWorld.getBlockEntity(octagramPos)).getAffiliation(false) != octagram.getAffiliation(false)) {
                    caster.sendMessage(new TranslatableText("message.miskatonicmysteries.invalid_octagram.bad_affiliation"), true);
                    return false;
                }
                if (((OctagramBlockEntity) boundWorld.getBlockEntity(octagramPos)).boundPos != null) {
                    caster.sendMessage(new TranslatableText("message.miskatonicmysteries.invalid_octagram.already_bound"), true);
                    return false;
                }

                return true;
            } else if (caster != null) {
                caster.sendMessage(new TranslatableText("message.miskatonicmysteries.invalid_incantation"), true);
                return false;
            }
        }
        return false;
    }


    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (!isFinished(octagram) && !octagram.permanentRiteActive) {
            super.tick(octagram);
        }
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        if (!octagram.getWorld().isClient) {
            ServerWorld world = (ServerWorld) octagram.getWorld();
            octagram.tickCount = 0;
            ItemStack incantation = octagram.getStack(MMObjects.INCANTATION_YOG);
            if (!incantation.isEmpty()) {
                BlockPos octagramPos = IncantationYogItem.getPosition(incantation);
                ServerWorld boundWorld = IncantationYogItem.getWorld(world, incantation);
                if (boundWorld.getBlockEntity(octagramPos) instanceof OctagramBlockEntity) {
                    octagram.bind(boundWorld, octagramPos);
                    OctagramBlockEntity otherOctagram = (OctagramBlockEntity) boundWorld.getBlockEntity(octagramPos);
                    otherOctagram.bind(world, octagram.getPos());
                    otherOctagram.permanentRiteActive = true;
                    otherOctagram.currentRite = this;
                    otherOctagram.tickCount = 0;
                    otherOctagram.markDirty();
                    otherOctagram.sync();
                }
            }
        }
        super.onFinished(octagram);
    }

    @Override
    public void onCancelled(OctagramBlockEntity octagram) {
        OctagramBlockEntity otherOctagram = getBoundOctagram(octagram);
        if (otherOctagram != null && !otherOctagram.getWorld().isClient) {
            otherOctagram.permanentRiteActive = false;
            otherOctagram.currentRite = null;
            otherOctagram.tickCount = 0;
            octagram.boundPos = null;
            otherOctagram.boundPos = null;
            otherOctagram.markDirty();
            octagram.markDirty();
            otherOctagram.sync();
        }
        super.onCancelled(octagram);
    }


    public static OctagramBlockEntity getBoundOctagram(OctagramBlockEntity octagram) {
        BlockPos octagramPos = octagram.getBoundPos();
        ServerWorld boundWorld = octagram.getBoundDimension();
        if (octagramPos != null && boundWorld != null) {
            BlockEntity be = boundWorld.getBlockEntity(octagramPos);
            if (be instanceof OctagramBlockEntity) {
                return (OctagramBlockEntity) be;
            }
        }
        return null;
    }

    @Override
    public boolean shouldContinue(OctagramBlockEntity octagram) {
        return super.shouldContinue(octagram);
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.tickCount >= ticksNeeded;
    }

    @Override
    public boolean isPermanent(OctagramBlockEntity octagram) {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
        return super.beforeRender(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
        float alpha = entity.permanentRiteActive ? 1 : entity.tickCount / (float) ticksNeeded;
        renderPortalOctagram(alpha, entity.getAffiliation(true).getColor(), entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
    }
}
