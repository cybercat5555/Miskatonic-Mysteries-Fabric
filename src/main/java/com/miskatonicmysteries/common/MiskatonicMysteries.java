package com.miskatonicmysteries.common;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.common.feature.ModCommand;
import com.miskatonicmysteries.common.handler.networking.packet.SpellPacket;
import com.miskatonicmysteries.common.handler.networking.packet.SyncSpellCasterDataPacket;
import com.miskatonicmysteries.common.handler.networking.packet.c2s.InvokeManiaPacket;
import com.miskatonicmysteries.common.registry.*;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.Identifier;

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
        TrinketSlots.addSlot(SlotGroups.HEAD, Slots.MASK, new Identifier("trinkets", "textures/item/empty_trinket_slot_mask.png"));
        TrackedDataHandlerRegistry.register(MiskatonicMysteriesAPI.AFFILIATION_TRACKER);
        ArgumentTypes.register("insanity_event", ModCommand.InsanityEventArgumentType.class, new ConstantArgumentSerializer(IdentifierArgumentType::identifier));
        ModCommand.setup();
        MMWorld.init();

        ServerPlayNetworking.registerGlobalReceiver(InvokeManiaPacket.ID, InvokeManiaPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(SyncSpellCasterDataPacket.ID, SyncSpellCasterDataPacket::handleFromClient);
        ServerPlayNetworking.registerGlobalReceiver(SpellPacket.ID, SpellPacket::handleFromClient);
    }
}
