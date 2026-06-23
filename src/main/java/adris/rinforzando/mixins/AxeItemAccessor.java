package adris.rinforzando.mixins;

import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {

    //#if MC <= 11605
    //$$ @Accessor("field_23139")
    //$$ Set<net.minecraft.block.Material> getEffectiveMaterials();
    //#endif

}
