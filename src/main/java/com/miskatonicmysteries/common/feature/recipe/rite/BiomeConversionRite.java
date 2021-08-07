package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.BiomeMask;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

public class BiomeConversionRite extends AscensionLockedRite {
    private Function<ServerWorld, Biome> biomeSupplier;

    public BiomeConversionRite(Identifier id, @Nullable Affiliation octagram, Function<ServerWorld, Biome> biomeSupplier, String knowledge, int stage, Ingredient... ingredients) {
        super(id, octagram, knowledge, 0, stage, ingredients);
        this.biomeSupplier = biomeSupplier;
    }


    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return false;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (octagram.tickCount <= 43200){
            octagram.tickCount++;
        }
        if (octagram.getWorld() instanceof ServerWorld serverWorld && serverWorld.getTime() % 20 == 0){
            Random random = serverWorld.getRandom();
            int radius = octagram.tickCount / 120;
            Biome biome = biomeSupplier.apply(serverWorld);
            BlockPos center = octagram.getPos();
            Iterable<BlockPos> blockPosIterable = BlockPos.iterateRandomly(random, 1 + random.nextInt(3), center.getX() - radius, center.getY(), center.getZ() - radius, center.getX() + radius, center.getY(), center.getZ() + radius);
            for (BlockPos targetPos : blockPosIterable) {
                for (int i = -radius; i < radius; i++) {
                    MiskatonicMysteriesAPI.setBiomeMask(octagram.getWorld(), targetPos.add(0, i, 0), biome);
                }
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRendererFactory.Context context) {
        super.renderRite(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
    }
}
