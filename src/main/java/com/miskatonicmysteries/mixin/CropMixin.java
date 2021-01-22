package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.lib.MMObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropMixin extends Block {
    public CropMixin(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "withAge", at = @At("HEAD"), cancellable = true)
    public void randomDisplay(int age, CallbackInfoReturnable<BlockState> cir) {
        if (getDefaultState().getBlock() == Blocks.WHEAT && age == 6 && Math.random() < MiskatonicMysteries.config.world.infestedWheatChance){
            cir.setReturnValue(MMObjects.INFESTED_WHEAT_CROP.getDefaultState());
        }
    }
}
