package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.common.entity.BoltEntity;
import com.miskatonicmysteries.common.entity.SpellProjectileEntity;
import com.miskatonicmysteries.common.lib.MMEntities;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "onEntitySpawn", at = @At(value = "HEAD"), cancellable = true)
    private void spawnModEntities(EntitySpawnS2CPacket packet, CallbackInfo info) {
        Entity entity = null;
        if (packet.getEntityTypeId() == MMEntities.SPELL_PROJECTILE) {
            entity = new SpellProjectileEntity((EntityType<SpellProjectileEntity>) packet.getEntityTypeId(), world);
        } else if (packet.getEntityTypeId() == MMEntities.BOLT) {
            entity = new BoltEntity((EntityType<BoltEntity>) packet.getEntityTypeId(), world);
        }
        if (entity != null) {
            int entityId = packet.getId();
            entity.setVelocity(packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityZ());
            entity.updatePosition(packet.getX(), packet.getY(), packet.getZ());
            entity.updateTrackedPosition(packet.getX(), packet.getY(), packet.getZ());
            entity.pitch = (float) (packet.getPitch() * 360) / 256.0F;
            entity.yaw = (float) (packet.getYaw() * 360) / 256.0F;
            entity.setEntityId(entityId);
            entity.setUuid(packet.getUuid());
            this.world.addEntity(entityId, entity);
            info.cancel();
        }
    }
}
