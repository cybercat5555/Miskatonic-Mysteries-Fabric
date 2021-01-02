package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class CraftingRite extends Rite {
    private int ticksNeeded;
    private ItemStack result;

    public CraftingRite(Identifier id, int ticksNeeded, ItemStack result, Ingredient... ingredients) {
        super(id, ingredients);
        this.ticksNeeded = ticksNeeded;
        this.result = result;
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.tickCount >= ticksNeeded;
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        octagram.clear();
        if (!octagram.getWorld().isClient) {
            Vec3d pos = octagram.getSummoningPos();
            octagram.getWorld().spawnEntity(new ItemEntity(octagram.getWorld(), pos.x, pos.y, pos.z, result));
            octagram.markDirty();
        }
    }
}
