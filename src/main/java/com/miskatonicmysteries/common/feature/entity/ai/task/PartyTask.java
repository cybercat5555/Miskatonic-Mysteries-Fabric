package com.miskatonicmysteries.common.feature.entity.ai.task;

import com.miskatonicmysteries.common.feature.world.party.MMPartyState;
import com.miskatonicmysteries.common.feature.world.party.Party;

import net.minecraft.entity.ai.brain.task.SeekSkyTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import net.minecraft.util.math.random.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

public class PartyTask extends Task<VillagerEntity> {

	private static final DyeColor[] fireworkColors = new DyeColor[]{DyeColor.YELLOW, DyeColor.YELLOW, DyeColor.RED, DyeColor.ORANGE};
	@Nullable
	private Party party;

	public PartyTask() {
		super(ImmutableMap.of(), 100, 400);
	}

	protected void keepRunning(ServerWorld serverWorld, VillagerEntity villagerEntity, long l) {
		Random random = villagerEntity.getRandom();
		float partyPower = party.getPercentualPartyPower();
		if (random.nextFloat() * 100 <= partyPower) {
			if (random.nextBoolean() && random.nextBoolean()) {
				villagerEntity.playCelebrateSound();
			}
			villagerEntity.getJumpControl().setActive();
		}

		if (random.nextFloat() * 400 <= party.getPercentualPartyPower() && SeekSkyTask.isSkyVisible(serverWorld,
																									villagerEntity, villagerEntity.getBlockPos())) {
			DyeColor dyeColor = Util.getRandom(fireworkColors, random);
			DyeColor dyeColor2 = Util.getRandom(fireworkColors, random);
			int i = 1 + random.nextInt(2);
			ItemStack itemStack = this.createFirework(dyeColor, dyeColor2, i, random.nextBoolean() ?
																			  FireworkRocketItem.Type.BURST
																								   : (random.nextFloat() <= partyPower - 0.5F) ?
																									 FireworkRocketItem.Type.LARGE_BALL
																																			   : FireworkRocketItem.Type.SMALL_BALL);
			FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(villagerEntity.world, villagerEntity,
																				 villagerEntity.getX(), villagerEntity.getEyeY(),
																				 villagerEntity.getZ(), itemStack);
			villagerEntity.world.spawnEntity(fireworkRocketEntity);
		}

	}

	protected void finishRunning(ServerWorld serverWorld, VillagerEntity villagerEntity, long l) {
		this.party = null;
		villagerEntity.getBrain().refreshActivities(serverWorld.getTimeOfDay(), serverWorld.getTime());
	}

	protected boolean shouldKeepRunning(ServerWorld serverWorld, VillagerEntity villagerEntity, long l) {
		return this.party != null && !party.shouldStop();
	}

	protected boolean shouldRun(ServerWorld serverWorld, VillagerEntity villagerEntity) {
		BlockPos blockPos = villagerEntity.getBlockPos();
		this.party = MMPartyState.get(serverWorld).getParty(blockPos);
		return this.party != null;
	}

	private ItemStack createFirework(DyeColor color, DyeColor color2, int flight, FireworkRocketItem.Type type) {
		ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET, 1);
		ItemStack itemStack2 = new ItemStack(Items.FIREWORK_STAR);
		NbtCompound nbtCompound = itemStack2.getOrCreateSubNbt("Explosion");
		List<Integer> list = Lists.newArrayList();
		list.add(color.getFireworkColor());
		nbtCompound.putIntArray("Colors", list);
		nbtCompound.putByte("Type", (byte) type.getId());
		NbtCompound nbtCompound2 = itemStack.getOrCreateSubNbt("Fireworks");
		NbtList nbtList = new NbtList();
		NbtCompound nbtCompound3 = itemStack2.getSubNbt("Explosion");
		if (nbtCompound3 != null) {
			nbtList.add(nbtCompound3);
		}

		nbtCompound2.putByte("Flight", (byte) flight);
		if (!nbtList.isEmpty()) {
			nbtCompound2.put("Explosions", nbtList);
		}

		return itemStack;
	}
}