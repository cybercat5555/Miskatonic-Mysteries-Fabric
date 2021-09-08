package com.miskatonicmysteries.common.feature.world.party;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.feature.entity.ai.task.PartyTask;
import com.miskatonicmysteries.common.feature.entity.brain.HasturCultistBrain;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

public class Party {
	public static final int FIREWORK_BONUS = 40;
	public static final int DRUGS_BONUS = 10;
	public static final Text EVENT_TEXT = new TranslatableText("event.miskatonicmysteries.hastur_party");
	public static final Text EVENT_TEXT_CONCLUDED = new TranslatableText("event.miskatonicmysteries.hastur_party" +
			".finish");
	public static final Text EVENT_TEXT_WAITING = new TranslatableText("event.miskatonicmysteries.hastur_party" +
			".waiting");
	public static final int PARTY_POWER_MAX = 1000;
	private final ServerWorld world;
	private final ServerBossBar bar;
	private final BlockPos centerPos;
	private final int id;
	private final Set<LivingEntity> participants = new HashSet<>();
	public Set<BlockPos> musicSources = new HashSet<>();
	private int tickCount;
	private int partyPower;
	private int bonusCooldown;
	private Status status;


	public Party(ServerWorld world, BlockPos center, int id) {
		this.world = world;
		this.bar = new ServerBossBar(EVENT_TEXT, BossBar.Color.YELLOW, BossBar.Style.PROGRESS);
		this.centerPos = center;
		this.id = id;
		this.status = Status.WAITING;
		this.partyPower = 0;
		this.tickCount = 0;
		this.bonusCooldown = 0;
	}

	public Party(ServerWorld world, NbtCompound tag) {
		this(world, NbtHelper.toBlockPos(tag.getCompound(Constants.NBT.CENTER_POS)), tag.getInt(Constants.NBT.ID));
		this.status = Status.fromName(tag.getString(Constants.NBT.STATUS));
		this.partyPower = tag.getInt(Constants.NBT.PARTY_POWER);
		this.tickCount = tag.getInt(Constants.NBT.TICK_COUNT);
		this.bonusCooldown = tag.getInt(Constants.NBT.BONUS_COOLDOWN);
		NbtList list = tag.getList(Constants.NBT.MUSIC_SOURCES, 10);
		for (NbtElement nbtElement : list) {
			musicSources.add(NbtHelper.toBlockPos((NbtCompound) nbtElement));
		}
		updateMembers();
	}

