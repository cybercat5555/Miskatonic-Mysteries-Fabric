package com.miskatonicmysteries.common.item;

import com.miskatonicmysteries.lib.Constants;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ItemRifle extends Item {
    public ItemRifle() {
        super(new Item.Settings().group(Constants.MM_GROUP).maxCount(1));
        //ModelPredicateProviderRegistry
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (isLoaded(stack)){
            shoot(world, user, stack);
            return TypedActionResult.consume(stack);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        loadRifle(stack, world, user);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

/*    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return
    }
*/
    private ItemStack loadRifle(ItemStack stack, World world, LivingEntity user) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        //todo consume stuff, loading sounds too
        stack.getTag().putBoolean(Constants.NBT.LOADED, true);
        return stack;
    }

    private void shoot(World world, PlayerEntity player, ItemStack stack) {
        if (!world.isClient) {
            ArrowItem arrowItem = (ArrowItem) Items.ARROW; //for now arrows, later bullets
            PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, stack, player);
            persistentProjectileEntity.setProperties(player, player.pitch, player.yaw, 0.0F, 3.0F, 1.0F);
            persistentProjectileEntity.setCritical(true);

            int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (j > 0) {
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double)j * 0.5D + 0.5D);
            }

            int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
            if (k > 0) {
                persistentProjectileEntity.setPunch(k);
            }

            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                persistentProjectileEntity.setOnFireFor(100);
            }


            stack.getTag().putBoolean(Constants.NBT.LOADED, false);

            world.spawnEntity(persistentProjectileEntity);
        }
         //todo actual sounds
        world.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F));

        player.getItemCooldownManager().set(this, 20);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    public static boolean isLoaded(ItemStack stack){
        return stack.hasTag() && stack.getTag().getBoolean(Constants.NBT.LOADED);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW; //all that should change, will probably do animations
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }
}
