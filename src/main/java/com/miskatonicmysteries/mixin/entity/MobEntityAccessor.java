package com.miskatonicmysteries.mixin.entity;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEntity.class)
public interface MobEntityAccessor {

	@Accessor("goalSelector")
	GoalSelector getGoalSelector();

	@Accessor("targetSelector")
	GoalSelector getTargetSelector();

	@Invoker("disablePlayerShield")
	public void invokeDisablePlayerShield(PlayerEntity player, ItemStack mobStack, ItemStack playerStack);
}
