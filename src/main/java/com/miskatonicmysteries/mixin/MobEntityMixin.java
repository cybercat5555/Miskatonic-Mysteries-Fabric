package com.miskatonicmysteries.mixin;

import com.google.common.collect.ImmutableSet;
import com.miskatonicmysteries.api.interfaces.Appeasable;
import com.miskatonicmysteries.api.interfaces.Hallucination;
import com.miskatonicmysteries.api.item.MMBookItem;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements Hallucination {
    private static final TrackedData<Optional<UUID>> HALLUCINATION_UUID = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public Optional<UUID> getHallucinationTarget() {
        return dataTracker.get(HALLUCINATION_UUID);
    }

    @Override
    public void setHallucinationTarget(Optional<UUID> target) {
        dataTracker.set(HALLUCINATION_UUID, target);
    }

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
                setHallucinationTarget(Optional.of(player.getUuid())); //todo that's a test
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
                    player.world.sendEntityStatus(this, (byte) 14);
                }
            }
        });
    }

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
        setHallucinationTarget(!tag.contains(Constants.NBT.HALLUCINATION) ? Optional.empty() : Optional.of(tag.getUuid(Constants.NBT.HALLUCINATION)));
    }

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
        if (getHallucinationTarget().isPresent()) {
            tag.putUuid(Constants.NBT.HALLUCINATION, getHallucinationTarget().get());
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo callbackInfo) {
        dataTracker.startTracking(HALLUCINATION_UUID, Optional.empty());
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void handleAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (getHallucinationTarget().isPresent() && !target.getUuid().equals(getHallucinationTarget().get())) {
            cir.setReturnValue(false);
        }
    }
}
