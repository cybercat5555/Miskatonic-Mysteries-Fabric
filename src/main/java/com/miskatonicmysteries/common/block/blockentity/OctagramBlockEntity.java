package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class OctagramBlockEntity extends BaseBlockEntity implements ImplementedBlockEntityInventory, Affiliated,
		GameEventListener {
	private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(8, ItemStack.EMPTY);
	private final PositionSource positionSource;
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

	public OctagramBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.OCTAGRAM_BLOCK_ENTITY_TYPE, pos, state);
		positionSource = new PositionSource() {
			@Override
			public Optional<BlockPos> getPos(World world) {
				return Optional.of(pos);
			}

			@Override
			public PositionSourceType<?> getType() {
				return PositionSourceType.BLOCK;
			}
		};
	}

	public static void tick(OctagramBlockEntity blockEntity) {
		if (blockEntity.currentRite != null) {
			if (blockEntity.currentRite.shouldContinue(blockEntity)) {
				blockEntity.currentRite.tick(blockEntity);
				if (blockEntity.currentRite.isFinished(blockEntity)) {

					if (blockEntity.getOriginalCaster() instanceof ServerPlayerEntity) {
						MMCriteria.RITE_CAST.trigger((ServerPlayerEntity) blockEntity.getOriginalCaster(),
								blockEntity.currentRite);
					}
					blockEntity.handleInvestigators();
					if (blockEntity.currentRite.isPermanent(blockEntity)) {
						blockEntity.permanentRiteActive = true;
						blockEntity.currentRite.onFinished(blockEntity);
					}
					else {
						blockEntity.currentRite.onFinished(blockEntity);
						blockEntity.tickCount = 0;
						blockEntity.currentRite = null;
						blockEntity.targetedEntity = null;
						blockEntity.setFlag(0, false);
					}
					if (!blockEntity.world.isClient) {
						blockEntity.sync();
					}
				}
			}
			else {
				blockEntity.currentRite.onCancelled(blockEntity);
				blockEntity.originalCaster = null;
				blockEntity.currentRite = null;
				blockEntity.setFlag(0, false);
				if (!blockEntity.world.isClient) {
					blockEntity.sync();
				}
			}
			blockEntity.markDirty();
		}
	}

	public void setFlag(int index, boolean value) {
		if (value) {
			octagramFlags |= 1 << index;
		}
		else {
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
	public void readNbt(NbtCompound tag) {
		ITEMS.clear();
		Inventories.readNbt(tag, ITEMS);
		tickCount = tag.getInt(Constants.NBT.TICK_COUNT);
		if (tag.contains(Constants.NBT.RITE)) {
			currentRite = MMRegistries.RITES.get(new Identifier(tag.getString(Constants.NBT.RITE)));
		}
		else {
			currentRite = null;
		}
		permanentRiteActive = tag.getBoolean(Constants.NBT.PERMANENT_RITE);
		if (tag.contains(Constants.NBT.PLAYER_UUID)) {
			originalCaster = tag.getUuid(Constants.NBT.PLAYER_UUID);
		}
		else {
			originalCaster = null;
		}
		if (tag.contains(Constants.NBT.DIMENSION)) {
			boundPos = new Pair<>(new Identifier(tag.getString(Constants.NBT.DIMENSION)),
					BlockPos.fromLong(tag.getLong(Constants.NBT.POSITION)));
		}
		else {
			boundPos = null;
		}
		triggered = tag.getBoolean(Constants.NBT.TRIGGERED);
		octagramFlags = tag.getByte(Constants.NBT.FLAGS);
		super.readNbt(tag);
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

	private void handleInvestigators() {
		PlayerEntity caster = getOriginalCaster();
		if (caster != null && !world.isClient && world.random.nextFloat() < currentRite.getInvestigatorChance()) {
			float subtlety = 0;
			if (MiskatonicMysteries.config.entities.subtlety) {
				for (BlockPos blockPos : BlockPos.iterateOutwards(pos, 8, 8, 8)) {
					Block block = world.getBlockState(blockPos).getBlock();
					if (Constants.Tags.SUBTLE_BLOCKS.contains(block)) {
						subtlety += 0.05F;
					}
					else if (Constants.Tags.SUSPICIOUS_BLOCKS.contains(block)) {
						subtlety -= 0.05F;
					}
					if (subtlety >= 0.35F) {
						break;
					}
				}
				subtlety += world.isNight() ? 0.15F : -0.1;
				subtlety += MaskTrinketItem.getMask(caster).isEmpty() ? 0 : 0.15F;
				for (ItemStack armor : caster.getArmorItems()) {
					if (Constants.Tags.CULTIST_ARMOR.contains(armor.getItem())) {
						subtlety += 0.1F;
					}
				}
				subtlety = Math.min(subtlety, 0.8F);
			}
			else {
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
		return getCachedState().getBlock() instanceof OctagramBlock o ? o.getAffiliation(true) : null;
	}

	@Override
	public boolean isSupernatural() {
		return getCachedState().getBlock() instanceof OctagramBlock o && o.isSupernatural();
	}

	@Override
	public void clear() {
		for (int i = 0; i < size(); i++) {
			if (getStack(i).getItem().hasRecipeRemainder()) {
				setStack(i, new ItemStack(getStack(i).getItem().getRecipeRemainder()));
				continue;
			}
			if (!getStack(i).isEmpty() && Constants.Tags.RITE_TOOLS.contains(getStack(i).getItem())) {
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
		return boundPos != null && !world.isClient ? world.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY,
				boundPos.getFirst())) : null;
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

	@Override
	public PositionSource getPositionSource() {
		return positionSource;
	}

	@Override
	public int getRange() {
		return 6;
	}

	@Override
	public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
		if (currentRite != null) {
			if (!currentRite.listen(this, world, event, entity, pos)) {
				if (!world.isClient && event == GameEvent.ENTITY_KILLED && entity != null && entity.getType().isIn(Constants.Tags.VALID_SACRIFICES)) {
					setFlag(1, true);
					markDirty();
					sync();
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}
}
