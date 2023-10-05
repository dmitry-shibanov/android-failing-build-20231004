package nl.fd.data.entity.article;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FontScaleHolder {

   private static Float scalingFactor = 1F;

   @BindingAdapter("scaledFont")
   public static void scaledFont(@NonNull TextView textView, boolean scaledFont) {
      if (scaledFont) {
         textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * scalingFactor / getSystemFontScale(textView.getContext().getContentResolver()));
      }
   }

   public static void setScalingFactor(Float scalingFactor) {
      FontScaleHolder.scalingFactor = scalingFactor;
   }

   public static Float getScalingFactor() {
      return scalingFactor;
   }

   public static Float getSystemFontScale(ContentResolver contentResolver) {
      float systemScale = 1F;
      if (contentResolver != null) {
         try {
            systemScale = Settings.System.getFloat(
                    contentResolver,
                    Settings.System.FONT_SCALE
            );
         } catch (Settings.SettingNotFoundException e) {
            systemScale = 1F;
         }
      }
      return systemScale;
   }
}
