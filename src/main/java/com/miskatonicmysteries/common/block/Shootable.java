package com.miskatonicmysteries.common.block;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Shootable {
    void onShot(World world, BlockPos pos, LivingEntity shooter);
}
