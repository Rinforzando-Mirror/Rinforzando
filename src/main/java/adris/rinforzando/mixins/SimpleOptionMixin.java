package adris.rinforzando.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> {


    @Shadow
    T value;

    @Inject(method = "setValue",at = @At("HEAD"), cancellable = true)
    public void inject(T value, CallbackInfo ci) {
        if (MinecraftClient.getInstance() == null || MinecraftClient.getInstance().options == null) return;
        if (((Object)this) == MinecraftClient.getInstance().options.getGamma()) {
            this.value = value;
            ci.cancel();
        }
    }

}
