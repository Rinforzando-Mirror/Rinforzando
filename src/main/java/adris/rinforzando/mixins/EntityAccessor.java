package adris.rinforzando.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    //#if MC <= 12006
    //$$ @Accessor("inNetherPortal")
    //$$ boolean isInNetherPortal();
    //#endif

    @Accessor
    int getPortalCooldown();

    //#if MC <= 11605
    //$$ @Invoker("getLandingPos")
    //$$ BlockPos invokeGetLandingPos();
    //#endif

}
