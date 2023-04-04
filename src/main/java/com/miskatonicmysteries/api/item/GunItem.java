package com.miskatonicmysteries.api.item;

import com.miskatonicmysteries.common.feature.block.Shootable;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public abstract class GunItem extends Item {

	public GunItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!canUse(user)) {
			if (world.isClient) {
				user.sendMessage(Text.translatable("message." + Constants.MOD_ID + ".heavy_gun.needs_offhand"), true);
			}
			return TypedActionResult.fail(stack);
		}
		if (user.isSneaking()) {
			setLoading(stack, true);
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		} else if (!isLoading(stack) && isLoaded(stack)) {
			shoot(world, user, stack);
			stack.damage(1, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));
			return TypedActionResult.consume(stack);
		}
		return TypedActionResult.pass(stack);
	}

	public static boolean isLoading(ItemStack stack) {
		if (!stack.hasNbt()) {
			stack.setNbt(new NbtCompound());
			stack.getNbt().putBoolean(Constants.NBT.LOADING, false);
			return false;
		}
		return stack.getNbt().getBoolean(Constants.NBT.LOADING);
	}

	public static ItemStack setLoading(ItemStack stack, boolean loading) {
		if (!stack.hasNbt()) {
			stack.setNbt(new NbtCompound());
		}
		stack.getNbt().putBoolean(Constants.NBT.LOADING, loading);
		return stack;
	}

	public static boolean isLoaded(ItemStack stack) {
		if (!stack.hasNbt()) {
			stack.setNbt(new NbtCompound());
		}
		return stack.getNbt().getInt(Constants.NBT.SHOTS) > 0;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		return isLoading(stack) ? loadGun(stack, world, user) : stack;
	}

	public ItemStack loadGun(ItemStack stack, World world, LivingEntity user) {
		if (!stack.hasNbt()) {
			stack.setNbt(new NbtCompound());
		}

		int generatedShots = user instanceof PlayerEntity && !((PlayerEntity) user).isCreative() ? loadBullets((PlayerEntity) user,
																											   stack.getNbt()
																												   .getInt(Constants.NBT.SHOTS))
																								 : getMaxShots();
		stack.getNbt().putInt(Constants.NBT.SHOTS, generatedShots);
		setLoading(stack, false);
		if (user instanceof PlayerEntity) {
			((PlayerEntity) user).getItemCooldownManager().set(this, getLoadingTime());
		}
		return stack;
	}

	private int loadBullets(PlayerEntity user, int startCount) {
		ItemStack stack = new ItemStack(MMObjects.BULLET);
		int bullets = startCount;
		for (int i = 0; i < getMaxShots() - startCount; i++) {
			int slot = Util.getSlotWithStack(user.getInventory(), stack);
			if (slot >= 0) {
				user.getInventory().getStack(slot).decrement(1);
				bullets++;
			}
		}
		return bullets;
	}

	public abstract int getLoadingTime();

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return isLoading(stack) ? getLoadingTime() : 10;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		stack.getNbt().putBoolean(Constants.NBT.LOADING, false);
		super.onStoppedUsing(stack, world, user, remainingUseTicks);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(
				Text.translatable(isLoaded(stack) ? "tooltip.miskatonicmysteries.gun_loaded" : "tooltip.miskatonicmysteries.gun_not_loaded",
								 stack.getNbt().getInt(Constants.NBT.SHOTS), getMaxShots())
				.setStyle(Style.EMPTY.withColor(isLoaded(stack) ? TextColor.fromRgb(0x00FF00) : TextColor.fromRgb(0xFF0000))));
		tooltip.add(Text.translatable("tooltip.miskatonicmysteries.gun_tip_load")
						.setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.GRAY)));
		super.appendTooltip(stack, world, tooltip, context);
	}

	public abstract int getMaxShots();

	private boolean canUse(PlayerEntity entity) {
		return !isHeavy() || entity.getOffHandStack().isEmpty();
	}

	public abstract boolean isHeavy();

	public void shoot(World world, LivingEntity attacker, ItemStack stack) {
		Vec3d vec3d = attacker.getCameraPosVec(1);
		Vec3d vec3d2 = attacker.getRotationVec(1);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * getMaxDistance(), vec3d2.y * getMaxDistance(), vec3d2.z * getMaxDistance());
		HitResult blockHit = world
			.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, attacker));
		double distance = Math.pow(getMaxDistance(), 2);
		EntityHitResult hit = ProjectileUtil.getEntityCollision(world, attacker, vec3d, vec3d3,
																attacker.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D),
																(target) -> !target.isSpectator()  && attacker.isAttackable() && attacker.canSee(target));

		BlockPos blockPos = new BlockPos(blockHit.getPos());
		if (world.getBlockState(blockPos).getBlock() instanceof Shootable) {
			((Shootable) world.getBlockState(blockPos).getBlock()).onShot(world, blockPos, attacker);
		}
		if (hit != null && hit.getEntity() != null && (blockHit.squaredDistanceTo(attacker) > hit.getEntity().squaredDistanceTo(attacker))) {
			hit.getEntity().damage(new EntityDamageSource(Constants.MOD_ID + ".gun", attacker), getDamage());
			if (world.isClient) {
				for (int i = 0; i < 4; i++) {
					world.addParticle(ParticleTypes.SMOKE, hit.getPos().x + world.random.nextGaussian() / 20F,
									  hit.getPos().y + world.random.nextGaussian() / 20F, hit.getPos().z + world.random.nextGaussian() / 20F, 0, 0,
									  0);
				}
			}
		}

		setLoading(stack, false);
		stack.getNbt().putInt(Constants.NBT.SHOTS, stack.getNbt().getInt(Constants.NBT.SHOTS) - 1);
		world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), MMSounds.ITEM_GUN_GUN_SHOT, SoundCategory.PLAYERS, 0.6F,
						1.0F / (world.random.nextFloat() * 0.2F + (isHeavy() ? 1F : 0.5F)));

		if (attacker instanceof PlayerEntity) {
			((PlayerEntity) attacker).getItemCooldownManager().set(this, getCooldown());
			((PlayerEntity) attacker).incrementStat(Stats.USED.getOrCreateStat(this));
		}
	}

	public abstract int getCooldown();

	public abstract int getDamage();

	public abstract int getMaxDistance();
}
