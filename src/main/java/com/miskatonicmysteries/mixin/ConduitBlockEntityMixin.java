package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {
    @Inject(method = "updateActivatingBlocks", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void updateActivatingBlocks(World world, BlockPos pos, List<BlockPos> activatingBlocks, CallbackInfoReturnable<Boolean> cir, int l, int m, int n, BlockPos pos2, BlockState state){
        if (state.isOf(MMObjects.PRISMARINE_CTHULHU_MURAL) || Constants.Tags.OCEANIC_GOLD_BLOCKS.contains(state.getBlock())){
            activatingBlocks.add(pos2);
        }
    }
}
