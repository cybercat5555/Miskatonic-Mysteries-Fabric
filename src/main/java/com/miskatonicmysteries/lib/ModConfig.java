package com.miskatonicmysteries.lib;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name=Constants.MOD_ID)
public class ModConfig {
    public Common commonConfig = new Common();
    public Client clientConfig = new Client();
    public static class Common{
        //haha
    }

    public static class Client{
        @Comment(value = "Determines if shaders are used")
        public boolean useShaders = true;
    }
}
