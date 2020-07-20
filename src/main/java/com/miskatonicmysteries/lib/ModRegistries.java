package com.miskatonicmysteries.lib;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRegistries {
    public static SoundEvent GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "gun_shot"));
    public static void init(){
        Util.register(Registry.SOUND_EVENT, "gun_shot", GUN_SHOT);
    }
}
