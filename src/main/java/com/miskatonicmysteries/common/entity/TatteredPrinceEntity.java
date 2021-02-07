package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.entity.ai.CastSpellGoal;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.interfaces.Affiliated;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.EffectParticlePacket;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class TatteredPrinceEntity extends PathAwareEntity implements IAnimatable, Affiliated, CastingMob {
    private final ServerBossBar bossBar;
    private AnimationFactory factory = new AnimationFactory(this);
    protected static final TrackedData<Integer> CASTING_TIME_LEFT = DataTracker.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Nullable
    public Spell currentSpell;

    public TatteredPrinceEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        bossBar = new ServerBossBar(getDisplayName(), BossBar.Color.YELLOW, BossBar.Style.PROGRESS);
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true); //probably make own navigation that allows re-size, or do teleport ig
        this.getNavigation().setCanSwim(true);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(CASTING_TIME_LEFT, 0);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource == DamageSource.OUT_OF_WORLD) {
            return false;
        } else if (isInvulnerable()) {
            return true;
        } else if (damageSource == DamageSource.MAGIC || damageSource == DamageSource.GENERIC) {
            return false;
        }
        return true;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (!isAttacking()) {
            heal(1);
        }
        if (isCasting()) {
            if (currentSpell != null && !world.isClient) {
                EffectParticlePacket.send(this);
            }
        }
        if (!world.isClient && currentSpell != null && getCastTime() <= 0) {
            currentSpell.cast(this);
            currentSpell = null;
            getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, 40);
        }
        bossBar.setPercent(getHealth() / getMaxHealth());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new LongDoorInteractGoal(this, false));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new CastSpellGoal<>(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 12));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.targetSelector.add(0, new RevengeGoal(this, TatteredPrinceEntity.class));
        super.initGoals();
    }

    @Override
    public boolean isAffectedBySplashPotions() {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return world.getEntitiesByClass(TatteredPrinceEntity.class, getBoundingBox().expand(50, 50, 50), null).size() < 1;
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        bossBar.setName(getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        bossBar.removePlayer(player);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public <P extends IAnimatable> PlayState animationPredicate(AnimationEvent<P> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walking", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }


    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    public void tickMovement() {
        this.tickHandSwing();
        super.tickMovement();
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.CASTING, getCastTime());
        if (currentSpell != null) {
            CompoundTag spell = currentSpell.toTag(new CompoundTag());
            tag.put(Constants.NBT.SPELL, spell);
        }
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        if (hasCustomName()) {
            bossBar.setName(getDisplayName());
        }
        if (tag.contains(Constants.NBT.SPELL)) {
            currentSpell = Spell.fromTag((CompoundTag) tag.get(Constants.NBT.SPELL));
        }
        setCastTime(tag.getInt(Constants.NBT.CASTING));
    }

    @Override
    public void setCastTime(int castTime) {
        dataTracker.set(CASTING_TIME_LEFT, castTime);
    }

    @Override
    public int getCastTime() {
        return dataTracker.get(CASTING_TIME_LEFT);
    }

    @Override
    public boolean isCasting() {
        return dataTracker.get(CASTING_TIME_LEFT) > 0;
    }

    @Override
    @Nullable
    public Spell getCurrentSpell() {
        return currentSpell;
    }

    @Override
    public void setCurrentSpell(@Nullable Spell currentSpell) {
        this.currentSpell = currentSpell;
    }

    @Override
    public Spell selectSpell() {
        SpellMedium medium = SpellMedium.BOLT;
        SpellEffect effect = SpellEffect.DAMAGE;
        Spell spell = new Spell(medium, effect, 2 + world.random.nextInt(3));
        return spell;
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return Affiliation.HASTUR;
    }

    @Override
    public boolean isSupernatural() {
        return true;
    }
}
