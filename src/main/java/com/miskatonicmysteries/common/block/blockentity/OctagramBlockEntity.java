package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.block.OctagramBlock;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.recipe.rite.Rite;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.item.armor.CultistArmor;
import com.miskatonicmysteries.common.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class OctagramBlockEntity extends BaseBlockEntity implements ImplementedInventory, Affiliated, Tickable {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(8, ItemStack.EMPTY);
    public int tickCount;
    public boolean permanentRiteActive;
    public Rite currentRite = null;
    public PlayerEntity originalCaster = null;

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
        tag.putBoolean(Constants.NBT.PERMANENT_RITE, permanentRiteActive);
        if (originalCaster != null) {
            tag.putUuid(Constants.NBT.PLAYER_UUID, originalCaster.getUuid());
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
        permanentRiteActive = tag.getBoolean(Constants.NBT.PERMANENT_RITE);
        if (tag.contains(Constants.NBT.PLAYER_UUID)) {
            originalCaster = world.getPlayerByUuid(tag.getUuid(Constants.NBT.PLAYER_UUID));
        }
        super.fromTag(state, tag);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (currentRite != null && !permanentRiteActive && !currentRite.canCast(this)) {
            currentRite.onCancelled(this);
            tickCount = 0;
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
        if (currentRite != null && currentRite.shouldContinue(this)) {
            currentRite.tick(this);
            if (currentRite.isFinished(this)) {
                handleInvestigators();
                if (currentRite.isPermanent(this)) {
                    permanentRiteActive = true;
                    currentRite.onFinished(this);
                } else {
                    currentRite.onFinished(this);
                    tickCount = 0;
                    currentRite = null;
                }
            }
            markDirty();
        } else {
            originalCaster = null;
        }
    }

    private void handleInvestigators() {

        if (originalCaster != null && !world.isClient && world.random.nextFloat() < currentRite.investigatorChance) {
            float subtlety = 0;
            if (MiskatonicMysteries.config.entities.subtlety) {
                for (BlockPos blockPos : BlockPos.iterateOutwards(pos, 8, 8, 8)) {
                    Block block = world.getBlockState(blockPos).getBlock();
                    if (block.isIn(Constants.Tags.SUBTLE_BLOCKS)) {
                        subtlety += 0.05F;
                    } else if (block.isIn(Constants.Tags.SUSPICIOUS_BLOCKS)) {
                        subtlety -= 0.05F;
                    }
                    if (subtlety >= 0.35F) {
                        break;
                    }
                }
                subtlety += world.isNight() ? 0.15F : -0.1;
                subtlety += MaskTrinketItem.getMask(originalCaster).isEmpty() ? 0 : 0.15F;
                for (ItemStack armor : originalCaster.getArmorItems()) {
                    if (armor.getItem() instanceof CultistArmor) {
                        subtlety += 0.1F;
                    }
                }
                subtlety = Math.min(subtlety, 0.8F);
            } else {
                subtlety = 0.25F;
            }
            if (world.random.nextFloat() > subtlety) {
                ProtagonistHandler.spawnProtagonist((ServerWorld) world, originalCaster);
            }
        }
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return getCachedState().getBlock() instanceof OctagramBlock ? ((OctagramBlock) world.getBlockState(pos).getBlock()).getAffiliation(true) : null;
    }

    @Override
    public boolean isSupernatural() {
        return getCachedState().getBlock() instanceof OctagramBlock && ((OctagramBlock) world.getBlockState(pos).getBlock()).isSupernatural();
    }


    public Vec3d getSummoningPos() {
        return new Vec3d(pos.getX() + 0.5F, pos.getY() + 0.25F, pos.getZ() + 0.5F);
    }

    public Box getSelectionBox() {
        return new Box(pos.add(-1, -1, -1), pos.add(2, 2, 2));
    }
}
