package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public abstract class ItemGun extends Item {
    public ItemGun() {
        super(new Settings().group(Constants.MM_GROUP).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (canUse(user)) {
            user.setCurrentHand(hand);
            if (isLoaded(stack)) {
                shoot(world, user, stack);
                return TypedActionResult.consume(stack);
            }
            setLoading(stack, true);
            return TypedActionResult.consume(stack);
        }
        if (world.isClient) user.sendMessage(new TranslatableText("message.heavy_gun.needs_offhand"), true);
        return TypedActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return !isLoaded(stack) ? loadGun(stack, world, user) : stack;
    }

    public static boolean isLoading(ItemStack stack){
        if (!stack.hasTag()){
            stack.setTag(new CompoundTag());
            stack.getTag().putBoolean(Constants.NBT.LOADING, false);
            return false;
        }
        return stack.getTag().getBoolean(Constants.NBT.LOADING);
    }

    public static ItemStack setLoading(ItemStack stack, boolean loading){
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        stack.getTag().putBoolean(Constants.NBT.LOADING, loading);
        return stack;
    }
//todo fix the thing sometimes now being in loading mode

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        stack.getTag().putBoolean(Constants.NBT.LOADING, false);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    private ItemStack loadGun(ItemStack stack, World world, LivingEntity user) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());

        stack.getTag().putInt(Constants.NBT.SHOTS, getMaxShots());
        setLoading(stack, false);
        if (user instanceof PlayerEntity) ((PlayerEntity) user).getItemCooldownManager().set(this, getCooldown() / 2);
        return stack;
    }

    public abstract int getMaxShots();

    private boolean canUse(PlayerEntity entity){
        return !isHeavy() || entity.getOffHandStack().isEmpty();
    }

    private void shoot(World world, PlayerEntity player, ItemStack stack) {
        Vec3d vec3d = player.getCameraPosVec(1);
        Vec3d vec3d2 = player.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * getMaxDistance(), vec3d2.y * getMaxDistance(), vec3d2.z * getMaxDistance());
        HitResult blockHit = world.rayTrace(new RayTraceContext(vec3d, vec3d3, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, player));
        double distance = Math.pow(getMaxDistance(), 2);
        EntityHitResult hit = ProjectileUtil.rayTrace(player, vec3d, vec3d3, player.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D), (target) -> target instanceof LivingEntity && !target.isSpectator() && target.collides(), distance);

        if (hit != null && hit.getEntity() != null && (blockHit.squaredDistanceTo(player) > hit.getEntity().squaredDistanceTo(player))) {
            hit.getEntity().damage(DamageSource.ANVIL, getDamage()); //create own damageSource
            //possibly apply enchantments
        }

        stack.getTag().putInt(Constants.NBT.SHOTS, stack.getTag().getInt(Constants.NBT.SHOTS) - 1);
        setLoading(stack, false);
        //todo actual sounds
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F));

        player.getItemCooldownManager().set(this, getCooldown());
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    public static boolean isLoaded(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getInt(Constants.NBT.SHOTS) > 0;
    }

  /*  @Override
    public UseAction getUseAction(ItemStack stack) {
        return isLoading(stack) ? UseAction.NONE : UseAction.BOW;
    }*/

    public abstract boolean isHeavy();

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return getLoadingTime();
    }

    public abstract int getLoadingTime();

    public abstract int getCooldown();

    public abstract int getDamage();

    public abstract int getMaxDistance();
}
