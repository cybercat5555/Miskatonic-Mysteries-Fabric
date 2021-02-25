package com.miskatonicmysteries.common.handler.networking;

import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.handler.networking.packet.c2s.InvokeManiaPacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketHandler {
    public static void registerC2S() {
        ServerPlayNetworking.registerGlobalReceiver(InvokeManiaPacket.ID, InvokeManiaPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SyncSpellCasterDataPacket.ID, SyncSpellCasterDataPacket::handleFromClient);
        ServerPlayNetworking.registerGlobalReceiver(SpellPacket.ID, SpellPacket::handleFromClient);
    }

    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(ExpandSanityPacket.ID, ExpandSanityPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(RemoveExpansionPacket.ID, RemoveExpansionPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SpellPacket.ID, SpellPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(MobSpellPacket.ID, MobSpellPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(InsanityEventPacket.ID, InsanityEventPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(ProtagonistParticlePacket.ID, ProtagonistParticlePacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(EffectParticlePacket.ID, EffectParticlePacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(BloodParticlePacket.ID, BloodParticlePacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncSpellCasterDataPacket.ID, SyncSpellCasterDataPacket::handleFromServer);
        ClientPlayNetworking.registerGlobalReceiver(OpenSpellEditorPacket.ID, OpenSpellEditorPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(TeleportEffectPacket.ID, TeleportEffectPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncBlessingsPacket.ID, SyncBlessingsPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(ModifyBlessingPacket.ID, ModifyBlessingPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SoundPacket.ID, SoundPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncRiteTargetPacket.ID, SyncRiteTargetPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncHeldEntityPacket.ID, SyncHeldEntityPacket::handle);
    }

    public static void sendToPlayer(PlayerEntity player, PacketByteBuf data, Identifier packet) {
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).networkHandler != null)
            ServerPlayNetworking.send((ServerPlayerEntity) player, packet, data);
    }

    public static void sendToPlayers(World world, BlockPos trackPos, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            PlayerLookup.tracking((ServerWorld) world, trackPos).forEach(p -> sendToPlayer(p, data, packet));
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }

    public static void sendToPlayers(World world, Entity trackingEntity, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            PlayerLookup.tracking(trackingEntity).forEach(p -> sendToPlayer(p, data, packet));
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }

    public static void sendToPlayers(World world, BlockEntity trackingBlockEntity, PacketByteBuf data, Identifier packet) {
        if (world instanceof ServerWorld) {
            PlayerLookup.tracking(trackingBlockEntity).forEach(p -> sendToPlayer(p, data, packet));
            world.getPlayers().forEach(p -> sendToPlayer(p, data, packet));
        }
    }
}