	private static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> createPartyTasks(float speed) {
		return ImmutableList.of(Pair.of(4, new PartyTask()), Pair.of(4,
				new RandomTask<>(ImmutableList.of(Pair.of(new GoToIfNearbyTask(MemoryModuleType.MEETING_POINT, speed,
						80), 2), Pair.of(new WanderAroundTask(40, 80), 1)))), Pair.of(3,
				new FindInteractionTargetTask(EntityType.PLAYER, 4)), Pair.of(3, new MeetVillagerTask()), Pair.of(2,
				new VillagerWalkTowardsTask(MemoryModuleType.MEETING_POINT, speed, 12, 100, 200)), Pair.of(3,
				new ForgetCompletedPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT)),
				Pair.of(99, new ScheduleActivityTask()));
	}

	public void tick() {
		tickCount++;
		if (status == Status.WAITING) {
			if (world.isNight()) {
				status = Status.ONGOING;
				tickCount = 0;
			}
			if (tickCount % 20 == 0) {
				updateBarToPlayers(this.world.getPlayers(this.isInPartyDistance()));
			}
		}
		if (status == Status.ONGOING) {
			if (bonusCooldown > 0) {
				bonusCooldown--;
			}
			if (tickCount % 20 == 0) {
				if ((tickCount >= 6000 || partyPower >= PARTY_POWER_MAX)) {
					status = Status.CONCLUDED;
					tickCount = 0;
				}
				List<ServerPlayerEntity> playersAround = updateMembers();
				updatePartyPower();
				updateBarToPlayers(playersAround);

			}
		}
	}

	public void addPartyPower(int power){
		if (bonusCooldown > 0){
			return;
		}
		this.partyPower += power;
		this.bonusCooldown = 40;
	}

	private void updatePartyPower() {
		partyPower -= 12 + (18 * partyPower / PARTY_POWER_MAX);
		for (LivingEntity participant : participants) {
			float partyGain = 0;
			double speed = participant.getVelocity().length();
			if (speed > 0) {
				partyGain++;
				if (speed > 0.2F) {
					partyGain++;
				}
			}
			if (tickCount % 40 == 0 && participant.hasStatusEffect(StatusEffects.NAUSEA)) {
				partyGain *= 2.2F;
			}
			if (participant.hasStatusEffect(MMStatusEffects.MANIA) || participant.hasStatusEffect(MMStatusEffects.TRANQUILIZED)) {
				partyGain *= 1.8F;
			}
			if (!musicSources.isEmpty()) {
				partyGain *= 1.8F;
			}
			partyPower += Math.round(partyGain);
		}
		bar.setVisible(true);
		bar.setPercent(Math.min(partyPower / (float) PARTY_POWER_MAX, 1));
	}

	private void setPartyBrain(VillagerEntity villager, Brain<VillagerEntity> brain) {
		brain.setSchedule(Schedule.EMPTY);
		brain.setTaskList(Activity.CORE, VillagerTaskListProvider.createCoreTasks(VillagerProfession.NITWIT, 0.65F));
		brain.setTaskList(Activity.PANIC, VillagerTaskListProvider.createPanicTasks(VillagerProfession.NITWIT, 0.65F));
		brain.setTaskList(Activity.IDLE, createPartyTasks(0.65F));
		if (villager instanceof HasturCultistEntity cultist) {
			brain.setTaskList(Activity.FIGHT, 10, HasturCultistBrain.createFightTasks(cultist, 0.65F),
					MemoryModuleType.ATTACK_TARGET);
		}

		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.doExclusively(Activity.IDLE);
		brain.refreshActivities(this.world.getTimeOfDay(), this.world.getTime());
	}

	private void updateBarToPlayers(List<ServerPlayerEntity> playersAround) {
		Set<ServerPlayerEntity> trackedPlayers = Sets.newHashSet(this.bar.getPlayers());
		for (ServerPlayerEntity serverPlayerEntity : playersAround) {
			if (!trackedPlayers.contains(serverPlayerEntity)) {
				this.bar.addPlayer(serverPlayerEntity);
			}
		}

		for (ServerPlayerEntity serverPlayerEntity : trackedPlayers) {
			if (!playersAround.contains(serverPlayerEntity)) {
				this.bar.removePlayer(serverPlayerEntity);
			}
		}

		switch (status) {
			case ONGOING -> bar.setName(EVENT_TEXT);
			case WAITING -> bar.setName(EVENT_TEXT_WAITING);
			case CONCLUDED -> bar.setName(EVENT_TEXT_CONCLUDED);
		}
	}

	private List<ServerPlayerEntity> updateMembers() {
		List<ServerPlayerEntity> playersAround = this.world.getPlayers(this.isInPartyDistance());
		if (tickCount % 100 == 0) {
			for (LivingEntity participant : participants) {
				if (participant instanceof VillagerEntity v) {
					v.reinitializeBrain(world);
				}
			}
			participants.clear();
			participants.addAll(playersAround);
			participants.addAll(world.getEntitiesByType(TypeFilter.instanceOf(VillagerEntity.class),
					isInPartyDistance()));
			for (LivingEntity participant : participants) {
				if (participant instanceof VillagerEntity v) {
					setPartyBrain(v, v.getBrain());
					v.setJumping(true);
				}
			}
		}
		return playersAround;
	}

	private Predicate<? super LivingEntity> isInPartyDistance() {
		return (entity) -> entity.isAlive() && MMPartyState.get(world).getParty(entity.getBlockPos()) == this;
	}

	public void writeNbt(NbtCompound tag) {
		tag.putInt(Constants.NBT.ID, id);
		tag.put(Constants.NBT.CENTER_POS, NbtHelper.fromBlockPos(centerPos));
		tag.putString(Constants.NBT.STATUS, status.getName());
		tag.putInt(Constants.NBT.PARTY_POWER, partyPower);
		tag.putInt(Constants.NBT.TICK_COUNT, tickCount);
		tag.putInt(Constants.NBT.BONUS_COOLDOWN, bonusCooldown);
	}

	public int getId() {
		return id;
	}

	public boolean shouldStop() {
		return (status == Status.ONGOING && tickCount > 400 && partyPower <= 0) || (status == Status.CONCLUDED && tickCount > 200);
	}

	public void concludeParty() {
		bar.setVisible(false);
		bar.clearPlayers();
		List<ServerPlayerEntity> playersAround = updateMembers();
		for (LivingEntity participant : participants) {
			if (participant instanceof VillagerEntity v) {
				v.reinitializeBrain(world);
				for (ServerPlayerEntity serverPlayerEntity : playersAround) {
					if (partyPower > 300 && MiskatonicMysteriesAPI.getNonNullAffiliation(serverPlayerEntity, true) == MMAffiliations.HASTUR) {
						v.getGossip().startGossip(serverPlayerEntity.getUuid(), VillageGossipType.MAJOR_POSITIVE, world.random.nextInt(partyPower / 20));
					}
					v.getGossip().startGossip(serverPlayerEntity.getUuid(), VillageGossipType.MINOR_POSITIVE, partyPower / 20 - 2);
				}
			}
		}
		if (partyPower > 500) {
			int effectDuration = Math.round(partyPower * 24);
			for (ServerPlayerEntity serverPlayerEntity : playersAround) {
				if (MiskatonicMysteriesAPI.getNonNullAffiliation(serverPlayerEntity, false) == MMAffiliations.HASTUR) {
					serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, effectDuration));
					serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, effectDuration));
					HasturAscensionHandler.holdGoldenGathering(serverPlayerEntity, partyPower);
				}
			}
		}
	}


	public BlockPos getCenter() {
		return centerPos;
	}

	public float getPercentualPartyPower() {
		return partyPower / (float) PARTY_POWER_MAX;
	}

	enum Status {
		WAITING, ONGOING, CONCLUDED;

		static Status fromName(String name) {
			for (Status status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
			return ONGOING;
		}

		public String getName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
