package com.miskatonicmysteries.mixin;

import com.google.common.collect.ImmutableSet;
import com.miskatonicmysteries.api.interfaces.Appeasable;
import com.miskatonicmysteries.api.item.MMBookItem;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
    @Unique
    private static final Set FORBIDDEN_POTIONS = ImmutableSet.of(Potions.WATER, Potions.EMPTY, Potions.MUNDANE, Potions.AWKWARD, Potions.THICK);

    @Shadow
    public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    @Shadow
    public abstract void playAmbientSound();

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Appeasable.of(this).ifPresent(appeasable -> {
            if ((Object) this instanceof WitchEntity) {
                if (appeasable.isAppeased() && getEquippedStack(EquipmentSlot.MAINHAND).isEmpty() && player.getStackInHand(hand).getItem() == MMObjects.NECRONOMICON && !MMBookItem.hasKnowledge(Constants.Misc.WITCH_KNOWLEDGE, player.getStackInHand(hand))) {
                    if (!player.world.isClient) {
                        equipStack(EquipmentSlot.MAINHAND, player.getStackInHand(hand));
                        player.setStackInHand(hand, ItemStack.EMPTY);
                        player.inventory.markDirty();
                    }
                } else if (!appeasable.isAppeased() && player.getStackInHand(hand).getItem() == Items.POTION && !FORBIDDEN_POTIONS.contains(PotionUtil.getPotion(player.getStackInHand(hand)))) {
                    player.getStackInHand(hand).decrement(1);
                    appeasable.setAppeasedTicks(200 + player.getRandom().nextInt(200));
                    playAmbientSound();
                    player.world.sendEntityStatus((Entity) (Object) this, (byte) 14);
                }
            }
        });
    }
}
