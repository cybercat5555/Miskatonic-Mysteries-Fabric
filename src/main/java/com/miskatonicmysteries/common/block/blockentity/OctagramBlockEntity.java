package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.block.OctagramBlock;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.recipe.rite.Rite;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModObjects;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;

public class OctagramBlockEntity extends BaseBlockEntity implements ImplementedInventory, Affiliated, Tickable {
    private static final int RANGE = 8;
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(8, ItemStack.EMPTY);
    public int tickCount;
    public Rite currentRite = null;

    //todo focus power stuff
    public OctagramBlockEntity() {
        super(ModObjects.OCTAGRAM_BLOCK_ENTITY_TYPE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, ITEMS);
        tag.putInt(Constants.NBT.TICK_COUNT, tickCount);
        if (currentRite != null) {
            tag.putString(Constants.NBT.RITE, currentRite.id.toString());
        }
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        Inventories.fromTag(tag, ITEMS);
        tickCount = tag.getInt(Constants.NBT.TICK_COUNT);
        if (tag.contains(Constants.NBT.RITE)) {
            currentRite = Rite.RITES.getOrDefault(new Identifier(tag.getString(Constants.NBT.RITE)), null);
        }
        super.fromTag(state, tag);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (currentRite != null && !currentRite.canCast(this)) {
            currentRite.onCancelled(this);
            currentRite = null;
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return ITEMS;
    }

    @Override
    public void tick() {
        if (world.getTime() % 20 == 0) {  //scan every second
            scanSurroundings();
        }
        if (currentRite != null && currentRite.shouldContinue(this)) {
            currentRite.tick(this);
            if (currentRite.isFinished(this)) {
                currentRite.onFinished(this);
                tickCount = 0;
                currentRite = null;
                return;
            }
            markDirty();
        }
    }

    private void scanSurroundings() {
        // BlockPos.streamOutwards(pos, RANGE, RANGE / 2, RANGE);
        /*
        search for placed foci
        then search for entities

        afterwards iterate thru each to calculte focal power (and instability)
         */
    }

    @Override
    public Identifier getAffiliation() {
        return world.getBlockState(pos).getBlock() instanceof OctagramBlock ? ((OctagramBlock) world.getBlockState(pos).getBlock()).getAffiliation() : null;
    }

    @Override
    public boolean isSupernatural() {
        return world.getBlockState(pos).getBlock() instanceof OctagramBlock && ((OctagramBlock) world.getBlockState(pos).getBlock()).isSupernatural();
    }


    public Vec3d getSummoningPos() {
        return new Vec3d(pos.getX() + 0.5F, pos.getY() + 0.25F, pos.getZ() + 0.5F);
    }
}
