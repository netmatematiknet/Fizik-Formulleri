package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Reklam anahtarları ve duyuru metinleri Firebase Remote Config'den okunur.
 * Konsolda aynı isimlerle tanımlanmalıdır.
 */
public final class AppRemoteConfig {

    public static final String KEY_ADS_ENABLED = "ads_enabled";
    public static final String KEY_BANNER_ENABLED = "banner_enabled";
    public static final String KEY_INTERSTITIAL_ENABLED = "interstitial_enabled";
    public static final String KEY_INTERSTITIAL_HOME_PERCENT = "interstitial_home_percent";
    public static final String KEY_INTERSTITIAL_FORMULA_PERCENT = "interstitial_formula_percent";
    public static final String KEY_BANNER_EVERY_N_ITEMS = "banner_every_n_items";
    public static final String KEY_HOME_MESSAGE = "home_message";
    public static final String KEY_ANNOUNCEMENT_MESSAGE = "announcement_message";

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
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        Map<String, Object> defaults = new HashMap<>();
        defaults.put(KEY_ADS_ENABLED, true);
        defaults.put(KEY_BANNER_ENABLED, true);
        defaults.put(KEY_INTERSTITIAL_ENABLED, true);
        defaults.put(KEY_INTERSTITIAL_HOME_PERCENT, 30L);
        defaults.put(KEY_INTERSTITIAL_FORMULA_PERCENT, 20L);
        defaults.put(KEY_BANNER_EVERY_N_ITEMS, 2L);
        defaults.put(KEY_HOME_MESSAGE, "");
        defaults.put(KEY_ANNOUNCEMENT_MESSAGE, "");
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

    public int getInterstitialHomePercent() {
        return clampPercent((int) remoteConfig.getLong(KEY_INTERSTITIAL_HOME_PERCENT));
    }

    public int getInterstitialFormulaPercent() {
        return clampPercent((int) remoteConfig.getLong(KEY_INTERSTITIAL_FORMULA_PERCENT));
    }

    public int getBannerEveryNItems() {
        int value = (int) remoteConfig.getLong(KEY_BANNER_EVERY_N_ITEMS);
        return Math.max(1, value);
    }

    public String getHomeMessage() {
        String value = remoteConfig.getString(KEY_HOME_MESSAGE);
        return value == null ? "" : value.trim();
    }

    public String getAnnouncementMessage() {
        String value = remoteConfig.getString(KEY_ANNOUNCEMENT_MESSAGE);
        return value == null ? "" : value.trim();
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
