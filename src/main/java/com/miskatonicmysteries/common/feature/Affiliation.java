package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static com.miskatonicmysteries.common.lib.Constants.MOD_ID;

public class Affiliation {
    public static final Map<Identifier, Affiliation> AFFILIATION_MAP = new HashMap<>();
    public static final Affiliation NONE = new Affiliation(new Identifier(MOD_ID, "none"), new float[]{0, 0, 0});
    public static final Affiliation HASTUR = new Affiliation(new Identifier(MOD_ID, "hastur"), new float[]{1, 1, 0});
    public static final Affiliation SHUB = new Affiliation(new Identifier(MOD_ID, "shub"), new float[]{0.5F, 0.456F, 0.357F});
    public static final Affiliation CTHULHU = new Affiliation(new Identifier(MOD_ID, "cthulhu"), new float[]{0.2F, 0.27F, 0.44F});

    protected Identifier id;
    protected float[] color;

    public Affiliation(Identifier id, float[] color) {
        this.id = id;
        this.color = color;
        AFFILIATION_MAP.put(id, this);
    }

    public CompoundTag toTag(CompoundTag tag) {
        tag.putString(Constants.NBT.AFFILIATION, id.toString());
        return tag;
    }

    public static Affiliation fromTag(CompoundTag tag) {
        return AFFILIATION_MAP.getOrDefault(new Identifier(tag.getString(Constants.NBT.AFFILIATION)), null);
    }


    public Identifier getId() {
        return id;
    }

    public float[] getColor() {
        return color;
    }

    public int getIntColor() {
        int red = ((int) (color[0] * 255) << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        int green = ((int) (color[1] * 255) << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        int blue = (int) (color[2] * 255) & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | red | green | blue;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
