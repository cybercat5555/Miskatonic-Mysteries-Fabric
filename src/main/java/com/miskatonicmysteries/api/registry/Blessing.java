package com.miskatonicmysteries.api.registry;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class Blessing implements Affiliated {
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
