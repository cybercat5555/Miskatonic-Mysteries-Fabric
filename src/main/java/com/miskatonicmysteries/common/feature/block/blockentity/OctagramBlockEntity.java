package com.miskatonicmysteries.common.feature.block.blockentity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.feature.item.IncantationYogItem;
import com.miskatonicmysteries.common.feature.recipe.instability_event.InstabilityEvent;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMCriteria;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.datafixers.util.Pair;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

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
	private float instability;
	/**
	 * Octagram flags may be used by Rites Reserved flags: 0 - Bloody (has something been sacrificed nearby?)
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
				if (!blockEntity.world.isClient && blockEntity.handleInstabilityEvents()) {
					blockEntity.currentRite.onCancelled(blockEntity);
					blockEntity.closeRite(false);
					return;
				}
				if (blockEntity.currentRite.isFinished(blockEntity)) {
					if (blockEntity.getOriginalCaster() instanceof ServerPlayerEntity) {
						MMCriteria.RITE_CAST.trigger((ServerPlayerEntity) blockEntity.getOriginalCaster(),
							blockEntity.currentRite);
					}
					blockEntity.handleInvestigators();
					blockEntity.permanentRiteActive = blockEntity.currentRite.isPermanent(blockEntity);
					blockEntity.currentRite.onFinished(blockEntity);
					if (!blockEntity.world.isClient) {
						if (!blockEntity.permanentRiteActive) {
							blockEntity.closeRite(true);
						} else {
							blockEntity.sync(blockEntity.world, blockEntity.pos);
						}
					}
				}
			} else {
				blockEntity.currentRite.onCancelled(blockEntity);
				if (!blockEntity.world.isClient) {
					blockEntity.closeRite(true);
				}
			}
			blockEntity.markDirty();
		}
	}

	private void closeRite(boolean success) {
		instability = 0;
		tickCount = 0;
		currentRite = null;
		targetedEntity = null;
		permanentRiteActive = false;
		setFlag(0, false);
		clear(success);
		sync(world, pos);
		markDirty();
	}

	private boolean handleInstabilityEvents() {
		if (tickCount % MiskatonicMysteries.config.mechanics.modUpdateInterval == 0) {
			calculateInstability();
		}
		if (tickCount % 20 == 0 && world.random.nextFloat() * (currentRite.isPermanent(this) ? 10 : 4) < instability) {
			List<InstabilityEvent> possibleEvents = MMRegistries.INSTABILITY_EVENTS.stream()
				.filter(e -> e.shouldCast(this, instability)).collect(Collectors.toList());
			if (possibleEvents.size() > 0) {
				InstabilityEvent event = possibleEvents.get(world.random.nextInt(possibleEvents.size()));
				return event.cast(this, instability);
			}
		}
		return false;
	}

	private void calculateInstability() {
		if (!world.isClient) {
			BiomeEffect biomeEffect = MiskatonicMysteriesAPI.getBiomeEffect(world, getPos());
			float instability = biomeEffect != null ? (biomeEffect
				.getAffiliation(false) == getAffiliation(false) ? 0.1F : 0.8F) : currentRite.getInstabilityBase(this);
			int stabilizerCount = 0;
			Set<Block> strongStabilizerCache = new HashSet<>();
			for (BlockPos blockPos : BlockPos.iterateOutwards(getPos(), 5, 3, 5)) {
				BlockState state = world.getBlockState(blockPos);
				Block block = state.getBlock();
				if (Constants.Tags.STABILIZERS.contains(block)) {
					if (block instanceof CandleBlock && !state.get(CandleBlock.LIT)) {
						continue;
					}
					float strength = Constants.Tags.STRONG_STABILIZERS
						.contains(block) && strongStabilizerCache
						.add(block) ? 0.05F : Constants.Tags.WEAK_STABILIZERS
						.contains(block) ? 0.025F : 0.03F;

					if (block instanceof Affiliated a && a.getAffiliation(false) != MMAffiliations.NONE && a
						.getAffiliation(false) != getAffiliation(false)) {
						instability += strength * 2;
					} else if (stabilizerCount < MiskatonicMysteries.config.mechanics.maxStabilizers) {
						stabilizerCount++;
						instability -= strength;
					}
				}
			}
			this.instability = MathHelper.clamp(instability, 0.0F, 0.9F);
			sync(world, pos);
		}
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
	public void writeNbt(NbtCompound tag) {
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
		tag.putFloat(Constants.NBT.INSTABILITY, instability);
	}

	@Override
	public void readNbt(NbtCompound tag) {
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
			boundPos = new Pair<>(new Identifier(tag.getString(Constants.NBT.DIMENSION)),
				BlockPos.fromLong(tag.getLong(Constants.NBT.POSITION)));
		} else {
			boundPos = null;
		}
		triggered = tag.getBoolean(Constants.NBT.TRIGGERED);
		octagramFlags = tag.getByte(Constants.NBT.FLAGS);
		this.instability = tag.getFloat(Constants.NBT.INSTABILITY);
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
					} else if (Constants.Tags.SUSPICIOUS_BLOCKS.contains(block)) {
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
		return getCachedState().getBlock() instanceof OctagramBlock o ? o.getAffiliation(true) : null;
	}

	@Override
	public boolean isSupernatural() {
		return getCachedState().getBlock() instanceof OctagramBlock o && o.isSupernatural();
	}

	@Override
	public void clear() {
		clear(true);
	}

	private void clear(boolean success) {
		for (int i = 0; i < size(); i++) {
			if (!success && (world.random.nextFloat() + 0.15F < instability)) {
				continue;
			}
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
		return 4;
	}

	@Override
	public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
		if (currentRite != null) {
			if (!currentRite.listen(this, world, event, entity, pos)) {
				if (!world.isClient && event == GameEvent.ENTITY_KILLED && entity != null && entity.getType()
					.isIn(Constants.Tags.VALID_SACRIFICES)) {
					setFlag(0, true);
					markDirty();
					sync(world, pos);
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public void sync(World world, BlockPos pos) {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}
}
