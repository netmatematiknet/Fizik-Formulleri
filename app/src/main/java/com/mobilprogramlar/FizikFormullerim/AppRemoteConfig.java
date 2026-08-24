package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Reklam anahtarları Firebase Remote Config'den okunur; konsolda aynı isimlerle tanımlanmalıdır.
 * <ul>
 *   <li>{@link #KEY_ADS_ENABLED} — tüm reklamlar (true/false)</li>
 *   <li>{@link #KEY_BANNER_ENABLED} — banner açık/kapalı</li>
 *   <li>{@link #KEY_INTERSTITIAL_ENABLED} — geçiş reklamı açık/kapalı</li>
 *   <li>{@link #KEY_INTERSTITIAL_MAIN_PERCENT} — ana sayfa geçiş olasılığı 0–100</li>
 *   <li>{@link #KEY_INTERSTITIAL_FORMULA_PERCENT} — formül listesi geçiş olasılığı 0–100</li>
 *   <li>{@link #KEY_BANNER_EVERY_N_ITEMS} — her N kategori kartından sonra 1 banner</li>
 * </ul>
 */
public final class AppRemoteConfig {

    public static final String KEY_ADS_ENABLED = "ads_enabled";
    public static final String KEY_BANNER_ENABLED = "banner_enabled";
    public static final String KEY_INTERSTITIAL_ENABLED = "interstitial_enabled";
    public static final String KEY_INTERSTITIAL_MAIN_PERCENT = "interstitial_main_percent";
    public static final String KEY_INTERSTITIAL_FORMULA_PERCENT = "interstitial_formula_percent";
    public static final String KEY_BANNER_EVERY_N_ITEMS = "banner_every_n_items";

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
        remoteConfig.fetchAndActivate();
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
