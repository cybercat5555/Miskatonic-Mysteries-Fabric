package com.miskatonicmysteries.common.feature.item;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IncantationYogItem extends Item {
    public IncantationYogItem() {
        super(new Settings().group(Constants.MM_GROUP).maxCount(1));
    }

    public static ItemStack storePosition(ItemStack stack, World world, BlockPos pos) {
        if (pos == null) {
            return stack;
        }
        if (!stack.hasTag()) {
            stack.setTag(new NbtCompound());
        }
        stack.getTag().putLong(Constants.NBT.POSITION, pos.asLong());
        stack.getTag().putString(Constants.NBT.DIMENSION, world.getRegistryKey().getValue().toString());
        return stack;
    }

    public static BlockPos getPosition(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(Constants.NBT.POSITION)) {
            return null;
        }
        return BlockPos.fromLong(stack.getTag().getLong(Constants.NBT.POSITION));
    }

    public static ServerWorld getWorld(ServerWorld world, ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(Constants.NBT.DIMENSION)) {
            return null;
        }
        return world.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(stack.getTag().getString(Constants.NBT.DIMENSION))));
    }

    public static void clear(ItemStack stack) {
        if (stack.hasTag()) {
            stack.getTag().remove(Constants.NBT.POSITION);
            stack.getTag().remove(Constants.NBT.DIMENSION);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        BlockPos boundPos = getPosition(stack);
        if (boundPos != null) {
            tooltip.add(new TranslatableText("tooltip.miskatonicmysteries.bound_to", boundPos.getX(), boundPos.getY(), boundPos.getZ()));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return getPosition(stack) != null ? Rarity.UNCOMMON : super.getRarity(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        BlockPos foundPos = null;
        for (BlockPos iterateOutward : BlockPos.iterateOutwards(user.getBlockPos(), 3, 3, 3)) {
            if (world.getBlockEntity(iterateOutward) instanceof OctagramBlockEntity) {
                foundPos = iterateOutward;
                break;
            }
        }
        if (foundPos != null) {
            world.playSound(user.getX(), user.getY(), user.getZ(), MMSounds.INCANTATION_BIND_SUCCESS, SoundCategory.PLAYERS, 1, 1, false);
        }
        return storePosition(stack, world, foundPos);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }
}
