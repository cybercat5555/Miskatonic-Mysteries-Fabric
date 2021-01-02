package com.miskatonicmysteries.common.feature.recipe.rite.focus;

import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.BiPredicate;

public class PlacedFocus {
    private final Identifier id;
    private final int power;
    private final int maxAmount;
    private final float instabilityRate;
    private final BiPredicate<World, BlockPos> predicate;

    protected PlacedFocus(Identifier id, int power, int maxCount, float instabilityRate, BiPredicate<World, BlockPos> predicate) {
        this.id = id;
        this.power = power;
        this.maxAmount = maxCount;
        this.instabilityRate = instabilityRate;
        this.predicate = predicate;
    }

    public boolean isValid(OctagramBlockEntity octagram, World world, BlockPos pos) {
        return predicate.test(world, pos);
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public float getInstabilityRate() {
        return instabilityRate;
    }

    public int getPower() {
        return power;
    }

    public int getPowerInContext(int amount, OctagramBlockEntity octagram, BlockPos pos) {
        return Math.min(amount * power, maxAmount * power);
    }

    public int getInstabilityInContext(int amount, OctagramBlockEntity octagram, BlockPos pos) {
        return amount * power; //instability just keeps rising
    }
}
