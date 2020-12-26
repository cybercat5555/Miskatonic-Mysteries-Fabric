package com.miskatonicmysteries.client;

import blue.endless.jankson.Comment;
import com.miskatonicmysteries.common.lib.Constants;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = Constants.MOD_ID + "_client")
public class ClientConfig implements ConfigData {
    @Comment(value = "Determines if the mod uses any shaders")
    public boolean useShaders = true;
}
