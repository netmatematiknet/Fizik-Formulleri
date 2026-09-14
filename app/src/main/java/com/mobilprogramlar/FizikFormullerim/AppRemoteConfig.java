package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Firebase Remote Config — v6 Türkçe önekler:
 * reklam_, tanitim_, guncelleme_, kutlama_, bilgi_, bakim_, serit_
 */
public final class AppRemoteConfig {

    // —— Reklam (AdMob) ——
    public static final String KEY_REKLAM_AKTIF = "reklam_aktif";
    public static final String KEY_REKLAM_BANNER_AKTIF = "reklam_banner_aktif";
    public static final String KEY_REKLAM_GECIS_AKTIF = "reklam_gecis_aktif";
    public static final String KEY_REKLAM_GECIS_ANA_YUZDE = "reklam_gecis_ana_yuzde";
    public static final String KEY_REKLAM_GECIS_OYUN_YUZDE = "reklam_gecis_oyun_yuzde";
    public static final String KEY_REKLAM_ACILIS_AKTIF = "reklam_acilis_aktif";
    public static final String KEY_REKLAM_ODULLU_AKTIF = "reklam_odullu_aktif";
    public static final String KEY_REKLAM_ODULLU_MOLASI_SAAT = "reklam_odullu_molasi_saat";
    public static final String KEY_REKLAM_BANNER_HER_N = "reklam_banner_her_n";
    public static final String KEY_REKLAM_KALDIR_AKTIF = "reklam_kaldir_aktif";
    public static final String KEY_REKLAM_GECIS_UYGULAMA_YUZDE = "reklam_gecis_uygulama_yuzde";

    // —— Tanıtım (kendi uygulamalar) ——
    public static final String KEY_TANITIM_AKTIF = "tanitim_aktif";
    public static final String KEY_TANITIM_ID = "tanitim_id";
    public static final String KEY_TANITIM_BASLIK = "tanitim_baslik";
    public static final String KEY_TANITIM_MESAJ = "tanitim_mesaj";
    public static final String KEY_TANITIM_BUTON = "tanitim_buton";
    public static final String KEY_TANITIM_URL = "tanitim_url";
    public static final String KEY_TANITIM_RESIM_URL = "tanitim_resim_url";
    public static final String KEY_TANITIM_YER = "tanitim_yer";

    // —— Güncelleme ——
    public static final String KEY_GUNCELLEME_MIN = "guncelleme_min_version";
    public static final String KEY_GUNCELLEME_ZORUNLU = "guncelleme_zorunlu";
    public static final String KEY_GUNCELLEME_BASLIK = "guncelleme_baslik";
    public static final String KEY_GUNCELLEME_MESAJI = "guncelleme_mesaji";

    // —— Kutlama ——
    public static final String KEY_KUTLAMA_AKTIF = "kutlama_aktif";
    public static final String KEY_KUTLAMA_ID = "kutlama_id";
    public static final String KEY_KUTLAMA_BASLIK = "kutlama_baslik";
    public static final String KEY_KUTLAMA_MESAJI = "kutlama_mesaji";
    public static final String KEY_KUTLAMA_BUTON = "kutlama_buton";
    public static final String KEY_KUTLAMA_URL = "kutlama_url";
    public static final String KEY_KUTLAMA_RESIM_URL = "kutlama_resim_url";

    // —— Bilgi ——
    public static final String KEY_BILGI_AKTIF = "bilgi_aktif";
    public static final String KEY_BILGI_ID = "bilgi_id";
    public static final String KEY_BILGI_BASLIK = "bilgi_baslik";
    public static final String KEY_BILGI_MESAJ = "bilgi_mesaj";
    public static final String KEY_BILGI_BUTON = "bilgi_buton";
    public static final String KEY_BILGI_URL = "bilgi_url";
    public static final String KEY_BILGI_RESIM_URL = "bilgi_resim_url";

    // —— Bakım ——
    public static final String KEY_BAKIM_AKTIF = "bakim_aktif";
    public static final String KEY_BAKIM_BASLIK = "bakim_baslik";
    public static final String KEY_BAKIM_MESAJ = "bakim_mesaj";

    // —— Şerit ——
    public static final String KEY_SERIT_AKTIF = "serit_aktif";
    public static final String KEY_SERIT_METIN = "serit_metin";
    public static final String KEY_SERIT_URL = "serit_url";
    public static final String KEY_SERIT_RESIM_URL = "serit_resim_url";

    // —— Gizlilik ——
    public static final String KEY_GIZLILIK_URL = "gizlilik_url";

