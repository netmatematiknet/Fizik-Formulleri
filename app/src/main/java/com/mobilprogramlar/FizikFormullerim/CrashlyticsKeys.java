package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * Crash raporlarına dil / tema / premium anahtarları ekler.
 */
public final class CrashlyticsKeys {

    private CrashlyticsKeys() {
    }

    public static void refresh(@NonNull Context context) {
        Context app = context.getApplicationContext();
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        String language = new LocaleManager(app).getLanguage();
        int themeId = new ThemeManager(app).getTheme();
        boolean adFree = PremiumManager.getInstance(app).isAdFree();

        crashlytics.setCustomKey("language", language != null ? language : "unknown");
        crashlytics.setCustomKey("theme_id", themeId);
        crashlytics.setCustomKey("theme_name", themeName(themeId));
        crashlytics.setCustomKey("premium_ad_free", adFree);
        crashlytics.setCustomKey("app_version_name", BuildConfig.VERSION_NAME);
        crashlytics.setCustomKey("app_version_code", BuildConfig.VERSION_CODE);
    }

    @NonNull
    private static String themeName(int themeId) {
        if (themeId == R.style.Theme_Nurullah) {
            return "Nurullah";
        }
        if (themeId == R.style.Theme_Nadiye) {
            return "Nadiye";
        }
        if (themeId == R.style.Theme_Sude) {
            return "Sude";
        }
        if (themeId == R.style.Theme_Kubra) {
            return "Kubra";
        }
        if (themeId == R.style.Theme_Zeynep) {
            return "Zeynep";
        }
        if (themeId == R.style.Theme_Irem) {
            return "Irem";
        }
        if (themeId == R.style.Theme_Ilknur) {
            return "Ilknur";
        }
        if (themeId == R.style.Theme_Rukiye) {
            return "Rukiye";
        }
        if (themeId == R.style.Theme_Hilal) {
            return "Hilal";
        }
        return "unknown";
    }
}
