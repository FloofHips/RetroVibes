package com.fruityspikes.retrovibes.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraftforge.client.extensions.IForgeDimensionSpecialEffects;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import java.awt.*;

@Mixin(DimensionSpecialEffects.class)
public class DimensionSpecialEffectsMixin implements IForgeDimensionSpecialEffects {

    @Override
    public void adjustLightmapColors(ClientLevel level, float partialTicks, float skyDarken, float skyLight, float blockLight, int pixelX, int pixelY, Vector3f colors) {
        int r = (int)(colors.x() * 255);
        int g = (int)(colors.y() * 255);
        int b = (int)(colors.z() * 255);

        float[] hsl = rgbToHsl(r, g, b);
        hsl[1] = 0;
        hsl[2] = hsl[2] * 0.8f;

        float[] color = hslToRgb(hsl[0], hsl[1], hsl[2]);

        float x = color[0] / 255.0f;
        float y = color[1] / 255.0f;
        float z = color[2] / 255.0f;

        colors.set(x, y, z);
    }

    private static float[] rgbToHsl(int r, int g, int b) {
        float rf = r / 255.0f;
        float gf = g / 255.0f;
        float bf = b / 255.0f;

        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float h, s, l;
        l = (max + min) / 2.0f;

        if (max == min) {
            h = 0;
            s = 0;
        } else {
            float d = max - min;
            s = l > 0.5f ? d / (2.0f - max - min) : d / (max + min);

            if (max == rf) {
                h = (gf - bf) / d + (gf < bf ? 6 : 0);
            } else if (max == gf) {
                h = (bf - rf) / d + 2;
            } else {
                h = (rf - gf) / d + 4;
            }
            h /= 6.0f;
        }

        return new float[]{h, s, l};
    }

    private static float[] hslToRgb(float h, float s, float l) {
        float r, g, b;

        if (s == 0) {
            r = g = b = l;
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1.0f / 3.0f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1.0f / 3.0f);
        }

        return new float[]{r * 255, g * 255, b * 255};
    }

    private static float hueToRgb(float p, float q, float t) {
        float tVal = t;
        if (tVal < 0) tVal += 1;
        if (tVal > 1) tVal -= 1;
        if (tVal < 1.0f / 6.0f) return p + (q - p) * 6 * tVal;
        if (tVal < 1.0f / 2.0f) return q;
        if (tVal < 2.0f / 3.0f) return p + (q - p) * (2.0f / 3.0f - tVal) * 6;
        return p;
    }
}
