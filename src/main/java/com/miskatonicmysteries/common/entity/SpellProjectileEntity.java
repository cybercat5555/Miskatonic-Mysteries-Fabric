package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.nbt.NbtCompound;
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
        super(MMEntities.SPELL_PROJECTILE, caster, world);
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
        remove(RemovalReason.DISCARDED);
    }


    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt(Constants.NBT.INTENSITY, getIntensity());
        tag.putString(Constants.NBT.SPELL_EFFECT, dataTracker.get(EFFECT));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        setIntensity(tag.getInt(Constants.NBT.INTENSITY));
        setSpell(MMRegistries.SPELL_EFFECTS.get(new Identifier(tag.getString(Constants.NBT.SPELL_EFFECT))));
    }

    public void setSpell(SpellEffect effect) {
        dataTracker.set(EFFECT, effect == null ? "" : effect.getId().toString());
    }

    @Nullable
    public SpellEffect getSpell() {
        return MMRegistries.SPELL_EFFECTS.get(new Identifier(dataTracker.get(EFFECT)));
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
            getSpell().effect(world, (LivingEntity) getOwner(), entityHitResult.getEntity(), entityHitResult.getPos(), MMSpellMediums.PROJECTILE, getIntensity(), this);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (getOwner() instanceof LivingEntity && getSpell() != null) {
            getSpell().effect(world, (LivingEntity) getOwner(), null, blockHitResult.getPos(), MMSpellMediums.PROJECTILE, getIntensity(), this);
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