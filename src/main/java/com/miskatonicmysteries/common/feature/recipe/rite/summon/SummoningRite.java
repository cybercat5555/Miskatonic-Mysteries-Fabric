package com.miskatonicmysteries.common.feature.recipe.rite.summon;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.rite.AscensionLockedRite;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import javax.annotation.Nullable;

public abstract class SummoningRite<T extends Entity> extends AscensionLockedRite {
    protected int tickCount;
    protected EntityType<T> summon;
    protected @Nullable
    NbtCompound nbt;
    protected @Nullable
    EntityData data;

    public SummoningRite(Identifier id, @Nullable Affiliation octagram, String knowledge, float investigatorChance, int stage, EntityType<T> summon, Ingredient... ingredients) {
        super(id, octagram, knowledge, investigatorChance, stage, ingredients);
        this.summon = summon;
        tickCount = 200;
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        if (octagram.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) octagram.getWorld();
            T entity = summon.create(world, nbt, null, octagram.getOriginalCaster(), octagram.getPos(), SpawnReason.MOB_SUMMONED, true, false);
            Vec3d pos = octagram.getSummoningPos();
            Direction direction = world.getBlockState(octagram.getPos()).get(HorizontalFacingBlock.FACING);
            entity.updatePositionAndAngles(pos.x, pos.y, pos.z, direction.asRotation() + 180, 90);
            addDataToSummon(world, octagram, entity);
            world.spawnEntityAndPassengers(entity);
        }
        super.onFinished(octagram);
    }

    protected void addDataToSummon(ServerWorld world, OctagramBlockEntity octagram, T entity) {
        if (entity instanceof MobEntity) {
            ((MobEntity) entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
        }
    }

    @Override
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
        super.renderRite(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
        if (entity.tickCount > 0) {
            float alpha = entity.tickCount > 20 ? 1 : entity.tickCount / (float) 20;
            float[] rgb = entity.getAffiliation(true).getColor();
            renderPortalOctagram(alpha, rgb, entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
            Model model = getRenderedModel(entity);
            double distance = entity.getPos().getSquaredDistance(context.getRenderDispatcher().camera.getPos(), true);
            matrixStack.translate(1.5, 1.5, 1.5);
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            RenderSystem.enableBlend();
            alpha = entity.tickCount / 200F;
            model.render(matrixStack, ResourceHandler.TOTAL_DARK.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderHelper.getTransparency())), 0, overlay, 0, 0, 0, alpha);
            RenderHelper.renderModelAsPortal(vertexConsumers, matrixStack, light, overlay, model, rgb, alpha);
        }
    }

    @Environment(EnvType.CLIENT)
    protected abstract Model getRenderedModel(OctagramBlockEntity entity);

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.tickCount >= 200;
    }
}
