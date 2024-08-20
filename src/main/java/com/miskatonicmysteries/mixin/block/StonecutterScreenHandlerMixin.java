package com.miskatonicmysteries.mixin.block;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.StonecutterScreenHandler;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StonecutterScreenHandler.class)
public class StonecutterScreenHandlerMixin {
//
//	@Shadow
//	private List<StonecuttingRecipe> availableRecipes;
//	@Unique
//	private PlayerEntity mm_cachedPlayer;
//
//	@Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("RETURN"))
//	private void init(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
//		this.mm_cachedPlayer = playerInventory.player;
//	}
//
//	@Inject(method = "updateInput", at = @At("TAIL"))
//	private void updateInput(Inventory input, ItemStack stack, CallbackInfo ci) {
//		if (mm_cachedPlayer != null) {
//			for (StonecuttingRecipe recipe : List.copyOf(availableRecipes)) {
//				Item outputItem = recipe.getOutput().getItem();
//				if (outputItem instanceof Affiliated || (outputItem instanceof BlockItem b && b.getBlock() instanceof Affiliated)) {
//					Affiliated affiliated = outputItem instanceof Affiliated
//											? (Affiliated) outputItem
//											: (Affiliated) ((BlockItem) outputItem).getBlock();
//					if (affiliated.getAffiliation(true) != MiskatonicMysteriesAPI.getNonNullAffiliation(mm_cachedPlayer, false)) {
//						availableRecipes.remove(recipe);
//					}
//				}
//			}
//		}
//	}
}
