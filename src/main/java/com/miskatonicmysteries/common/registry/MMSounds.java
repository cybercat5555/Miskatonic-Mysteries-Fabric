package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.RegistryUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMSounds {
    public static final SoundEvent GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "gun_shot"));
    public static final SoundEvent SCARY_SOUNDS = new SoundEvent(new Identifier(Constants.MOD_ID, "scary"));
    public static final SoundEvent TELEPORT_SOUND = new SoundEvent(new Identifier(Constants.MOD_ID, "teleport"));
    public static final SoundEvent BROKE_VEIL_SPAWN = new SoundEvent(new Identifier(Constants.MOD_ID, "veil_spawn"));
    public static final SoundEvent PRIMED_RITE_TRIGGERED = new SoundEvent(new Identifier(Constants.MOD_ID, "rite_triggered"));
    public static final SoundEvent INCANTATION_BIND_SUCCESS = new SoundEvent(new Identifier(Constants.MOD_ID, "incantation_bound"));
    public static final SoundEvent MAGIC = new SoundEvent(new Identifier(Constants.MOD_ID, "spell_cast"));
    public static final SoundEvent RESONATOR_HUMMING = new SoundEvent(new Identifier(Constants.MOD_ID, "resonator"));
    public static final SoundEvent PLING = new SoundEvent(new Identifier(Constants.MOD_ID, "pling"));

    public static void init() {
        RegistryUtil.register(Registry.SOUND_EVENT, "gun_shot", GUN_SHOT);
        RegistryUtil.register(Registry.SOUND_EVENT, "scary", SCARY_SOUNDS);
        RegistryUtil.register(Registry.SOUND_EVENT, "teleport", TELEPORT_SOUND);
        RegistryUtil.register(Registry.SOUND_EVENT, "veil_spawn", BROKE_VEIL_SPAWN);
        RegistryUtil.register(Registry.SOUND_EVENT, "incantation_bound", INCANTATION_BIND_SUCCESS);
        RegistryUtil.register(Registry.SOUND_EVENT, "rite_triggered", PRIMED_RITE_TRIGGERED);
        RegistryUtil.register(Registry.SOUND_EVENT, "spell_cast", MAGIC);
        RegistryUtil.register(Registry.SOUND_EVENT, "resonator", RESONATOR_HUMMING);
        RegistryUtil.register(Registry.SOUND_EVENT, "pling", PLING);
    }
}
