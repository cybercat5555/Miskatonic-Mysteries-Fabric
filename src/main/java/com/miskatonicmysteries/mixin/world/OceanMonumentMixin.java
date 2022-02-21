package com.miskatonicmysteries.mixin.world;

import com.miskatonicmysteries.common.registry.MMObjects;
import java.util.Random;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.structure.StructurePiece;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OceanMonumentGenerator.CoreRoom.class)
public abstract class OceanMonumentMixin extends StructurePiece {

	private OceanMonumentMixin(StructurePieceType type, int length, BlockBox boundingBox) {
		super(type, length, boundingBox);
	}

	@Inject(method = "generate",
		at = @At(value = "INVOKE",
			target = "net/minecraft/structure/OceanMonumentGenerator$CoreRoom.fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIILnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V",
			ordinal = 14))
	private void generate(StructureWorldAccess structureWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci) {
		this.fillWithOutline(structureWorldAccess, boundingBox, 7, 4, 7, 8, 5, 8, MMObjects.OCEANIC_GOLD_PILLAR_ORNATE.getDefaultState(),
			MMObjects.WARDED_OCEANIC_GOLD_BLOCK.getDefaultState(), false);
	}
}
