package com.miskatonicmysteries.mixin.item;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.MMMidnightLibConfig;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Inject(method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
	private void induceSanityAfterUse(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> info) {
		if (!stack.isEmpty() && Sanity.of(entity).isPresent() && (getUseAction(stack) == UseAction.BLOCK
			|| getUseAction(stack) == UseAction.DRINK || getUseAction(stack) == UseAction.EAT)) {
			MMRegistries.INSANITY_INDUCERS.forEach((inducer) -> {
				if (inducer.ingredient.test(stack)) {
					inducer.induceInsanity(world, entity, Sanity.of(entity).get());
				}
			});
		}
	}

	@Shadow
	public abstract UseAction getUseAction(ItemStack stack);

	@Inject(method = "usageTick", at = @At("HEAD"))
	public void tickShield(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo info) {
		if (!world.isClient && stack.getItem() instanceof ShieldItem
			&& user.getRandom().nextInt(MMMidnightLibConfig.modUpdateInterval) == 0) {
			if (stack.hasNbt() && stack.getNbt().getCompound(Constants.NBT.BLOCK_ENTITY_TAG) != null) {
				NbtCompound compoundTag = stack.getSubNbt(Constants.NBT.BLOCK_ENTITY_TAG);
				if (compoundTag != null && compoundTag.contains(Constants.NBT.BANNER_PP_TAG, 9) &&
					Util.isValidYellowSign(compoundTag.getList(Constants.NBT.BANNER_PP_TAG, 10))) {
					int distance = 16 * 16;
					Vec3d vec3d = user.getCameraPosVec(1);
					Vec3d vec3d2 = user.getRotationVec(1);
					Vec3d vec3d3 = vec3d.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);

					EntityHitResult hit = ProjectileUtil.getEntityCollision(world, user, vec3d, vec3d3,
							user.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D),
							(target) -> !target.isSpectator()  && user.isAttackable() && user.canSee(target));

					if (hit != null && hit.getEntity() instanceof LivingEntity && ((LivingEntity) hit.getEntity()).canSee(user)
						&& !MiskatonicMysteriesAPI.isImmuneToYellowSign((LivingEntity) hit.getEntity())) {
						LivingEntity target = (LivingEntity) hit.getEntity();
						if (user instanceof MobEntity m && target != m.getTarget()) {
							return;
						}
						target.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 200, 1, false, true));
						Sanity.of(target).ifPresent(sanity -> {
							sanity.setSanity(((Sanity) target).getSanity() - 5, false);
							sanity.setShocked(true);
						});
					}
				}
			}
		}
	}
}
