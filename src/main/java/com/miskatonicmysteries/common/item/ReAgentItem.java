package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModObjects;
import com.miskatonicmysteries.mixin.ZombieVillagerMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ReAgentItem extends Item {
    public ReAgentItem() {
        super(new Item.Settings().group(Constants.MM_GROUP).recipeRemainder(ModObjects.SYRINGE).maxCount(1));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof ZombieVillagerEntity) {
            if (entity.getRandom().nextBoolean()) {
                ((ZombieVillagerMixin) entity).callSetConverting(user.getUuid(), 100);
            } else entity.damage(DamageSource.WITHER, 100);
            stack.decrement(1);
            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
