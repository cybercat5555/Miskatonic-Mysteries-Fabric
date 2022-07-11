package com.miskatonicmysteries.common.handler.networking.packet.s2c;

import com.miskatonicmysteries.common.feature.entity.SpellProjectileEntity;
import com.miskatonicmysteries.common.feature.entity.util.CastingMob;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import io.netty.buffer.Unpooled;

public class ProjectileSpellEffectPacket {

	public static final Identifier ID = new Identifier(Constants.MOD_ID, "projectile_spell");

	public static <T extends PathAwareEntity & CastingMob> void send(SpellProjectileEntity projectile, Entity entity, Vec3d pos) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeInt(projectile.getId());
		data.writeInt(entity == null ? -1 : entity.getId());
		data.writeDouble(pos.x);
		data.writeDouble(pos.y);
		data.writeDouble(pos.z);
		if (projectile.world instanceof ServerWorld) {
			PlayerLookup.tracking(projectile).forEach(p -> ServerPlayNetworking.send(p, ID, data));
		}
	}

	@Environment(EnvType.CLIENT)
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf,
							  PacketSender sender) {
		if (client.world != null && client.world.getEntityById(packetByteBuf.readInt()) instanceof SpellProjectileEntity p) {
			int id = packetByteBuf.readInt();
			Entity target = id == -1 ? null : client.world.getEntityById(id);
			Vec3d pos = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
			client.execute(() -> {
				p.getSpell().effect(p.world, (LivingEntity) p.getOwner(), target, pos,
									MMSpellMediums.PROJECTILE, p.getIntensity(), p);
			});
		}
	}
}
