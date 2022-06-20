package com.miskatonicmysteries.mixin.entity;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.Knowledge;
import com.miskatonicmysteries.api.interfaces.MalleableAffiliated;
import com.miskatonicmysteries.api.interfaces.Resonating;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.api.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.MMMidnightLibConfig;
import com.miskatonicmysteries.common.MMServerEvents;
import com.miskatonicmysteries.common.component.AscendantComponent;
import com.miskatonicmysteries.common.feature.entity.TindalosHoundEntity;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.ExpandSanityPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.RemoveExpansionPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncKnowledgePacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.toast.SpellEffectToastPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.toast.SpellMediumToastPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMComponents;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.NbtUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.miskatonicmysteries.common.util.Constants.DataTrackers.AFFILIATION;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.APPARENT_AFFILIATION;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.MAX_SPELLS;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.POWER_POOL;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.RESONANCE;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.SANITY;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.SANITY_CAP;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.SHOCKED;
import static com.miskatonicmysteries.common.util.Constants.DataTrackers.SPELL_COOLDOWN;
import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Sanity, MalleableAffiliated, SpellCaster, Resonating, Knowledge, Ascendant {

	public final Map<String, Integer> sanityCapOverrides = new ConcurrentHashMap<>();
	@Unique
	private final List<Spell> spells = new ArrayList<>();
	@Unique
	private final Set<SpellEffect> learnedEffects = new HashSet<>();
	@Unique
	private final Set<SpellMedium> learnedMediums = new HashSet<>();
	@Unique
	private final List<String> knowledge = new ArrayList<>();
	private AscendantComponent ascendantComponent;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
		ascendantComponent = MMComponents.ASCENDANT_COMPONENT.get(this);
	}

	@Inject(method = "wakeUp(ZZ)V", at = @At("HEAD"))
	private void wakeUp(boolean bl, boolean updateSleepingPlayers, CallbackInfo ci) {
		if (canResetTimeBySleeping() && !world.isClient
			&& world.random.nextFloat() < MMMidnightLibConfig.statueEffectChance) {
			Iterable<BlockPos> positions = BlockPos.iterateOutwards(getBlockPos(), 10, 10, 10);
			for (BlockPos position : positions) {
				if (world.getBlockState(position).getBlock() instanceof StatueBlock) {
					((StatueBlock) world.getBlockState(position).getBlock()).selectStatusEffects(this, this);
					break;
				}
			}
		}
	}

	@Shadow
	public abstract boolean canResetTimeBySleeping();

	@Inject(method = "tick()V", at = @At("TAIL"))
	private void handleMiskStats(CallbackInfo info) {
		TindalosHoundEntity.handleSpawning((PlayerEntity) (Object) this);
		if (getResonance() > 0) {
			if (getSanity() < 750) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0, true, true, false));
				if (getSanity() < 500) {
					if (!world.isClient && getResonance() > 0.4F && age % 400 == 0) {
						addExperienceLevels(random.nextInt(10));
					}
				}
			}
			setResonance(getResonance() - 0.01F);
		}
		if (getSpellCooldown() > 0) {
			setSpellCooldown(getSpellCooldown() - 1);
		}
		if (age % MMMidnightLibConfig.modUpdateInterval == 0) {
			if (isShocked() && random.nextFloat() < MMMidnightLibConfig.shockRemoveChance) {
				setShocked(false);
			}
		}
		if (!world.isClient && age > 100 && age % MMMidnightLibConfig.insanityInterval == 0) {
			InsanityHandler.handleInsanityEvents((PlayerEntity) (Object) this);
		}
	}

	@Shadow
	public abstract void addExperienceLevels(int levels);

	@Override
	public int getSanity() {
		return dataTracker.get(SANITY);
	}

	@Override
	public void setSanity(int sanity, boolean ignoreFactors) {
		if (ignoreFactors || (!isShocked() && !hasStatusEffect(MMStatusEffects.TRANQUILIZED))) {
			dataTracker.set(SANITY, MathHelper.clamp(sanity, 0, getMaxSanity()));
		}
	}

	@Override
	public boolean isShocked() {
		return dataTracker.get(SHOCKED);
	}

	@Override
	public void setShocked(boolean shocked) {
		dataTracker.set(SHOCKED, shocked);
	}

	@Override
	public void addSanityCapExpansion(String name, int amount) {
		if ((PlayerEntity) (Object) this instanceof ServerPlayerEntity) {
			sanityCapOverrides.putIfAbsent(name, amount);
			ExpandSanityPacket.send((ServerPlayerEntity) (Object) this, name, amount);
			if (getSanity() > getMaxSanity()) {
				setSanity(getMaxSanity(), true);
			}
		}
	}

	@Override
	public void removeSanityCapExpansion(String name) {
		if (!world.isClient && sanityCapOverrides.containsKey(name)) {
			sanityCapOverrides.remove(name);
			RemoveExpansionPacket.send((PlayerEntity) (Object) this, name);
		}
	}

	@Override
	public Map<String, Integer> getSanityCapExpansions() {
		return sanityCapOverrides;
	}

	@Override
	public void syncSanityData() {
		if (!world.isClient) {
			sanityCapOverrides.forEach((s, i) -> ExpandSanityPacket.send((PlayerEntity) (Object) this, s, i));
		}
	}

	@Override
	public int getMaxSanity() {
		int mod = 0;
		for (Integer value : getSanityCapExpansions().values()) {
			mod += value;
		}
		return Constants.DataTrackers.SANITY_CAP + mod;
	}

	@Override
	public float getResonance() {
		return dataTracker.get(RESONANCE);
	}

	@Override
	public void setResonance(float resonance) {
		this.dataTracker.set(RESONANCE, resonance);
	}

	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	private void manipulateDamage(DamageSource source, float amount,
								  CallbackInfoReturnable<Boolean> infoReturnable) {
		MMServerEvents.playerDamagePre((PlayerEntity) (Object) this, source, amount, infoReturnable);
	}

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("RETURN"), cancellable = true)
	private void manipulateDeath(DamageSource source, float amount, CallbackInfoReturnable<Boolean> infoReturnable) {
		this.dead = MMServerEvents.playerDamageDeath((PlayerEntity) (Object) this, source, amount, infoReturnable);
	}

	@Inject(method = "initDataTracker()V", at = @At("TAIL"))
	private void addMiskStats(CallbackInfo info) {
		dataTracker.startTracking(SANITY, SANITY_CAP);
		dataTracker.startTracking(SHOCKED, false);
		dataTracker.startTracking(POWER_POOL, 2);
		dataTracker.startTracking(SPELL_COOLDOWN, 0);
		dataTracker.startTracking(MAX_SPELLS, Constants.DataTrackers.MIN_SPELLS);
		dataTracker.startTracking(AFFILIATION, MMAffiliations.NONE);
		dataTracker.startTracking(APPARENT_AFFILIATION, MMAffiliations.NONE);
		dataTracker.startTracking(RESONANCE, 0F);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeMiskData(NbtCompound compoundTag, CallbackInfo info) {
		NbtCompound tag = new NbtCompound();

		tag.putInt(Constants.NBT.SANITY, getSanity());
		tag.putBoolean(Constants.NBT.SHOCKED, isShocked());
		NbtList expansions = new NbtList();
		getSanityCapExpansions().forEach((s, i) -> {
			NbtCompound expansionTag = new NbtCompound();
			expansionTag.putString("Name", s);
			expansionTag.putInt("Amount", i);
			expansions.add(expansionTag);
		});
		tag.put(Constants.NBT.SANITY_EXPANSIONS, expansions);

		tag.putInt(Constants.NBT.POWER_POOL, getPowerPool());
		tag.putInt(Constants.NBT.MAX_SPELLS, getMaxSpells());
		tag.putInt(Constants.NBT.SPELL_COOLDOWN, getSpellCooldown());
		NbtUtil.writeSpellData(this, tag);
		tag.putString(Constants.NBT.AFFILIATION, getAffiliation(false).getId().toString());
		tag.putString(Constants.NBT.APPARENT_AFFILIATION, getAffiliation(true).getId().toString());

		tag.putFloat(Constants.NBT.RESONANCE, getResonance());
		NbtList knowledgeList = new NbtList();
		for (String knowledgeId : knowledge) {
			knowledgeList.add(NbtString.of(knowledgeId));
		}
		tag.put(Constants.NBT.KNOWLEDGE, knowledgeList);

		compoundTag.put(Constants.NBT.MISK_DATA, tag);
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return dataTracker.get(apparent ? APPARENT_AFFILIATION : AFFILIATION);
	}

	@Override
	public boolean isSupernatural() {
		return false;
	}

	@Override
	public int getPowerPool() {
		return dataTracker.get(POWER_POOL);
	}

	@Override
	public void setPowerPool(int amount) {
		dataTracker.set(POWER_POOL, amount);
	}

	@Override
	public int getMaxSpells() {
		return dataTracker.get(MAX_SPELLS);
	}

	@Override
	public void setMaxSpells(int amount) {
		dataTracker.set(MAX_SPELLS, amount);
	}

	@Override
	public List<Spell> getSpells() {
		return spells;
	}

	@Override
	public Set<SpellEffect> getLearnedEffects() {
		return learnedEffects;
	}

	@Override
	public void learnEffect(SpellEffect effect) {
		if ((Object) this instanceof ServerPlayerEntity p && !learnedEffects.contains(effect)) {
			SpellEffectToastPacket.send(p, effect);
		}
		learnedEffects.add(effect);
	}

	@Override
	public Set<SpellMedium> getLearnedMediums() {
		return learnedMediums;
	}

	@Override
	public void learnMedium(SpellMedium medium) {
		if ((Object) this instanceof ServerPlayerEntity p && !learnedMediums.contains(medium)) {
			SpellMediumToastPacket.send(p, medium);
		}
		learnedMediums.add(medium);
	}

	@Override
	public int getSpellCooldown() {
		return dataTracker.get(SPELL_COOLDOWN);
	}

	@Override
	public void setSpellCooldown(int ticks) {
		this.dataTracker.set(SPELL_COOLDOWN, ticks);
	}

	@Override
	public void syncSpellData() {
		if (!world.isClient) {
			SyncSpellCasterDataPacket.send(false, (PlayerEntity) (Object) this, this);
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readMiskData(NbtCompound compoundTag, CallbackInfo info) {
		NbtCompound tag = (NbtCompound) compoundTag.get(Constants.NBT.MISK_DATA);
		if (tag != null) {
			setSanity(tag.getInt(Constants.NBT.SANITY), true);
			setShocked(tag.getBoolean(Constants.NBT.SHOCKED));
			getSanityCapExpansions().clear();
			((NbtList) tag.get(Constants.NBT.SANITY_EXPANSIONS))
				.forEach(s -> sanityCapOverrides.put(((NbtCompound) s).getString("Name"), ((NbtCompound) s).getInt("Amount")));
			setPowerPool(tag.getInt(Constants.NBT.POWER_POOL));
			setMaxSpells(tag.getInt(Constants.NBT.MAX_SPELLS));
			setSpellCooldown(tag.getInt(Constants.NBT.SPELL_COOLDOWN));
			NbtUtil.readSpellData(this, tag);

			setAffiliation(MMRegistries.AFFILIATIONS.get(new Identifier(tag.getString(Constants.NBT.AFFILIATION))),
						   false);
			setAffiliation(MMRegistries.AFFILIATIONS.get(new Identifier(tag.getString(Constants.NBT.APPARENT_AFFILIATION))), true);

			setResonance(tag.getFloat(Constants.NBT.RESONANCE));

			NbtList knowledgeList = tag.getList(Constants.NBT.KNOWLEDGE, 8);
			knowledge.clear();
			for (NbtElement knowledgeTag : knowledgeList) {
				knowledge.add(knowledgeTag.asString());
			}

		}
	}

	@Override
	public void setAffiliation(Affiliation affiliation, boolean apparent) {
		dataTracker.set(apparent ? APPARENT_AFFILIATION : AFFILIATION, affiliation);
	}

	@Override
	public void addKnowledge(String knowledge) {
		this.knowledge.add(knowledge);
	}

	@Override
	public boolean hasKnowledge(String knowledge) {
		return this.knowledge.contains(knowledge);
	}

	@Override
	public void clearKnowledge() {
		knowledge.clear();
	}

	@Override
	public void syncKnowledge() {
		if (!world.isClient) {
			SyncKnowledgePacket.send(this, this);
		}
	}

	@Override
	public List<String> getKnowledge() {
		return knowledge;
	}

	@Environment(EnvType.CLIENT)
	@Inject(method = "shouldRenderName", at = @At("HEAD"), cancellable = true)
	private void shouldRenderName(CallbackInfoReturnable<Boolean> cir) {
		if (MMMidnightLibConfig.masksConcealNameplates && !MaskTrinketItem.getMask((PlayerEntity) (Object) this).isEmpty()) {
			cir.setReturnValue(false);
		}
	}

	@Override
	public void addBlessing(Blessing blessing) {
		ascendantComponent.addBlessing(blessing);
	}

	@Override
	public boolean removeBlessing(Blessing blessing) {
		return ascendantComponent.removeBlessing(blessing);
	}

	@Override
	public List<Blessing> getBlessings() {
		return ascendantComponent.getBlessings();
	}

	@Override
	public int getAscensionStage() {
		return ascendantComponent.getAscensionStage();
	}

	@Override
	public void setAscensionStage(int level) {
		ascendantComponent.setAscensionStage(level);
	}
}