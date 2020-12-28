package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModObjects;
import com.miskatonicmysteries.common.lib.ModRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public abstract class GunItem extends Item {
    public GunItem() {
        super(new Settings().group(Constants.MM_GROUP).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!canUse(user)) {
            if (world.isClient) user.sendMessage(new TranslatableText("message.heavy_gun.needs_offhand"), true);
            return TypedActionResult.fail(stack);
        }
        if (user.isSneaking()) {
            setLoading(stack, true);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else if (isLoaded(stack)) {
            shoot(world, user, stack);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.pass(stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText(isLoaded(stack) ? "tooltip.miskatonicmysteries.gun_loaded" : "tooltip.miskatonicmysteries.gun_not_loaded", stack.getTag().getInt(Constants.NBT.SHOTS), getMaxShots()).setStyle(Style.EMPTY.withColor(isLoaded(stack) ? TextColor.fromRgb(0x00FF00) : TextColor.fromRgb(0xFF0000))));
        tooltip.add(new TranslatableText("tooltip.miskatonicmysteries.gun_tip_load").setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.GRAY)));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return !isLoaded(stack) ? loadGun(stack, world, user) : stack;
    }

    public static boolean isLoading(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
            stack.getTag().putBoolean(Constants.NBT.LOADING, false);
            return false;
        }
        return stack.getTag().getBoolean(Constants.NBT.LOADING);
    }

    public static ItemStack setLoading(ItemStack stack, boolean loading) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        stack.getTag().putBoolean(Constants.NBT.LOADING, loading);
        return stack;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        stack.getTag().putBoolean(Constants.NBT.LOADING, false);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    public ItemStack loadGun(ItemStack stack, World world, LivingEntity user) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());

        int generatedShots = user instanceof PlayerEntity && !((PlayerEntity) user).isCreative() ? loadBullets((PlayerEntity) user, stack.getTag().getInt(Constants.NBT.SHOTS)) : getMaxShots();
        stack.getTag().putInt(Constants.NBT.SHOTS, generatedShots);
        setLoading(stack, false);
        if (user instanceof PlayerEntity) ((PlayerEntity) user).getItemCooldownManager().set(this, getLoadingTime());
        return stack;
    }

    private int loadBullets(PlayerEntity user, int startCount) {
        ItemStack stack = new ItemStack(ModObjects.BULLET);
        int bullets = startCount;
        for (int i = 0; i < getMaxShots() - startCount; i++) {
            int slot = user.inventory.getSlotWithStack(stack);
            if (slot >= 0) {
                user.inventory.removeStack(slot, 1);
                bullets++;
            }
        }
        return bullets;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public abstract int getMaxShots();

    private boolean canUse(PlayerEntity entity) {
        return !isHeavy() || entity.getOffHandStack().isEmpty();
    }

    public void shoot(World world, LivingEntity player, ItemStack stack) {
        Vec3d vec3d = player.getCameraPosVec(1);
        Vec3d vec3d2 = player.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * getMaxDistance(), vec3d2.y * getMaxDistance(), vec3d2.z * getMaxDistance());
        HitResult blockHit = world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        double distance = Math.pow(getMaxDistance(), 2);
        EntityHitResult hit = ProjectileUtil.raycast(player, vec3d, vec3d3, player.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D), (target) -> !target.isSpectator() && target.collides(), distance);

        if (hit != null && hit.getEntity() != null && (blockHit.squaredDistanceTo(player) > hit.getEntity().squaredDistanceTo(player))) {
            hit.getEntity().damage(new EntityDamageSource(Constants.MOD_ID + ".gun", player), getDamage());
            if (hit.getEntity() instanceof LivingEntity)
                ((LivingEntity) hit.getEntity()).setAttacker(player);

            if (world.isClient) {
                for (int i = 0; i < 4; i++)
                    world.addParticle(ParticleTypes.SMOKE, hit.getPos().x + world.random.nextGaussian() / 20F, hit.getPos().y + world.random.nextGaussian() / 20F, hit.getPos().z + world.random.nextGaussian() / 20F, 0, 0, 0);
            }
        }

        setLoading(stack, false);
        stack.getTag().putInt(Constants.NBT.SHOTS, stack.getTag().getInt(Constants.NBT.SHOTS) - 1);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), ModRegistries.GUN_SHOT, SoundCategory.PLAYERS, 0.6F, 1.0F / (RANDOM.nextFloat() * 0.2F + (isHeavy() ? 1F : 0.5F)));

        if (player instanceof PlayerEntity) {
            ((PlayerEntity) player).getItemCooldownManager().set(this, getCooldown());
            ((PlayerEntity) player).incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    public static boolean isLoaded(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        return stack.getTag().getInt(Constants.NBT.SHOTS) > 0;
    }

    public abstract boolean isHeavy();

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return isLoading(stack) ? getLoadingTime() : 10;
    }

    public abstract int getLoadingTime();

    public abstract int getCooldown();

    public abstract int getDamage();

    public abstract int getMaxDistance();
}
