package com.miskatonicmysteries.common.item.consumable;

import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.mixin.ZombieVillagerAccessor;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ReAgentItem extends Item {
    public ReAgentItem() {
        super(new Item.Settings().group(Constants.MM_GROUP).recipeRemainder(MMObjects.SYRINGE).maxCount(1));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) user, stack);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (entity instanceof ZombieVillagerEntity) {
            if (entity.getRandom().nextBoolean()) {
                ((ZombieVillagerAccessor) entity).callSetConverting(user.getUuid(), 100);
            } else entity.damage(DamageSource.WITHER, 100);
            stack.decrement(1);
            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
