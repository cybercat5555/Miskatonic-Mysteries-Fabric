package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class SpellMediumVision extends SpellMedium {
    public SpellMediumVision() {
        super(new Identifier(Constants.MOD_ID, "vision"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        if (caster instanceof MobEntity && ((MobEntity) caster).getTarget() != null && caster.canSee(((MobEntity) caster).getTarget())) {
            return effect.effect(world, caster, ((MobEntity) caster).getTarget(), this, intensity);
        } else {
            Vec3d vec3d = caster.getCameraPosVec(1);
            Vec3d vec3d2 = caster.getRotationVec(1);
            Vec3d vec3d3 = vec3d.add(vec3d2.x * effect.getMaxDistance(caster), vec3d2.y * effect.getMaxDistance(caster), vec3d2.z * effect.getMaxDistance(caster));
            HitResult blockHit = world.rayTrace(new RayTraceContext(vec3d, vec3d3, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, caster));
            double distance = Math.pow(effect.getMaxDistance(caster), 2);
            EntityHitResult hit = ProjectileUtil.rayTrace(caster, vec3d, vec3d3, caster.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D), (target) -> !target.isSpectator() && target.collides(), distance);
            if (hit != null && hit.getEntity() != null && (blockHit.squaredDistanceTo(caster) > hit.getEntity().squaredDistanceTo(caster))) {
                return effect.effect(world, caster, hit.getEntity(), this, intensity);
            }
        }
        return false;
    }
}
