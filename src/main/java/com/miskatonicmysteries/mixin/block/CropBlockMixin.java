package com.miskatonicmysteries.mixin.block;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.registry.MMObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends Block {
    public CropBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "withAge", at = @At("HEAD"), cancellable = true)
    public void infestedWheatWithAge(int age, CallbackInfoReturnable<BlockState> cir) {
        if (getDefaultState().getBlock() == Blocks.WHEAT && age == 6 && Math.random() < MiskatonicMysteries.config.world.infestedWheatChance) {
            cir.setReturnValue(MMObjects.INFESTED_WHEAT_CROP.getDefaultState());
        }
    }
}
