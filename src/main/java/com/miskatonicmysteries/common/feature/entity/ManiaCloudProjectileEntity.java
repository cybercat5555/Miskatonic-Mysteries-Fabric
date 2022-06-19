package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

public class ManiaCloudProjectileEntity extends ExplosiveProjectileEntity {

    public ManiaCloudProjectileEntity(EntityType<? extends ManiaCloudProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ManiaCloudProjectileEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(MMEntities.MANIA_PROJECTILE, owner, directionX, directionY, directionZ, world);
    }


    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult)hitResult).getEntity())) {
            if (!this.world.isClient) {
                List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D));
                AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity) {
                    areaEffectCloudEntity.setOwner((LivingEntity)entity);
                }

                areaEffectCloudEntity.setRadius(3.0F);
                areaEffectCloudEntity.setDuration(600);
                areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 1, 1));
                if (!list.isEmpty()) {
                    for (LivingEntity livingEntity : list) {
                        double d = this.squaredDistanceTo(livingEntity);
                        if (d < 16.0D) {
                            areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            break;
                        }
                    }
                }
                this.world.spawnEntity(areaEffectCloudEntity);
                this.discard();
            }

        }
    }

    public boolean collides() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    protected boolean isBurning() {
        return false;
    }
}
