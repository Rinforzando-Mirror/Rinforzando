package adris.rinforzando.mixins;

import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MiningToolItem.class)
public interface MiningToolItemAccessor {

    //#if MC <= 11605
    //$$ @Accessor("effectiveBlocks")
    //$$ Set<Block> getEffectiveBlocks();
    //#endif

}
