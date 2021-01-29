package com.miskatonicmysteries.common.feature.blessing;

import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.interfaces.Affiliated;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMMiscRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public class Blessing implements Affiliated {
    public static final Map<Identifier, Blessing> BLESSINGS = new HashMap<>();
    public static final Blessing CHARMING_PERSONALITY = MMMiscRegistries.addBlessing(new Blessing(new Identifier(Constants.MOD_ID, "charming_personality"), Affiliation.HASTUR));
    public static final Blessing ROYAL_ENTOURAGE = MMMiscRegistries.addBlessing(new RoyalEntourageBlessing());
    public static final Blessing MAGIC_BOOST = MMMiscRegistries.addBlessing(new MagicBoostBlessing());
    public static final Blessing BNUUY = MMMiscRegistries.addBlessing(new Blessing(new Identifier(Constants.MOD_ID, "fuckin_bnuuy"), Affiliation.HASTUR));

    private final Identifier id;
    private final Affiliation affiliation;

    public Blessing(Identifier id, Affiliation affiliation) {
        this.id = id;
        this.affiliation = affiliation;
    }

    public void onAcquired(LivingEntity entity) {
    }

    public void onRemoved(LivingEntity entity) {
    }

    public void tick(LivingEntity entity) {

    }

    //todo rendering overrides once those become relevant (luckily hastur does not have physical mutations)

    public Identifier getId() {
        return id;
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return affiliation;
    }

    @Override
    public boolean isSupernatural() {
        return true;
    }

    public String getTranslationString() {
        return "blessing." + id.toString().replaceAll(":", ".");
    }
}
