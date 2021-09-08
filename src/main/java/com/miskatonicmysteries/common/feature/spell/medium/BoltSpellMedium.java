package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.BoltEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BoltSpellMedium extends SpellMedium {
    public BoltSpellMedium() {
        super(new Identifier(Constants.MOD_ID, "bolt"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
        Vec3d vec3d = caster.getCameraPosVec(1);
        Vec3d vec3d2 = caster.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * getMaxDistance(), vec3d2.y * getMaxDistance(), vec3d2.z * getMaxDistance());
        HitResult blockHit = world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, caster));
        double distance = Math.pow(getMaxDistance(), 2);
        EntityHitResult hit = ProjectileUtil.getEntityCollision(world, caster, vec3d, vec3d3, caster.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D), (target) -> !target.isSpectator() && target.collides());
        if (!world.isClient && blockHit.getPos() != null) {
            BoltEntity bolt = new BoltEntity(caster, hit != null && hit.getEntity() != null ? hit.getEntity().distanceTo(caster) : blockHit.getPos().distanceTo(caster.getPos()), effect.getColor(caster));
            world.spawnEntity(bolt);
        }
        if (hit != null && blockHit.squaredDistanceTo(caster) > hit.getEntity().squaredDistanceTo(caster)) {
            return effect.effect(world, caster, hit.getEntity(), hit.getPos(), this, intensity, caster);
        } else {
            return effect.effect(world, caster, null, blockHit.getPos(), this, intensity, caster);
        }
    }

    private int getMaxDistance() {
        return 24;
    }

    @Override
    public float getCooldownModifier(LivingEntity caster) {
        return 1.5F;
    }
}
