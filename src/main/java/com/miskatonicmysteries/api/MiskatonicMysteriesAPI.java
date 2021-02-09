package com.miskatonicmysteries.api;

import com.miskatonicmysteries.api.interfaces.*;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMMiscRegistries;
import com.miskatonicmysteries.common.util.Constants;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class MiskatonicMysteriesAPI {
    public static Affiliation getNonNullAffiliation(Object obj, boolean apparent) {
        return Affiliated.of(obj).map(affiliated -> affiliated.getAffiliation(apparent)).orElse(MMAffiliations.NONE);
    }

    public static Affiliation getApparentAffiliationFromEquipment(@Nullable ItemStack exclude, PlayerEntity player) {
        Inventory trinkets = TrinketsApi.getTrinketsInventory(player);
        for (int i = 0; i < trinkets.size(); i++) {
            ItemStack stack = trinkets.getStack(i);
            if (stack.equals(exclude)) {
                continue;
            }
            if (stack.getItem() instanceof Affiliated) {
                return ((Affiliated) stack.getItem()).getAffiliation(true);
            }
        }
        return MMAffiliations.NONE;
    }

    public static void resetProgress(PlayerEntity player) {
        Sanity.of(player).ifPresent(sanity -> {
            sanity.getSanityCapExpansions().keySet().forEach(sanity::removeSanityCapExpansion);
            sanity.setSanity(sanity.getMaxSanity(), true);
            sanity.setShocked(true);
            sanity.syncSanityData();
        });
        Ascendant.of(player).ifPresent(ascendant -> {
            ascendant.setAscensionStage(0);
            ascendant.getBlessings().clear();
            ascendant.syncBlessingData();
        });
        SpellCaster.of(player).ifPresent(caster -> {
            caster.getSpells().clear();
            caster.getLearnedMediums().clear();
            caster.getLearnedEffects().clear();
            caster.setMaxSpells(Constants.DataTrackers.MIN_SPELLS);
            caster.setPowerPool(0);
            caster.syncSpellData();
        });
        MalleableAffiliated.of(player).ifPresent(malleableAffiliated -> {
            malleableAffiliated.setAffiliation(MMAffiliations.NONE, true);
            malleableAffiliated.setAffiliation(MMAffiliations.NONE, false);
        });
    }

    public static boolean hasBlessing(Ascendant ascendant, Blessing blessing) {
        return ascendant.getBlessings().contains(blessing);
    }

    public static boolean levelUp(PlayerEntity player, int stage, Affiliation affiliation) {
        Optional<Ascendant> ascendant = Ascendant.of(player);
        Optional<MalleableAffiliated> affiliated = MalleableAffiliated.of(player);
        if (ascendant.isPresent() && affiliated.isPresent() && canLevelUp(ascendant.get(), affiliated.get(), stage, affiliation)) {
            ascendant.ifPresent(a -> a.setAscensionStage(stage));
            affiliated.ifPresent(a -> a.setAffiliation(affiliation, false));
            if (player instanceof ServerPlayerEntity) {
                MMMiscRegistries.Criteria.LEVEL_UP.trigger((ServerPlayerEntity) player, affiliation, stage);
            }
            return true;
        }
        return false;
    }

    public static boolean canLevelUp(Ascendant ascendant, Affiliated affiliated, int stage, Affiliation affiliation) {
        if (ascendant.getAscensionStage() < stage - 1 || ascendant.getAscensionStage() >= stage) {
            return false;
        }
        if (affiliated.getAffiliation(false) != affiliation && (stage - 1) > 0) {
            return false;
        }
        return true;
    }

    public static int getAscensionStage(Object object) {
        Optional<Ascendant> ascendant = Ascendant.of(object);
        return ascendant.map(Ascendant::getAscensionStage).orElse(0);
    }

    public static void guaranteeSpellPower(int power, SpellCaster caster) {
        if (caster.getPowerPool() < power) {
            caster.setPowerPool(power);
        }
    }

    public static boolean isDefiniteAffiliated(Object object) {
        return Affiliated.of(object).isPresent() && Affiliated.of(object).get().getAffiliation(false) != MMAffiliations.NONE;
    }

    public static boolean isImmuneToYellowSign(LivingEntity entity) {
        return getNonNullAffiliation(entity, false).equals(MMAffiliations.HASTUR) && getAscensionStage(entity) > HasturAscensionHandler.SIGN_IMMUNITY_STAGE;
    }
}
