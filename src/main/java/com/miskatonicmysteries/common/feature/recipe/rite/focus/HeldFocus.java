package com.miskatonicmysteries.common.feature.recipe.rite.focus;

import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.BiPredicate;

public class HeldFocus {
    private final Identifier id;
    private final int power;
    private final int maxAmount;
    private final float instabilityRate;
    private final BiPredicate<LivingEntity, ItemStack> predicate;

    protected HeldFocus(Identifier id, int power, int maxCount, float instabilityRate, BiPredicate<LivingEntity, ItemStack> predicate) {
        this.id = id;
        this.power = power;
        this.maxAmount = maxCount;
        this.instabilityRate = instabilityRate;
        this.predicate = predicate;
    }

    public boolean isValid(OctagramBlockEntity octagram, LivingEntity entity, ItemStack stack) {
        return predicate.test(entity, stack);
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

    public int getPowerInContext(int amount, OctagramBlockEntity octagram, LivingEntity entity) {
        return Math.min(amount * power, maxAmount * power);
    }

    public int getInstabilityInContext(int amount, OctagramBlockEntity octagram, LivingEntity entity) {
        return amount * power; //instability just keeps rising
    }
}
