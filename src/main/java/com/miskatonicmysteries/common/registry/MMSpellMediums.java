package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.spell.medium.*;
import net.minecraft.util.registry.Registry;

public class MMSpellMediums {
    public static final SpellMedium SELF = new SelfSpellMedium();
    public static final SpellMedium VISION = new VisionSpellMedium();
    public static final SpellMedium BOLT = new BoltSpellMedium();
    public static final SpellMedium GROUP = new GroupSpellMedium();
    public static final SpellMedium MOB_TARGET = new MobTargetMedium();
    public static final SpellMedium PROJECTILE = new ProjectileSpellMedium();

    public static void init() {
        register(SELF);
        register(VISION);
        register(BOLT);
        register(GROUP);
        register(MOB_TARGET);
        register(PROJECTILE);
    }

    private static void register(SpellMedium medium) {
        Registry.register(MMRegistries.SPELL_MEDIUMS, medium.getId(), medium);
    }
}
