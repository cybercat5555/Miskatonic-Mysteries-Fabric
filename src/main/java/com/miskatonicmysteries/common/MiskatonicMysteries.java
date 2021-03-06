package com.miskatonicmysteries.common;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.interfaces.MalleableAffiliated;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.interfaces.SpellCaster;
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
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
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

        ServerPlayerEvents.COPY_FROM.register((player, oldPlayer, isDead) -> {
            Sanity.of(oldPlayer).ifPresent(oldSanity -> Sanity.of(player).ifPresent(sanity -> {
                sanity.setSanity(oldSanity.getSanity(), true);
                sanity.getSanityCapExpansions().putAll(oldSanity.getSanityCapExpansions());
                sanity.syncSanityData();
            }));

            SpellCaster.of(oldPlayer).ifPresent(oldCaster -> SpellCaster.of(player).ifPresent(caster -> {
                caster.setMaxSpells(oldCaster.getMaxSpells());
                caster.setPowerPool(oldCaster.getPowerPool());
                caster.getLearnedEffects().addAll(oldCaster.getLearnedEffects());
                caster.getLearnedMediums().addAll(oldCaster.getLearnedMediums());
                caster.getSpells().addAll(oldCaster.getSpells());
                caster.syncSpellData();
            }));

            Ascendant.of(oldPlayer).ifPresent(oldAscendant -> Ascendant.of(player).ifPresent(ascendant -> {
                ascendant.setAscensionStage(oldAscendant.getAscensionStage());
                ascendant.getBlessings().addAll(oldAscendant.getBlessings());
            }));

            MalleableAffiliated.of(oldPlayer).ifPresent(oldAffiliation -> MalleableAffiliated.of(player).ifPresent(affiliation -> {
                affiliation.setAffiliation(oldAffiliation.getAffiliation(false), false);
                affiliation.setAffiliation(oldAffiliation.getAffiliation(true), true);
            }));
        });
    }
}
