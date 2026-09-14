package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Reklam kapısı: IAP (Premium) + ödüllü mola + Remote Config.
 */
public final class AdGate {

    private AdGate() {
    }

    /** Banner / geçiş / app-open gösterilebilir mi? */
    public static boolean isAdEnabled(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        if (PremiumManager.getInstance(context).isAdFree()) {
            return false;
        }
        if (AppPrefs.isAdsPaused(context)) {
            return false;
        }
        return AppRemoteConfig.getInstance(context).areAdsEnabled();
    }

    public static boolean isRemoveAdsButtonEnabled(@NonNull Context context) {
        return AppRemoteConfig.getInstance(context).isRemoveAdsEnabled();
    }

    @NonNull
    public static String resolveRewardedAdUnitId(@NonNull Context context) {
        if (BuildConfig.DEBUG) {
            return context.getString(R.string.admob_rewarded_test_id);
        }
        return context.getString(R.string.admob_rewarded_id);
    }

    @NonNull
    public static String resolveAppOpenAdUnitId(@NonNull Context context) {
        if (BuildConfig.DEBUG) {
            return context.getString(R.string.admob_app_open_test_id);
        }
        String id = context.getString(R.string.admob_app_open_id);
        return id == null ? "" : id.trim();
    }
}
