package com.miskatonicmysteries.common.feature.block.blockentity;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.authlib.GameProfile;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MasterpieceStatueBlockEntity extends BaseBlockEntity implements Affiliated {

	public UUID creator = null;
	public String creatorName;
	public int pose = 0;
	@Nullable
	private GameProfile statueOwner;

	public MasterpieceStatueBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.MASTERPIECE_STATUE_BLOCK_ENTITY_TYPE, pos, state);
	}

	public static int selectRandomPose(Random random) {
		if (random.nextFloat() < 0.05F) {
			return 69; //the funny
		} else {
			return random.nextInt(3);
		}
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		if (creator != null) {
			tag.putUuid(Constants.NBT.PLAYER_UUID, creator);
		}
		if (statueOwner != null) {
			NbtCompound nbtCompound = new NbtCompound();
			NbtHelper.writeGameProfile(nbtCompound, statueOwner);
			tag.put(Constants.NBT.STATUE_OWNER, nbtCompound);
		}

		if (creatorName != null) {
			tag.putString(Constants.NBT.PLAYER_NAME, creatorName);
		}
		tag.putInt(Constants.NBT.POSE, pose);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);

		if (tag.contains(Constants.NBT.PLAYER_UUID)) {
			creator = tag.getUuid(Constants.NBT.PLAYER_UUID);
		}
		if (tag.contains(Constants.NBT.STATUE_OWNER, 10)) {
			this.setStatueProfile(NbtHelper.toGameProfile(tag.getCompound(Constants.NBT.STATUE_OWNER)));
		} else if (tag.contains("ExtraType", 8)) {
			String string = tag.getString("ExtraType");
			if (!StringHelper.isEmpty(string)) {
				this.setStatueProfile(new GameProfile(null, string));
			}
		}
		if (tag.contains(Constants.NBT.PLAYER_NAME)) {
			creatorName = tag.getString(Constants.NBT.PLAYER_NAME);
		}
		pose = tag.getInt(Constants.NBT.POSE);
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return getCachedState().getBlock() instanceof StatueBlock statue ? statue.getAffiliation(apparent) : MMAffiliations.NONE;
	}

	@Override
	public boolean isSupernatural() {
		return getCachedState().getBlock() instanceof StatueBlock statue && statue.isSupernatural();
	}

	public void setCreator(@Nullable PlayerEntity player) {
		this.creator = player == null ? null : player.getUuid();
		this.creatorName = player == null ? "" : player.getDisplayName().asString();
	}

	private void loadOwnerProperties() {
		SkullBlockEntity.loadProperties(this.statueOwner, (owner) -> {
			this.statueOwner = owner;
			this.markDirty();
		});
	}

	public GameProfile getStatueProfile() {
		return statueOwner;
	}

	public void setStatueProfile(@Nullable GameProfile profile) {
		synchronized (this) {
			this.statueOwner = profile;
		}

		this.loadOwnerProperties();

		if (world instanceof ServerWorld) {
			sync(world, pos);
		}
	}

	public void sync(World world, BlockPos pos) {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}

}
