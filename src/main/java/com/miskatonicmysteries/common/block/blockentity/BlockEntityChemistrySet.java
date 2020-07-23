package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.feature.PotentialItem;
import com.miskatonicmysteries.lib.Constants;
import com.miskatonicmysteries.lib.ModObjects;
import com.miskatonicmysteries.lib.ModRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class BlockEntityChemistrySet extends BlockEntityBase implements ImplementedInventory, Tickable {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private final DefaultedList<PotentialItem> POTENTIAL_ITEMS = DefaultedList.ofSize(3, PotentialItem.EMPTY);
    public int workProgress;
    public int[] smokeColor = {0, 0, 0};

    public BlockEntityChemistrySet() {
        super(ModObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, ITEMS);
        ListTag potentialItemTag = new ListTag();
        for (int i = 0; i < POTENTIAL_ITEMS.size(); i++) {
            PotentialItem item = POTENTIAL_ITEMS.get(i);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putByte("Slot", (byte) i);
            item.toTag(compoundTag);
            potentialItemTag.add(compoundTag);
        }
        tag.put(Constants.NBT.POTENTIAL_ITEMS, potentialItemTag);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        Inventories.fromTag(tag, ITEMS);
        ListTag listTag = tag.getList(Constants.NBT.POTENTIAL_ITEMS, 10);

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if (j >= 0 && j < POTENTIAL_ITEMS.size()) {
                POTENTIAL_ITEMS.set(j, PotentialItem.fromTag(compoundTag));
            }
        }
        super.fromTag(state, tag);
    }


    @Override
    public void tick() {
        if (isLit() && canWork()) {
            ChemistryRecipe recipe = ModRecipes.getRecipe(this);
            workProgress++;
            update();
            if (workProgress >= 100) {
                for (int i = 0; i < recipe.OUTPUT.size(); i++) {
                    changeSmokeColor(recipe.COLOR);
                    POTENTIAL_ITEMS.set(i, recipe.OUTPUT.get(i));
                }
                clear();
                finish();
            }
        } else if (isLit()){
            finish();
            update();
        }
    }

    private void changeSmokeColor(int color) {
        smokeColor[0] = (color >> 16) & 0xff;
        smokeColor[1] = (color >> 8) & 0xff;
        smokeColor[2] = color & 0xff;
    }

    public boolean convertPotentialItem(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        for (PotentialItem potentialItem : POTENTIAL_ITEMS) {
            if (potentialItem.canRealize(stack)) {
                ItemStack realizedStack = potentialItem.realize(stack);
                POTENTIAL_ITEMS.set(POTENTIAL_ITEMS.indexOf(potentialItem), PotentialItem.EMPTY);
                if (!world.isClient) {
                    world.spawnEntity(new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), realizedStack));
                }
                return true;
            }
        }
        return false;
    }

    public void finish() {
        world.setBlockState(pos, world.getBlockState(pos).with(Properties.LIT, false));
        workProgress = 0;
    }

    public boolean canWork() {
        return ModRecipes.getRecipe(this) != null;
    }

    public boolean canBeLit(PlayerEntity playerEntity) {
        if (containsPotentialItems()) {
            playerEntity.sendMessage(new TranslatableText("message.miskatonicmysteries.chemistry_set.contains_items"), true);
            return false;
        } else if (!canWork()) {
            playerEntity.sendMessage(new TranslatableText("message.miskatonicmysteries.chemistry_set.invalid_recipe"), true);
            return false;
        }
        return true;
    }

    public boolean isLit() {
        return world.getBlockState(pos).get(Properties.LIT);
    }

    public boolean containsPotentialItems() {
        for (PotentialItem potentialItem : POTENTIAL_ITEMS) {
            if (!potentialItem.isEmpty()) return true;
        }
        return false;
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return ITEMS;
    }

    public DefaultedList<PotentialItem> getPotentialItems() {
        return POTENTIAL_ITEMS;
    }
}
