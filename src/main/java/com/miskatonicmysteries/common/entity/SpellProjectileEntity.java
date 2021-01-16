package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.feature.spell.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.SpellMedium;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SpellProjectileEntity extends ThrownEntity {
    protected static final TrackedData<String> EFFECT = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.STRING);
    protected static final TrackedData<Integer> INTENSITY = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public SpellProjectileEntity(EntityType<SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public SpellProjectileEntity(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        super(ModEntities.SPELL_PROJECTILE, caster, world);
        setSpell(effect);
        setIntensity(intensity);
        setOwner(caster);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        remove();
    }


    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.INTENSITY, getIntensity());
        tag.putString(Constants.NBT.SPELL_EFFECT, dataTracker.get(EFFECT));
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        setIntensity(tag.getInt(Constants.NBT.INTENSITY));
        setSpell(SpellEffect.SPELL_EFFECTS.getOrDefault(new Identifier(tag.getString(Constants.NBT.SPELL_EFFECT)), null));
    }

    public void setSpell(SpellEffect effect) {
        dataTracker.set(EFFECT, effect == null ? "" : effect.getId().toString());
    }

    @Nullable
    public SpellEffect getSpell() {
        return SpellEffect.SPELL_EFFECTS.getOrDefault(new Identifier(dataTracker.get(EFFECT)), null);
    }

    public void setIntensity(int intensity) {
        dataTracker.set(INTENSITY, intensity);
    }

    public int getIntensity() {
        return dataTracker.get(INTENSITY);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (getOwner() instanceof LivingEntity && getSpell() != null) {
            getSpell().effect(world, (LivingEntity) getOwner(), entityHitResult.getEntity(), entityHitResult.getPos(), SpellMedium.PROJECTILE, getIntensity(), this);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (getOwner() instanceof LivingEntity && getSpell() != null) {
            getSpell().effect(world, (LivingEntity) getOwner(), null, blockHitResult.getPos(), SpellMedium.PROJECTILE, getIntensity(), this);
        }
    }

    @Override
    protected boolean updateWaterState() {
        return false;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(EFFECT, "");
        dataTracker.startTracking(INTENSITY, 0);
    }

    @Override
    protected float getGravity() {
        return 0.005F;
    }
}