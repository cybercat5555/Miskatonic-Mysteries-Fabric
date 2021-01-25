package com.miskatonicmysteries.mixin.world;

import com.miskatonicmysteries.common.lib.MMObjects;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(OceanMonumentGenerator.CoreRoom.class)
public abstract class OceanMonumentMixin extends OceanMonumentGenerator.Piece {
    public OceanMonumentMixin(StructurePieceType structurePieceType, int i) {
        super(structurePieceType, i);
    }

    @Inject(method = "generate",
            at = @At(value = "INVOKE",
                    target = "net/minecraft/structure/OceanMonumentGenerator$CoreRoom.fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIILnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V",
                    ordinal = 14))
    private void generate(StructureWorldAccess structureWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        System.out.println(blockPos);
        this.fillWithOutline(structureWorldAccess, boundingBox, 7, 4, 7, 8, 5, 8, MMObjects.OCEANIC_GOLD_PILLAR_ORNATE.getDefaultState(), MMObjects.WARDED_OCEANIC_GOLD_BLOCK.getDefaultState(), false);
    }
}
