package com.miskatonicmysteries.common.feature.spell.effect;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.entity.GenericTentacleEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.FlameEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class FireSpellEffect extends SpellEffect {
    public FireSpellEffect() {
        super(new Identifier(Constants.MOD_ID, "ignite"), null, 0xFF9900);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, @Nullable Vec3d pos, SpellMedium medium, int intensity, @Nullable Entity secondaryMedium, boolean backfires) {
        boolean flag = false;
        if (target != null){
            target.setFireTicks(intensity * 40);
            flag = true;
        }
        if (pos != null && !world.isClient) {
            BlockPos center = new BlockPos(pos);
            int range = Math.min(intensity, 5);
            for (int i = 0; i < (range * 3) + 1; i++) {
                lightFire(world, center, range);
            }

        }
        return flag;
    }

    private boolean lightFire(World world, BlockPos center, int range){
        return BlockPos.Mutable.findClosest(center, range, range, blockPos -> (range >= 2 || world.random.nextBoolean()) && (AbstractFireBlock.method_30032(world, blockPos, Direction.DOWN) || CampfireBlock.method_30035(world.getBlockState(blockPos)))).map(blockPos -> {
            BlockState blockState = world.getBlockState(blockPos);
            if (CampfireBlock.method_30035(blockState)) {
                world.setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
                return true;
            } else {
                if (AbstractFireBlock.method_30032(world, blockPos, Direction.DOWN)) {
                    BlockState blockState2 = AbstractFireBlock.getState(world, blockPos);
                    world.setBlockState(blockPos, blockState2, 11);
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    @Override
    public float getBurnoutMultiplier(int intensity) {
        return 0.9F;
    }
}
