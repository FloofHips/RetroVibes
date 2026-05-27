package com.fruityspikes.retrovibes.mixin;

import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin implements AutoCloseable {

    @Shadow
    private boolean updateLightTexture;

    @Shadow
    private float blockLightRedFlicker;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        ci.cancel();
        this.blockLightRedFlicker = this.blockLightRedFlicker + 0;
        this.blockLightRedFlicker *= 0.9F;
        this.updateLightTexture = true;
    }
}
