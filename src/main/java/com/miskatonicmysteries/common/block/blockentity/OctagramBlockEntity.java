package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.item.armor.CultistArmor;
import com.miskatonicmysteries.api.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.item.IncantationYogItem;
import com.miskatonicmysteries.common.registry.MMCriteria;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.UUID;

public class OctagramBlockEntity extends BaseBlockEntity implements ImplementedBlockEntityInventory, Affiliated, Tickable {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(8, ItemStack.EMPTY);
    public int tickCount;
    public boolean permanentRiteActive;
    public Rite currentRite = null;
    public UUID originalCaster = null;
    //misc values which may be used by rites
    public Pair<Identifier, BlockPos> boundPos = null;
    public boolean triggered;
    public Entity targetedEntity = null;
    /**
     * Octagram flags may be used by Rites
     * Reserved flags:
     * 1 - Bloody (has something been sacrificed nearby?)
     */
    private byte octagramFlags;

    public OctagramBlockEntity() {
        super(MMObjects.OCTAGRAM_BLOCK_ENTITY_TYPE);

    }

    public void setFlag(int index, boolean value) {
        if (value) {
            octagramFlags |= 1 << index;
        } else {
            octagramFlags &= ~(1 << index);
        }
        markDirty();
    }

    public boolean getFlag(int index) {
        return (octagramFlags & 1 << index) != 0;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        Inventories.writeNbt(tag, ITEMS);
        tag.putInt(Constants.NBT.TICK_COUNT, tickCount);
        if (currentRite != null) {
            tag.putString(Constants.NBT.RITE, currentRite.getId().toString());
        }
        tag.putBoolean(Constants.NBT.PERMANENT_RITE, permanentRiteActive);
        if (originalCaster != null) {
            tag.putUuid(Constants.NBT.PLAYER_UUID, originalCaster);
        }
        if (boundPos != null) {
            tag.putString(Constants.NBT.DIMENSION, boundPos.getFirst().toString());
            tag.putLong(Constants.NBT.POSITION, boundPos.getSecond().asLong());
        }
        tag.putBoolean(Constants.NBT.TRIGGERED, triggered);
        tag.putByte(Constants.NBT.FLAGS, octagramFlags);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(BlockState state, NbtCompound tag) {
        ITEMS.clear();
        Inventories.readNbt(tag, ITEMS);
        tickCount = tag.getInt(Constants.NBT.TICK_COUNT);
        if (tag.contains(Constants.NBT.RITE)) {
            currentRite = MMRegistries.RITES.get(new Identifier(tag.getString(Constants.NBT.RITE)));
        } else {
            currentRite = null;
        }
        permanentRiteActive = tag.getBoolean(Constants.NBT.PERMANENT_RITE);
        if (tag.contains(Constants.NBT.PLAYER_UUID)) {
            originalCaster = tag.getUuid(Constants.NBT.PLAYER_UUID);
        } else {
            originalCaster = null;
        }
        if (tag.contains(Constants.NBT.DIMENSION)) {
            boundPos = new Pair<>(new Identifier(tag.getString(Constants.NBT.DIMENSION)), BlockPos.fromLong(tag.getLong(Constants.NBT.POSITION)));
        } else {
            boundPos = null;
        }
        triggered = tag.getBoolean(Constants.NBT.TRIGGERED);
        octagramFlags = tag.getByte(Constants.NBT.FLAGS);
        super.readNbt(state, tag);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (currentRite != null && !permanentRiteActive && !currentRite.shouldContinue(this)) {
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
        if (currentRite != null) {
            if (currentRite.shouldContinue(this)) {
                currentRite.tick(this);
                if (currentRite.isFinished(this)) {

                    if (getOriginalCaster() instanceof ServerPlayerEntity) {
                        MMCriteria.RITE_CAST.trigger((ServerPlayerEntity) getOriginalCaster(), currentRite);
                    }
                    handleInvestigators();
                    if (currentRite.isPermanent(this)) {
                        permanentRiteActive = true;
                        currentRite.onFinished(this);
                    } else {
                        currentRite.onFinished(this);
                        tickCount = 0;
                        currentRite = null;
                        targetedEntity = null;
                        setFlag(0, false);
                    }
                    if (!world.isClient) {
                        sync();
                    }
                }
            } else {
                currentRite.onCancelled(this);
                originalCaster = null;
                currentRite = null;
                setFlag(0, false);
                if (!world.isClient) {
                    sync();
                }
            }
            markDirty();
        }
    }

    private void handleInvestigators() {
        PlayerEntity caster = getOriginalCaster();
        if (caster != null && !world.isClient && world.random.nextFloat() < currentRite.getInvestigatorChance()) {
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
                subtlety += MaskTrinketItem.getMask(caster).isEmpty() ? 0 : 0.15F;
                for (ItemStack armor : caster.getArmorItems()) {
                    if (armor.getItem() instanceof CultistArmor) {
                        subtlety += 0.1F;
                    }
                }
                subtlety = Math.min(subtlety, 0.8F);
            } else {
                subtlety = 0.25F;
            }
            if (world.random.nextFloat() > subtlety) {
                ProtagonistHandler.spawnProtagonist((ServerWorld) world, caster);
            }
        }
    }

    public PlayerEntity getOriginalCaster() {
        return world != null && originalCaster != null ? world.getPlayerByUuid(originalCaster) : null;
    }

    public void setOriginalCaster(PlayerEntity player) {
        if (currentRite == null || originalCaster == null) {
            this.originalCaster = player.getUuid();
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

    @Override
    public void clear() {
        for (int i = 0; i < size(); i++) {
            if (getStack(i).getItem().hasRecipeRemainder()) {
                setStack(i, new ItemStack(getStack(i).getItem().getRecipeRemainder()));
                continue;
            }
            if (!getStack(i).isEmpty() && getStack(i).getItem().isIn(Constants.Tags.RITE_TOOLS)) {
                if (getStack(i).getItem() instanceof IncantationYogItem) {
                    IncantationYogItem.clear(getStack(i));
                }
                continue;
            }
            setStack(i, ItemStack.EMPTY);
        }
    }

    public Vec3d getSummoningPos() {
        return new Vec3d(pos.getX() + 0.5F, pos.getY() + 0.25F, pos.getZ() + 0.5F);
    }

    public Box getSelectionBox() {
        return new Box(pos.add(-1, -1, -1), pos.add(2, 2, 2));
    }

    public BlockPos getBoundPos() {
        return boundPos != null && World.isValid(boundPos.getSecond()) ? boundPos.getSecond() : null;
    }

    public ServerWorld getBoundDimension() {
        return boundPos != null && !world.isClient ? world.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, boundPos.getFirst())) : null;
    }

    public void bind(World world, BlockPos pos) {
        boundPos = new Pair<>(world.getRegistryKey().getValue(), pos);
    }

    public boolean doesCasterHaveKnowledge(String knowledge) {
        if (knowledge.isEmpty()) {
            return true;
        }
        return MiskatonicMysteriesAPI.hasKnowledge(knowledge, getOriginalCaster());
    }
}
