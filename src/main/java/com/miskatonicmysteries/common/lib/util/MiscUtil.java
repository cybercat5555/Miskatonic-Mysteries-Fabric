package com.miskatonicmysteries.common.lib.util;

import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class MiscUtil {
    public static boolean isValidYellowSign(ListTag bannerListTag) {
        if (bannerListTag.isEmpty()) return false;
        CompoundTag found = null;
        for (Tag tag : bannerListTag) {
            if (tag instanceof CompoundTag && ((CompoundTag) tag).getString(Constants.NBT.BANNER_PATTERN).equals(Constants.MOD_ID + ":yellow_sign")) {
                found = (CompoundTag) tag;
                break;
            }
        }
        return found != null && DyeColor.byId(found.getInt(Constants.NBT.BANNER_COLOR)) == DyeColor.YELLOW;
    }

    public static boolean isImmuneToYellowSign(LivingEntity entity) {
        return CapabilityUtil.getAffiliation(entity, false) == Affiliation.HASTUR;
    }

    public static boolean isValidYellowSign(CompoundTag compoundTag) {
        return compoundTag != null && compoundTag.contains(Constants.NBT.BANNER_PP_TAG, 9) && MiscUtil.isValidYellowSign(compoundTag.getList(Constants.NBT.BANNER_PP_TAG, 10));
    }

    public static void teleport(ServerWorld world, Entity target, double x, double y, double z, float yaw, float pitch) {
        if (target instanceof ServerPlayerEntity) {
            ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
            world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, target.getEntityId());
            target.stopRiding();
            if (((ServerPlayerEntity) target).isSleeping()) {
                ((ServerPlayerEntity) target).wakeUp(true, true);
            }

            if (world == target.world) {
                ((ServerPlayerEntity) target).networkHandler.teleportRequest(x, y, z, yaw, pitch, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
            } else {
                ((ServerPlayerEntity) target).teleport(world, x, y, z, yaw, pitch);
            }

            target.setHeadYaw(yaw);
        } else {
            float f = MathHelper.wrapDegrees(yaw);
            float g = MathHelper.wrapDegrees(pitch);
            g = MathHelper.clamp(g, -90.0F, 90.0F);
            if (world == target.world) {
                target.refreshPositionAndAngles(x, y, z, f, g);
                target.setHeadYaw(f);
            } else {
                target.detach();
                Entity entity = target;
                target = target.getType().create(world);
                if (target == null) {
                    return;
                }

                target.copyFrom(entity);
                target.refreshPositionAndAngles(x, y, z, f, g);
                target.setHeadYaw(f);
                world.onDimensionChanged(target);
                entity.removed = true;
            }
        }

        if (!(target instanceof LivingEntity) || !((LivingEntity) target).isFallFlying()) {
            target.setVelocity(target.getVelocity().multiply(1.0D, 0.0D, 1.0D));
            target.setOnGround(true);
        }

        if (target instanceof PathAwareEntity) {
            ((PathAwareEntity) target).getNavigation().stop();
        }
    }

    public static LiteralText createPowerPercentageText(double power, double maxPower) {
        StringBuilder output = new StringBuilder("||||||||||||||||||||");
        int grayChar = (int) Math.round(20 * (power / maxPower));
        output.insert(grayChar, Formatting.DARK_GRAY);
        return new LiteralText((Formatting.BLUE + output.toString()));
    }
}
