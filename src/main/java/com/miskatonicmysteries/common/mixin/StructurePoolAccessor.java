package com.miskatonicmysteries.common.mixin;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructurePool.class)
public interface StructurePoolAccessor {//code taken from https://github.com/FoundationGames/Sandwichable/tree/master/remappedSrc/io/github/foundationgames/sandwichable (credit goes to Foundationgames and Draylar)

    @Accessor
    List<StructurePoolElement> getElements();
}
