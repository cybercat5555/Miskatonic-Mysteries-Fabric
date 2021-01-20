package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.effect.LazarusStatusEffect;
import com.miskatonicmysteries.common.feature.sanity.Sanity;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellCaster;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.ExpandSanityPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.RemoveExpansionPacket;
import com.miskatonicmysteries.common.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMMiscRegistries;
import com.miskatonicmysteries.common.lib.MMObjects;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import com.miskatonicmysteries.common.lib.util.InventoryUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.miskatonicmysteries.common.lib.Constants.DataTrackers.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements Sanity, Affiliated, SpellCaster {
    public final Map<String, Integer> sanityCapOverrides = new ConcurrentHashMap<>();

    private final List<Spell> spells = new ArrayList<>();
    private final Set<SpellEffect> learnedEffects = new HashSet<>();
    private final Map<SpellMedium, Integer> availableMediums = new HashMap<>();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void handleMiskStats(CallbackInfo info) {
        if (age % MiskatonicMysteries.config.modUpdateInterval == 0) {
            if (isShocked() && random.nextFloat() < MiskatonicMysteries.config.sanity.shockRemoveChance)
                setShocked(false);
        }
        if (!world.isClient && age % MiskatonicMysteries.config.sanity.insanityInterval == 0) {
            InsanityHandler.handleInsanityEvents((PlayerEntity) (Object) this);
        }
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"))
    private void manipulateProtagonistDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> infoReturnable) {
        if (source.getAttacker() instanceof ProtagonistEntity && !(source instanceof Constants.DamageSources.ProtagonistDamageSource))
            ((PlayerEntity) (Object) this).damage(new Constants.DamageSources.ProtagonistDamageSource(source.getAttacker()), amount);
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("RETURN"), cancellable = true)
    private void manipulateDeath(DamageSource source, float amount, CallbackInfoReturnable<Boolean> infoReturnable) {
        if (amount >= getHealth() && !source.isOutOfWorld()) {
            PlayerEntity entity = (PlayerEntity) (Object) this;
            if (InventoryUtil.getSlotForItemInHotbar(entity, MMObjects.RE_AGENT_SYRINGE) >= 0) {
                entity.inventory.removeStack(InventoryUtil.getSlotForItemInHotbar(entity, MMObjects.RE_AGENT_SYRINGE), 1);
                if (LazarusStatusEffect.revive(entity)) {
                    dead = false;
                    removed = false;
                    infoReturnable.setReturnValue(false);
                    infoReturnable.cancel();
                }
            } else if (isDead() && source instanceof Constants.DamageSources.ProtagonistDamageSource) {
                InsanityHandler.resetProgress((PlayerEntity) (Object) this);
                if (source.getSource() instanceof ProtagonistEntity)
                    ((ProtagonistEntity) source.getAttacker()).removeAfterTargetKill();
            }
        }
    }

    @Inject(method = "initDataTracker()V", at = @At("TAIL"))
    private void addMiskStats(CallbackInfo info) {
        dataTracker.startTracking(SANITY, SANITY_CAP);
        dataTracker.startTracking(SHOCKED, false);

        dataTracker.startTracking(POWER_POOL, 0);
        dataTracker.startTracking(MAX_SPELLS, 0);
    }

    @Override
    public int getSanity() {
        return dataTracker.get(SANITY);
    }

    @Override
    public void setSanity(int sanity, boolean ignoreFactors) {
        if (ignoreFactors || (!isShocked() && !hasStatusEffect(MMMiscRegistries.StatusEffects.TRANQUILIZED)))
            dataTracker.set(SANITY, MathHelper.clamp(sanity, 0, getMaxSanity()));
    }

    @Override
    public void setShocked(boolean shocked) {
        dataTracker.set(SHOCKED, shocked);
    }

    @Override
    public boolean isShocked() {
        return dataTracker.get(SHOCKED);
    }

    @Override
    public int getMaxSanity() {
        int mod = 0;
        for (Integer value : getSanityCapExpansions().values()) {
            mod += value;
        }
        return Constants.DataTrackers.SANITY_CAP + mod;
    }

    //using normal packets since i don't feel like adding a new data tracker type for something that's updated so little lol
    @Override
    public void addSanityCapExpansion(String name, int amount) {
        sanityCapOverrides.putIfAbsent(name, amount);
        if (!world.isClient) {
            ExpandSanityPacket.send((PlayerEntity) (Object) this, name, amount);
        }
        if (getSanity() > getMaxSanity()) setSanity(getMaxSanity(), true);
    }

    @Override
    public void removeSanityCapExpansion(String name) {
        sanityCapOverrides.remove(name);
        if (!world.isClient && sanityCapOverrides.containsKey(name)) {
            RemoveExpansionPacket.send((PlayerEntity) (Object) this, name);
        }
    }

    @Override
    public Map<String, Integer> getSanityCapExpansions() {
        return sanityCapOverrides;
    }

    @Override
    public void syncSanityData() {
        sanityCapOverrides.forEach((s, i) -> {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeString(s);
            data.writeInt(i);
            ExpandSanityPacket.send((PlayerEntity) (Object) this, s, i);
        });
    }

    @Inject(method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void writeMiskData(CompoundTag compoundTag, CallbackInfo info) {
        CompoundTag tag = new CompoundTag();

        tag.putInt(Constants.NBT.SANITY, getSanity());
        tag.putBoolean(Constants.NBT.SHOCKED, isShocked());
        ListTag expansions = new ListTag();
        getSanityCapExpansions().forEach((s, i) -> {
            CompoundTag expansionTag = new CompoundTag();
            expansionTag.putString("Name", s);
            expansionTag.putInt("Amount", i);
            expansions.add(expansionTag);
        });
        tag.put(Constants.NBT.SANITY_EXPANSIONS, expansions);

        tag.putInt(Constants.NBT.POWER_POOL, getPowerPool());
        tag.putInt(Constants.NBT.MAX_SPELLS, getMaxSpells());

        CapabilityUtil.writeSpellData(this, tag);
        compoundTag.put(Constants.NBT.MISK_DATA, tag);
    }

    @Inject(method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    public void readMiskData(CompoundTag compoundTag, CallbackInfo info) {
        CompoundTag tag = (CompoundTag) compoundTag.get(Constants.NBT.MISK_DATA);
        if (tag != null) {
            setSanity(tag.getInt(Constants.NBT.SANITY), true);
            setShocked(tag.getBoolean(Constants.NBT.SHOCKED));
            getSanityCapExpansions().clear();
            ((ListTag) tag.get(Constants.NBT.SANITY_EXPANSIONS)).forEach(s -> addSanityCapExpansion(((CompoundTag) s).getString("Name"), ((CompoundTag) s).getInt("Amount")));
            syncSanityData();

            setPowerPool(tag.getInt(Constants.NBT.POWER_POOL));
            setMaxSpells(tag.getInt(Constants.NBT.MAX_SPELLS));

            getSpells().clear();
            for (int i = 0; i < tag.getList(Constants.NBT.SPELL_LIST, 10).size(); i++) {
                getSpells().add(i, Spell.fromTag((CompoundTag) tag.getList(Constants.NBT.SPELL_LIST, 10).get(i)));
            }
            getLearnedEffects().clear();
            tag.getList(Constants.NBT.SPELL_EFFECTS, 8).forEach(effectString -> {
                Identifier id = new Identifier(effectString.asString());
                if (SpellEffect.SPELL_EFFECTS.containsKey(id)) {
                    getLearnedEffects().add(SpellEffect.SPELL_EFFECTS.get(id));
                }
            });
            getAvailableMediums().clear();
            tag.getList(Constants.NBT.SPELL_MEDIUMS, 10).forEach(mediumTag -> {
                Identifier id = new Identifier(((CompoundTag) mediumTag).getString(Constants.NBT.SPELL_MEDIUM));
                if (SpellMedium.SPELL_MEDIUMS.containsKey(id)) {
                    setMediumAvailability(SpellMedium.SPELL_MEDIUMS.get(id), ((CompoundTag) mediumTag).getInt("Amount"));
                }
            });
            syncSpellData();
        }
    }


    @Override
    public boolean isSupernatural() {
        return false;
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        if (apparent) {
            ItemStack mask = MaskTrinketItem.getMask((PlayerEntity) (Object) this);
            if (!mask.isEmpty()) {
                return ((MaskTrinketItem) mask.getItem()).getAffiliation(true);
            }
        }
        return Affiliation.NONE;
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
    public int getPowerPool() {
        return dataTracker.get(POWER_POOL);
    }

    @Override
    public void setPowerPool(int amount) {
        dataTracker.set(POWER_POOL, amount);
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
    public Map<SpellMedium, Integer> getAvailableMediums() {
        return availableMediums;
    }

    @Override
    public void learnEffect(SpellEffect effect) {
        learnedEffects.add(effect);
        syncSpellData();
    }

    @Override
    public void setMediumAvailability(SpellMedium medium, int count) {
        availableMediums.put(medium, count);
        syncSpellData();
    }

    @Override
    public void syncSpellData() {
        if (!world.isClient) {
            SyncSpellCasterDataPacket.send(false, (PlayerEntity) (Object) this, this);
        }
    }
}