    public static final String YER_ANA = "ana";
    public static final String YER_AYARLAR = "ayarlar";
    public static final String YER_SONUC = "sonuc";

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
        remoteConfig.setDefaultsAsync(buildDefaults());
    }

    private static Map<String, Object> buildDefaults() {
        Map<String, Object> d = new HashMap<>();
        d.put(KEY_REKLAM_AKTIF, true);
        d.put(KEY_REKLAM_BANNER_AKTIF, true);
        d.put(KEY_REKLAM_GECIS_AKTIF, true);
        d.put(KEY_REKLAM_GECIS_ANA_YUZDE, 50L);
        d.put(KEY_REKLAM_GECIS_OYUN_YUZDE, 30L);
        d.put(KEY_REKLAM_ACILIS_AKTIF, false);
        d.put(KEY_REKLAM_ODULLU_AKTIF, true);
        d.put(KEY_REKLAM_ODULLU_MOLASI_SAAT, 12L);
        d.put(KEY_REKLAM_BANNER_HER_N, 2L);
        d.put(KEY_REKLAM_KALDIR_AKTIF, true);
        d.put(KEY_REKLAM_GECIS_UYGULAMA_YUZDE, 20L);

        d.put(KEY_TANITIM_AKTIF, false);
        d.put(KEY_TANITIM_ID, "");
        d.put(KEY_TANITIM_BASLIK, "");
        d.put(KEY_TANITIM_MESAJ, "");
        d.put(KEY_TANITIM_BUTON, "");
        d.put(KEY_TANITIM_URL, "");
        d.put(KEY_TANITIM_RESIM_URL, "");
        d.put(KEY_TANITIM_YER, YER_ANA);

        d.put(KEY_GUNCELLEME_MIN, 0L);
        d.put(KEY_GUNCELLEME_ZORUNLU, false);
        d.put(KEY_GUNCELLEME_BASLIK, "");
        d.put(KEY_GUNCELLEME_MESAJI, "");

        d.put(KEY_KUTLAMA_AKTIF, false);
        d.put(KEY_KUTLAMA_ID, "");
        d.put(KEY_KUTLAMA_BASLIK, "");
        d.put(KEY_KUTLAMA_MESAJI, "");
        d.put(KEY_KUTLAMA_BUTON, "");
        d.put(KEY_KUTLAMA_URL, "");
        d.put(KEY_KUTLAMA_RESIM_URL, "");

        d.put(KEY_BILGI_AKTIF, false);
        d.put(KEY_BILGI_ID, "");
        d.put(KEY_BILGI_BASLIK, "");
        d.put(KEY_BILGI_MESAJ, "");
        d.put(KEY_BILGI_BUTON, "");
        d.put(KEY_BILGI_URL, "");
        d.put(KEY_BILGI_RESIM_URL, "");

        d.put(KEY_BAKIM_AKTIF, false);
        d.put(KEY_BAKIM_BASLIK, "");
        d.put(KEY_BAKIM_MESAJ, "");

        d.put(KEY_SERIT_AKTIF, false);
        d.put(KEY_SERIT_METIN, "");
        d.put(KEY_SERIT_URL, "");
        d.put(KEY_SERIT_RESIM_URL, "");
        d.put(KEY_GIZLILIK_URL, "http://www.mobilprogramlar.com/sitemiz-ve-uygulamamiz-icin-gizlilik-politikasi/");
        return d;
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

    public void fetchAndActivate(@Nullable OnCompleteListener<Boolean> listener) {
        remoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (listener != null) {
                listener.onComplete(task);
            }
        });
    }

    // —— Reklam ——
    public boolean areAdsEnabled() {
        return remoteConfig.getBoolean(KEY_REKLAM_AKTIF);
    }

    public boolean areBannersEnabled() {
        return areAdsEnabled() && remoteConfig.getBoolean(KEY_REKLAM_BANNER_AKTIF);
    }

    public boolean areInterstitialsEnabled() {
        return areAdsEnabled() && remoteConfig.getBoolean(KEY_REKLAM_GECIS_AKTIF);
    }

    public boolean isAppOpenEnabled() {
        return areAdsEnabled() && remoteConfig.getBoolean(KEY_REKLAM_ACILIS_AKTIF);
    }

    public boolean isRewardedEnabled() {
        return areAdsEnabled() && remoteConfig.getBoolean(KEY_REKLAM_ODULLU_AKTIF);
    }

    /** Kalıcı reklam kaldırma (IAP) butonu. */
    public boolean isRemoveAdsEnabled() {
        return remoteConfig.getBoolean(KEY_REKLAM_KALDIR_AKTIF);
    }

    /** Ödül sonrası reklamsız süre (saat). En az 1. */
    public int getRewardedPauseHours() {
        int h = (int) remoteConfig.getLong(KEY_REKLAM_ODULLU_MOLASI_SAAT);
        return Math.max(1, h);
    }

    public int getInterstitialMainPercent() {
        return clampPercent((int) remoteConfig.getLong(KEY_REKLAM_GECIS_ANA_YUZDE));
    }

    public int getInterstitialGamePercent() {
        return clampPercent((int) remoteConfig.getLong(KEY_REKLAM_GECIS_OYUN_YUZDE));
    }

    /** Fizik: formül listesi = reklam_gecis_oyun_yuzde. */
    public int getInterstitialFormulaPercent() {
        return getInterstitialGamePercent();
    }

    public int getInterstitialAppsPercent() {
        return clampPercent((int) remoteConfig.getLong(KEY_REKLAM_GECIS_UYGULAMA_YUZDE));
    }

    /** 0 = banner listede kapalı. */
    public int getBannerEveryN() {
        long n = remoteConfig.getLong(KEY_REKLAM_BANNER_HER_N);
        if (n <= 0L) {
            return 0;
        }
        if (n > 20L) {
            return 20;
        }
        return (int) n;
    }

    public int getBannerEveryNItems() {
        return getBannerEveryN();
    }

    // —— Güncelleme ——
    public int getUpdateMinVersionCode() {
        return (int) remoteConfig.getLong(KEY_GUNCELLEME_MIN);
    }

    public boolean isUpdateForced() {
        return remoteConfig.getBoolean(KEY_GUNCELLEME_ZORUNLU);
    }

    public String getUpdateTitle() {
        return remoteConfig.getString(KEY_GUNCELLEME_BASLIK);
    }

    public String getUpdateMessage() {
        return remoteConfig.getString(KEY_GUNCELLEME_MESAJI);
    }

    // —— Kutlama ——
    public boolean isAnnouncementEnabled() {
        return remoteConfig.getBoolean(KEY_KUTLAMA_AKTIF);
    }

    public String getAnnouncementId() {
        return remoteConfig.getString(KEY_KUTLAMA_ID);
    }

    public String getAnnouncementTitle() {
        return remoteConfig.getString(KEY_KUTLAMA_BASLIK);
    }

    public String getAnnouncementMessage() {
        return remoteConfig.getString(KEY_KUTLAMA_MESAJI);
    }

    public String getAnnouncementButton() {
        return remoteConfig.getString(KEY_KUTLAMA_BUTON);
    }

    public String getAnnouncementUrl() {
        return remoteConfig.getString(KEY_KUTLAMA_URL);
    }

    public String getAnnouncementImageUrl() {
        return remoteConfig.getString(KEY_KUTLAMA_RESIM_URL);
    }

    // —— Bilgi ——
    public boolean isInfoEnabled() {
        return remoteConfig.getBoolean(KEY_BILGI_AKTIF);
    }

    public String getInfoId() {
        return remoteConfig.getString(KEY_BILGI_ID);
    }

    public String getInfoTitle() {
        return remoteConfig.getString(KEY_BILGI_BASLIK);
    }

    public String getInfoMessage() {
        return remoteConfig.getString(KEY_BILGI_MESAJ);
    }

    public String getInfoButton() {
        return remoteConfig.getString(KEY_BILGI_BUTON);
    }

    public String getInfoUrl() {
        return remoteConfig.getString(KEY_BILGI_URL);
    }

    public String getInfoImageUrl() {
        return remoteConfig.getString(KEY_BILGI_RESIM_URL);
    }

    // —— Bakım ——
    public boolean isMaintenanceEnabled() {
        return remoteConfig.getBoolean(KEY_BAKIM_AKTIF);
    }

    public String getMaintenanceTitle() {
        return remoteConfig.getString(KEY_BAKIM_BASLIK);
    }

    public String getMaintenanceMessage() {
        return remoteConfig.getString(KEY_BAKIM_MESAJ);
    }

    // —— Şerit ——
    public boolean isStripEnabled() {
        return remoteConfig.getBoolean(KEY_SERIT_AKTIF);
    }

    public String getStripText() {
        return remoteConfig.getString(KEY_SERIT_METIN);
    }

    public String getStripUrl() {
        return remoteConfig.getString(KEY_SERIT_URL);
    }

    public String getStripImageUrl() {
        return remoteConfig.getString(KEY_SERIT_RESIM_URL);
    }

    @NonNull
    public String getPrivacyUrl(@NonNull Context context) {
        String url = remoteConfig.getString(KEY_GIZLILIK_URL);
        if (url == null || url.trim().isEmpty()) {
            return context.getString(R.string.privacy_policy_url);
        }
        return url.trim();
    }

    // —— Tanıtım ——
    public boolean isPromoEnabled() {
        return remoteConfig.getBoolean(KEY_TANITIM_AKTIF);
    }

    public String getPromoId() {
        return remoteConfig.getString(KEY_TANITIM_ID);
    }

    public String getPromoTitle() {
        return remoteConfig.getString(KEY_TANITIM_BASLIK);
    }

    public String getPromoMessage() {
        return remoteConfig.getString(KEY_TANITIM_MESAJ);
    }

    public String getPromoButton() {
        return remoteConfig.getString(KEY_TANITIM_BUTON);
    }

    public String getPromoUrl() {
        return remoteConfig.getString(KEY_TANITIM_URL);
    }

    public String getPromoImageUrl() {
        return remoteConfig.getString(KEY_TANITIM_RESIM_URL);
    }

    public String getPromoPlace() {
        String yer = remoteConfig.getString(KEY_TANITIM_YER);
        if (yer == null || yer.trim().isEmpty()) {
            return YER_ANA;
        }
        return yer.trim().toLowerCase();
    }

    public boolean isPromoPlace(String place) {
        return getPromoPlace().equalsIgnoreCase(place);
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
