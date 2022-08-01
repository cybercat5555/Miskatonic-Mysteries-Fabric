package com.miskatonicmysteries.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public class Util {

	public static boolean isValidYellowSign(NbtList bannerListTag) {
		if (bannerListTag.isEmpty()) {
			return false;
		}
		NbtCompound found = null;
		for (NbtElement tag : bannerListTag) {
			if (tag instanceof NbtCompound && ((NbtCompound) tag).getString(Constants.NBT.BANNER_PATTERN)
				.equals(Constants.MOD_ID + ":yellow_sign")) {
				found = (NbtCompound) tag;
				break;
			}
		}
		return found != null && DyeColor.byId(found.getInt(Constants.NBT.BANNER_COLOR)) == DyeColor.YELLOW;
	}

	public static void teleport(ServerWorld world, Entity target, double x, double y, double z, float yaw, float pitch) {
		if (target instanceof ServerPlayerEntity) {
			ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
			world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, target.getId());
			target.stopRiding();
			if (((ServerPlayerEntity) target).isSleeping()) {
				((ServerPlayerEntity) target).wakeUp(true, true);
			}

			if (world == target.world) {
				((ServerPlayerEntity) target).networkHandler
					.requestTeleport(x, y, z, yaw, pitch, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
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
				entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
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

	@Nullable
	public static BlockPos getPossibleMobSpawnPos(ServerWorld world, LivingEntity player, int tries, int radius, int zoneRadius,
												  EntityType<?> type) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int j = 0; j < tries; ++j) {
			float f = world.random.nextFloat() * 2 * (float) Math.PI;
			int k = (int) player.getX() + MathHelper.floor(MathHelper.cos(f) * radius) + world.random.nextInt(zoneRadius);
			int l = (int) player.getZ() + MathHelper.floor(MathHelper.sin(f) * radius) + world.random.nextInt(zoneRadius);
			int m = world.getTopY(Heightmap.Type.WORLD_SURFACE, k, l);
			mutable.set(k, m, l);
			if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, mutable, type)) {
				return mutable;
			}
		}
		return null;
	}

	public static Vec3d getYawRelativePos(Vec3d originPos, double distance, float yaw, float pitch) {
		float yawRadians = yaw * 0.017453292F;
		float pitchRadians = pitch * 0.017453292F;
		double x = distance * (-MathHelper.sin(yawRadians) * MathHelper.cos(pitchRadians));
		double y = distance * -MathHelper.sin(pitchRadians);
		double z = distance * (MathHelper.cos(yawRadians) * MathHelper.cos(pitchRadians));
		return originPos.add(x, y, z);
	}

	public static Vec3d getYawRelativePosRelatively(double distance, float yaw, float pitch) {
		float yawRadians = yaw * 0.017453292F;
		float pitchRadians = pitch * 0.017453292F;
		double x = distance * (-MathHelper.sin(yawRadians) * MathHelper.cos(pitchRadians));
		double y = distance * -MathHelper.sin(pitchRadians);
		double z = distance * (MathHelper.cos(yawRadians) * MathHelper.cos(pitchRadians));
		return new Vec3d(x, y, z);
	}

	public static int getSlotWithStack(Inventory inventory, ItemStack stack) {
		for (int i = 0; i < inventory.size(); ++i) {
			if (!inventory.getStack(i).isEmpty() && ItemStack.areItemsEqual(stack, inventory.getStack(i))) {
				return i;
			}
		}
		return -1;
	}


	public static final Pattern STANDARD_TRIMMING_REGEX = Pattern.compile("(.{1,32}(?:\\s|$))|(.{0,32})", Pattern.DOTALL);

	@NotNull
	public static List<Text> trimText(String text, Pattern regex) {
		List<Text> matchList = new ArrayList<>();
		Matcher regexMatcher = regex.matcher(text);
		while (regexMatcher.find()) {
			String group = regexMatcher.group();
			if (!group.isBlank()) {
				matchList.add(Text.of(group));
			}
		}
		return matchList;
	}

	@NotNull
	public static List<Text> trimText(String text) {
		return trimText(text, STANDARD_TRIMMING_REGEX);
	}
}
