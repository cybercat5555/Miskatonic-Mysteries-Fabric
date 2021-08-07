package com.miskatonicmysteries.mixin;

import com.google.common.collect.ImmutableSet;
import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Appeasable;
import com.miskatonicmysteries.api.interfaces.Hallucination;
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
import net.minecraft.nbt.NbtCompound;
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
    @Unique
    private static final Set FORBIDDEN_POTIONS = ImmutableSet.of(Potions.WATER, Potions.EMPTY, Potions.MUNDANE, Potions.AWKWARD, Potions.THICK);

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
                if (!appeasable.isAppeased() && player.getStackInHand(hand).getItem() == Items.POTION && !FORBIDDEN_POTIONS.contains(PotionUtil.getPotion(player.getStackInHand(hand)))) {
                    setStackInHand(Hand.MAIN_HAND, player.getStackInHand(hand).split(1));
                    appeasable.setAppeasedTicks(200 + player.getRandom().nextInt(200));
                    playAmbientSound();
                    player.world.sendEntityStatus(this, (byte) 14);
                    MiskatonicMysteriesAPI.addKnowledge(Constants.Misc.WITCH_KNOWLEDGE, player);
                }
            }
        });
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromTag(NbtCompound tag, CallbackInfo callbackInfo) {
        setHallucinationTarget(!tag.contains(Constants.NBT.HALLUCINATION) ? Optional.empty() : Optional.of(tag.getUuid(Constants.NBT.HALLUCINATION)));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToTag(NbtCompound tag, CallbackInfo callbackInfo) {
        if (getHallucinationTarget().isPresent()) {
            tag.putUuid(Constants.NBT.HALLUCINATION, getHallucinationTarget().get());
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo callbackInfo) {
        dataTracker.startTracking(HALLUCINATION_UUID, Optional.empty());
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void handleAttack(LivingEntity target, CallbackInfo ci) {
        if (getHallucinationTarget().isPresent() && !target.getUuid().equals(getHallucinationTarget().get())) {
            ci.cancel();
        }
    }
}
