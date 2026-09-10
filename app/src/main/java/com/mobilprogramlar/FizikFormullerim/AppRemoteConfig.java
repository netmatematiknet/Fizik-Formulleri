package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Firebase Remote Config: reklam + uygulama içi güncelleme metinleri.
 * Konsolda Publish edilmesi gereken güncelleme anahtarları:
 * guncelleme_min_version, guncelleme_zorunlu, guncelleme_baslik, guncelleme_mesaji
 */
public final class AppRemoteConfig {

    public static final String KEY_ADS_ENABLED = "ads_enabled";
    public static final String KEY_BANNER_ENABLED = "banner_enabled";
    public static final String KEY_INTERSTITIAL_ENABLED = "interstitial_enabled";
    public static final String KEY_INTERSTITIAL_MAIN_PERCENT = "interstitial_main_percent";
    public static final String KEY_INTERSTITIAL_FORMULA_PERCENT = "interstitial_formula_percent";
    public static final String KEY_BANNER_EVERY_N_ITEMS = "banner_every_n_items";

    public static final String KEY_UPDATE_MIN_VERSION = "guncelleme_min_version";
    public static final String KEY_UPDATE_FORCE = "guncelleme_zorunlu";
    public static final String KEY_UPDATE_TITLE = "guncelleme_baslik";
    public static final String KEY_UPDATE_MESSAGE = "guncelleme_mesaji";

    private static final long DEFAULT_MAIN = 10L;
    private static final long DEFAULT_FORMULA = 30L;
    private static final long DEFAULT_BANNER_EVERY_N = 2L;

    private static volatile AppRemoteConfig instance;
    private final FirebaseRemoteConfig remoteConfig;

    private AppRemoteConfig(Context appContext) {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        boolean debuggable = (appContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        long minInterval = debuggable ? 0L : 3600L;
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(minInterval)
                .build();
        remoteConfig.setConfigSettingsAsync(settings);
        Map<String, Object> defaults = new HashMap<>();
        defaults.put(KEY_ADS_ENABLED, true);
        defaults.put(KEY_BANNER_ENABLED, true);
        defaults.put(KEY_INTERSTITIAL_ENABLED, true);
        defaults.put(KEY_INTERSTITIAL_MAIN_PERCENT, DEFAULT_MAIN);
        defaults.put(KEY_INTERSTITIAL_FORMULA_PERCENT, DEFAULT_FORMULA);
        defaults.put(KEY_BANNER_EVERY_N_ITEMS, DEFAULT_BANNER_EVERY_N);
        defaults.put(KEY_UPDATE_MIN_VERSION, 0L);
        defaults.put(KEY_UPDATE_FORCE, false);
        defaults.put(KEY_UPDATE_TITLE, "Fizik Formülleri yenilendi!");
        defaults.put(KEY_UPDATE_MESSAGE, "Yeni sürüm hazır. Daha iyi deneyim için hemen güncelleyin.");
        remoteConfig.setDefaultsAsync(defaults);
    }

    public static AppRemoteConfig getInstance(Context context) {
        if (instance == null) {
            synchronized (AppRemoteConfig.class) {
                if (instance == null) {
                    instance = new AppRemoteConfig(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /** Uygulama açılışında bir kez çağrılır. */
    public void fetchAndActivate() {
        fetchAndActivate(null);
    }

    public void fetchAndActivate(@Nullable Runnable onComplete) {
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener((OnCompleteListener<Boolean>) task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        if (e != null) {
                            FirebaseCrashlytics.getInstance().log("RemoteConfig fetch failed: " + e.getMessage());
                        }
                    }
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });
    }

    public boolean areAdsEnabled() {
        return remoteConfig.getBoolean(KEY_ADS_ENABLED);
    }

    public boolean areBannersEnabled() {
        return areAdsEnabled() && remoteConfig.getBoolean(KEY_BANNER_ENABLED);
    }

    public boolean areInterstitialsEnabled() {
        return areAdsEnabled() && remoteConfig.getBoolean(KEY_INTERSTITIAL_ENABLED);
    }

    public int getInterstitialMainPercent() {
        return clampPercent((int) remoteConfig.getLong(KEY_INTERSTITIAL_MAIN_PERCENT));
    }

    public int getInterstitialFormulaPercent() {
        return clampPercent((int) remoteConfig.getLong(KEY_INTERSTITIAL_FORMULA_PERCENT));
    }

    /**
     * Her N içerik kartından sonra 1 banner. 1 = her karttan sonra (mevcut davranışa yakın).
     * 0 veya negatif gelirse banner kapatılır.
     */
    public int getBannerEveryNItems() {
        long n = remoteConfig.getLong(KEY_BANNER_EVERY_N_ITEMS);
        if (n <= 0L) {
            return 0;
        }
        if (n > 20L) {
            return 20;
        }
        return (int) n;
    }

    /** 0 = Remote Config eşiği kapalı. */
    public int getUpdateMinVersionCode() {
        long v = remoteConfig.getLong(KEY_UPDATE_MIN_VERSION);
        if (v < 0L) {
            return 0;
        }
        if (v > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) v;
    }

    public boolean isUpdateForced() {
        return remoteConfig.getBoolean(KEY_UPDATE_FORCE);
    }

    @NonNull
    public String getUpdateTitle(@NonNull Context context) {
        String title = remoteConfig.getString(KEY_UPDATE_TITLE);
        if (TextUtils.isEmpty(title)) {
            return context.getString(R.string.update_default_title);
        }
        return title;
    }

    @NonNull
    public String getUpdateMessage(@NonNull Context context) {
        String message = remoteConfig.getString(KEY_UPDATE_MESSAGE);
        if (TextUtils.isEmpty(message)) {
            return context.getString(R.string.update_default_message);
        }
        return message;
    }

    private static int clampPercent(int value) {
        if (value < 0) {
            return 0;
        }
        if (value > 100) {
            return 100;
        }
        return value;
    }
}
