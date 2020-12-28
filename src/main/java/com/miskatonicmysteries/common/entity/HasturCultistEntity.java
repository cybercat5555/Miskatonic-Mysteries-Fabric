package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.entity.ai.SpellCastGoal;
import com.miskatonicmysteries.common.entity.ai.TacticalDrawbackGoal;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.mixin.LivingEntityMixin;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.Durations;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.IntRange;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class HasturCultistEntity extends PathAwareEntity implements Angerable, Affiliated {
    protected static final TrackedData<Integer> VARIANT = DataTracker.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Boolean> ASCENDED = DataTracker.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected static final TrackedData<Boolean> CASTING = DataTracker.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    //anger
    private static final IntRange ANGER_TIME_RANGE = Durations.betweenSeconds(20, 30);
    private int angerTime;
    @Nullable
    private UUID targetUuid;

    //spellcasting
    @Nullable
    public Spell currentSpell;

    //save current spell

    /*
        Ascended: stronger stats and regen, more likely to use supporting spells

        Equipment: 75% chance for any sword, + 50% chance to spawn with a shield

        If a cultist has a sword, they will attack when the player is close enough, now and then
        If a cultist also has a shield, they will use it to block attacks (if the player is not way to close, and as long as the attack cooldown is active)
        If a cultist does not have a shield, they will try to cast a spell, usually one to heal or to push the player around
        They need to be far away enough to cast it though

    */
    public HasturCultistEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
        this.getNavigation().setCanSwim(true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new LongDoorInteractGoal(this, false));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 1D, false));

        //fight logic will be here
        this.goalSelector.add(3, new SpellCastGoal<>(this)); //also add a random chance to just not use it in the shouldexecute part
        //add random draw back ai that gets enforced when hit or randomly, where they'll try to keep a set distance
        this.goalSelector.add(3, new TacticalDrawbackGoal<>(this));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.2D, false));

        //also an ai to heal nearby villagers and golems if they're wounded


        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));

        this.targetSelector.add(0, new RevengeGoal(this, HasturCultistEntity.class).setGroupRevenge());
        //only follow if the attack cooldown is low!
        this.targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, true, living -> living instanceof Affiliated && ((Affiliated) living).getAffiliation() == Constants.Affiliation.SHUB));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, HostileEntity.class, 5, true, true, mob -> !(mob instanceof HasturCultistEntity) && !(mob instanceof CreeperEntity)));
        this.targetSelector.add(3, new UniversalAngerGoal<>(this, true));
    }

    @Override
    public void tickMovement() {
        this.tickHandSwing();
        super.tickMovement();
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (currentSpell == null) setCasting(false);
    }


    @Override
    public boolean damage(DamageSource source, float amount) {
        if (amount > 0.0F && ((LivingEntityMixin) this).callBlockedByShield(source)) {
            this.damageShield(amount);
            if (!source.isProjectile()) {
                Entity entity = source.getSource();
                if (entity instanceof LivingEntity) {
                    this.takeShieldHit((LivingEntity) entity);
                    world.sendEntityStatus(entity, (byte) 29);
                }
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public void swingHand(Hand hand) {
        this.swingHand(hand, true);
    }

    @Override //they are stronk
    protected void knockback(LivingEntity target) {
        target.takeKnockback(target instanceof PlayerEntity ? 1 : 0.5F, target.getX() - this.getX(), target.getZ() - this.getZ());
    }

    @Override
    public void takeKnockback(float strength, double x, double z) {
        if (isBlocking()) {
            strength -= 0.3F;
        }
        super.takeKnockback(strength, x, z);
    }

    @Override
    public boolean isBlocking() {
        return super.isBlocking();
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);

        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
        dataTracker.set(VARIANT, random.nextInt(2));
        if (spawnReason != SpawnReason.EVENT && spawnReason != SpawnReason.STRUCTURE)
            dataTracker.set(ASCENDED, random.nextBoolean());
        setLeftHanded(random.nextBoolean()); //more left-handedness lol
        initEquipment(difficulty);
        return entityData;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(VARIANT, 0);
        dataTracker.startTracking(ASCENDED, false);
        dataTracker.startTracking(CASTING, false);
    }

    public int getVariant() {
        return dataTracker.get(VARIANT);
    }

    public boolean isAscended() {
        return dataTracker.get(ASCENDED);
    }

    public void ascend(boolean ascend) {
        dataTracker.set(ASCENDED, ascend);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.VARIANT, getVariant());
        tag.putBoolean(Constants.NBT.ASCENDED, isAscended());
        tag.putBoolean(Constants.NBT.CASTING, isCasting());
        if (currentSpell != null) {
            CompoundTag spell = currentSpell.toTag(new CompoundTag());
            tag.put(Constants.NBT.SPELL, spell);
        }
        angerToTag(tag);
    }


    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
        ascend(tag.getBoolean(Constants.NBT.ASCENDED));
        if (tag.contains(Constants.NBT.SPELL)) {
            currentSpell = Spell.fromTag((CompoundTag) tag.get(Constants.NBT.SPELL));
        }
        angerFromTag((ServerWorld) world, tag);
    }

    @Override
    public int getAngerTime() {
        return angerTime;
    }

    @Override
    public void setAngerTime(int ticks) {
        this.angerTime = ticks;
    }

    @Override
    public @Nullable
    UUID getAngryAt() {
        return targetUuid;
    }

    @Override
    public void setAngryAt(@Nullable UUID uuid) {
        this.targetUuid = uuid;
    }

    @Override
    public void chooseRandomAngerTime() {
        setAngerTime(ANGER_TIME_RANGE.choose(this.random));
    }

    public void setCasting(boolean casting) {
        if (!casting) currentSpell = null;
        dataTracker.set(CASTING, casting);
    }

    public boolean isCasting() {
        return dataTracker.get(CASTING);
    }

    @Override
    public Identifier getAffiliation() {
        return Constants.Affiliation.HASTUR;
    }

    @Override
    public boolean isSupernatural() {
        return true;
    }
}
