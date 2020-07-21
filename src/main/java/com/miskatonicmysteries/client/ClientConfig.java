package com.miskatonicmysteries.client;

import blue.endless.jankson.Comment;
import com.miskatonicmysteries.lib.Constants;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name = Constants.MOD_ID + "/client")
public class ClientConfig {
    @Comment(value = "Determines if the mod uses any shaders")
    public boolean useShaders = true;
}
