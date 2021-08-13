package com.miskatonicmysteries.common;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.*;
import com.miskatonicmysteries.common.feature.ModCommand;
import com.miskatonicmysteries.common.handler.SchedulingHandler;
import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.handler.networking.packet.c2s.InvokeManiaPacket;
import com.miskatonicmysteries.common.registry.*;
import com.miskatonicmysteries.common.util.Constants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class MiskatonicMysteries implements ModInitializer {
    public static MMConfig config;

    static {
        AutoConfig.register(MMConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(MMConfig.class).getConfig();
    }

    @Override
    public void onInitialize() {
        MMAffiliations.init();
        MMBlessings.init();
        MMObjects.init();
        MMEntities.init();
        MMSpellMediums.init();
        MMSpellEffects.init();
        MMRites.init();
        MMSounds.init();
        MMStatusEffects.init();
        MMLootTables.init();
        MMTrades.init();
        MMCriteria.init();
        MMRecipes.init();
        MMInsanityEvents.init();
        MMParticles.init();
        ModCommand.setup();
        MMWorld.init();
        registerPackets();
        SchedulingHandler.init();
        MMServerEvents.init();
         /*
        todo some natural sanity regen
        */
    }

    private void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(InvokeManiaPacket.ID, InvokeManiaPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SyncSpellCasterDataPacket.ID, SyncSpellCasterDataPacket::handleFromClient);
        ServerPlayNetworking.registerGlobalReceiver(SpellPacket.ID, SpellPacket::handleFromClient);
    }
}
