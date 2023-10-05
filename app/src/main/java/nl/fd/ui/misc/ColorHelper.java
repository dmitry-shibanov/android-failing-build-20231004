package nl.fd.ui.misc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.BindingAdapter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ColorHelper {

    @BindingAdapter("srcCompat")
    public static void setIconVector(AppCompatImageView view, @DrawableRes int iconRes) {
        view.setImageResource(iconRes);
    }

    /**
     * Changes the color of the Navigation Bar to {@code color} (if supported) and the Status Bar
     * to a 10% darker version of {@code color} (if supported). Changes the foreground color of both
     * navigation/status bars too, depending on the lightness of the chosen color
     *
     * @param window System window {@linkplain Activity#getWindow()}
     * @param view   Any view that is a child of the Apps root layout
     * @param color  The color to change the navigation bar to.
     */
    public static void setNavigationBackground(final Window window, final View view, @ColorInt final int color) {
        boolean isLightColor = ColorHelper.isLightColor(color);
        int systemUiVisibilityFlags = view.getSystemUiVisibility();

        log.debug("navBarColor: {} ({}). Flags: {} {}",
                Integer.toHexString(color),
                (isLightColor ? "light" : "dark"),
                ((systemUiVisibilityFlags & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0 ? "LIGHT_NAVIGATION_BAR" : ""),
                ((systemUiVisibilityFlags & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0 ? "LIGHT_STATUS_BAR" : ""));

        // Only color the navigation bar on android versions that can also set it to a light theme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.setNavigationBarColor(color);
            if (isLightColor) {
                systemUiVisibilityFlags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                systemUiVisibilityFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
        }

        if (isLightColor) {
            systemUiVisibilityFlags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            systemUiVisibilityFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        log.debug( "Setting UI flags: {} {}",
                ((systemUiVisibilityFlags & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0 ? "LIGHT_NAVIGATION_BAR" : ""),
                ((systemUiVisibilityFlags & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0 ? "LIGHT_STATUS_BAR" : ""));
        view.setSystemUiVisibility(systemUiVisibilityFlags);
    }

    /**
     * Determines if the given color is 'light' (requires a dark color for better contrast).
     * Uses Lightness (HSL).
     *
     * @param color The color to inspect
     * @return The color has a lightness greater than 65%
     */
    public static boolean isLightColor(@ColorInt int color) {
        float[] hsl = new float[3];
        colorToHSL(color, hsl);
        return hsl[2] > 0.65f;
    }

    /**
     * Make the color appear darker by the given percentage. Modifies the colors Brightness
     * (HSV/HSB)
     *
     * @param color      The color to manipulate
     * @param percentage The percentage to make the color darker
     * @return The darker version of the color
     */
    @ColorInt
    public static int darken(@ColorInt int color, float percentage) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (1.0f - percentage / 100);
        hsv[2] = Math.max(0f, Math.min(hsv[2], 1f));
        return Color.HSVToColor(hsv);
    }

    private static void colorToHSL(@ColorInt int color, @NonNull @Size(3) float[] hsl) {
        final float r = Color.red(color) / 255f;
        final float g = Color.green(color) / 255f;
        final float b = Color.blue(color) / 255f;

        final float max = Math.max(r, Math.max(g, b)), min = Math.min(r, Math.min(g, b));
        hsl[2] = (max + min) / 2;

        if (max == min) {
            hsl[0] = hsl[1] = 0;
        } else {
            float d = max - min;
            //noinspection Range
            hsl[1] = (hsl[2] > 0.5f) ? d / (2 - max - min) : d / (max + min);
            if (max == r)
                hsl[0] = (g - b) / d + (g < b ? 6 : 0);
            else if (max == g)
                hsl[0] = (b - r) / d + 2;
            else if (max == b)
                hsl[0] = (r - g) / d + 4;
            hsl[0] /= 6;
        }
    }

    @BindingAdapter(value = {"borderId", "borderColor", "borderColorId",
            "inlayId", "inlayColor", "inlayColorId"}, requireAll = false)
    public static void coloredBackgroundBorder(View view,
                                               @IdRes Integer borderId, @ColorInt Integer borderColor, @ColorRes Integer borderColorId,
                                               @IdRes Integer inlayId, @ColorInt Integer inlayColor, @ColorRes Integer inlayColorId) {
        Drawable backgroundDrawable = view.getBackground();
        if (backgroundDrawable instanceof LayerDrawable) {
            Context context = view.getContext();
            coloredBorder((LayerDrawable) backgroundDrawable, borderId, pick(context, borderColor, borderColorId), inlayId, pick(context, inlayColor, inlayColorId));
        } else {
            log.warn("background is not a LayerDrawable!");
        }
    }

    @ColorInt
    private static Integer pick(Context context, @ColorInt Integer color, @ColorRes Integer colorId) {
        if (color != null) {
            return color;
        }
        if (colorId != null) {
            return ContextCompat.getColor(context, colorId);
        }
        return null;
    }

    public static LayerDrawable coloredBorder(LayerDrawable layerDrawable,
                                              @IdRes Integer borderId,
                                              @ColorInt Integer borderColor,
                                              @IdRes Integer inlayId,
                                              @ColorInt Integer inlayColor) {
        LayerDrawable mutable = (LayerDrawable) layerDrawable.mutate();

        if (borderId != null && borderColor != null) {
            colorLayer(mutable, borderId, borderColor);
        }
        if (inlayId != null && inlayColor != null) {
            colorLayer(mutable, inlayId, inlayColor);
        }
        return mutable;
    }

    private static void colorLayer(LayerDrawable layerDrawable, int layerId, @ColorInt int color) {
        Drawable layer = layerDrawable.findDrawableByLayerId(layerId);
        if (layer != null) {
            DrawableCompat.setTint(layer, color);
        } else {
            log.warn("Unable to find layer with id {}", layerId);
        }
    }

    @BindingAdapter("color")
    public static void setIconColor(ImageView view, @ColorInt int color) {
        view.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("colorRes")
    public static void setIconColorRes(ImageView view, @ColorRes int colorRes) {
        if (colorRes != 0) {
            int color = ContextCompat.getColor(view.getContext(), colorRes);
            setIconColor(view, color);
        }
    }
}
