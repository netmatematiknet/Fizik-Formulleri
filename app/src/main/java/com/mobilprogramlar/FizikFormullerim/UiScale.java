package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * Ekran genişliğine göre tüm dp/sp değerlerini orantılı ölçekler.
 * Kullanıcı fontScale ayarını yok sayar (sığmama olmasın).
 */
public final class UiScale {

    private static final float DESIGN_WIDTH_DP = 360f;
    private static final float MIN = 0.90f;
    private static final float MAX = 1.12f;

    private UiScale() {
    }

    @NonNull
    public static Context wrap(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float widthDp = dm.widthPixels / Math.max(0.01f, dm.density);
        // Tabletlerde aşırı şişirmeyi engelle; sw* + max width yeterli
        if (widthDp >= 600f) {
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.fontScale = 1.0f;
            return context.createConfigurationContext(config);
        }
        float scale = widthDp / DESIGN_WIDTH_DP;
        if (scale < MIN) {
            scale = MIN;
        } else if (scale > MAX) {
            scale = MAX;
        }
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.fontScale = 1.0f;
        config.densityDpi = Math.max(120, Math.round(dm.densityDpi * scale));
        return context.createConfigurationContext(config);
    }

    /** Konu / formül alt başlığı — uzunsa kısalt. */
    @NonNull
    public static String ellipsizeSubtitle(@NonNull String text, int maxChars) {
        String t = text.trim();
        if (t.length() <= maxChars) {
            return t;
        }
        int cut = Math.max(1, maxChars - 1);
        return t.substring(0, cut).trim() + "…";
    }
}
