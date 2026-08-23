package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.List;

/**
 * Banner + geçiş reklamı. DEBUG'da Google test ID, RELEASE'da gerçek birim ID kullanılır.
 * Uzaktan kapatma: Firebase Remote Config (ads_enabled / banner_enabled / interstitial_enabled).
 */
public final class AdManager {

    private static final InterstitialAdManager interstitialManager = new InterstitialAdManager();
    private static final List<AdView> registeredBanners = new ArrayList<>();

    private AdManager() {
    }

    public static boolean isAdEnabled(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return AppRemoteConfig.getInstance(context).areAdsEnabled();
        } catch (Exception e) {
            Log.w("AdManager", "Remote Config okunamadı, reklam açık kabul edilir.", e);
            return true;
        }
    }

    public static boolean isInterstitialShowing() {
        return interstitialManager.isShowing();
    }

    static String resolveBannerAdUnitId(Context context) {
        if (BuildConfig.DEBUG) {
            return context.getString(R.string.banner_ad_unit_test_id);
        }
        return context.getString(R.string.banner_gecerli_id);
    }

    static String resolveInterstitialAdUnitId(Context context) {
        if (BuildConfig.DEBUG) {
            return context.getString(R.string.interstitial_ad_unit_test_id);
        }
        return context.getString(R.string.interstitial_gecis_gecerli_id);
    }

    public static void loadBannerAd(AdView adView) {
        if (adView == null) {
            return;
        }
        adView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                adView.setVisibility(View.GONE);
                Log.e("AdManager", "Banner yüklenemedi: " + adError.getMessage());
            }
        });
        adView.loadAd(adRequest);
        registeredBanners.add(adView);
    }

    public static void loadAdaptiveBannerAd(Activity activity, FrameLayout container) {
        if (activity == null || container == null) {
            return;
        }
        if (!isAdEnabled(activity) || !AppRemoteConfig.getInstance(activity).areBannersEnabled()) {
            container.setVisibility(View.GONE);
            return;
        }
        if (container.getChildCount() > 0) {
            return;
        }
        container.post(() -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            container.setVisibility(View.VISIBLE);
            int widthPx = container.getWidth();
            if (widthPx <= 0) {
                widthPx = activity.getResources().getDisplayMetrics().widthPixels;
            }
            int adWidthDp = Math.max(320, (int) (widthPx / activity.getResources().getDisplayMetrics().density));
            AdView adView = new AdView(activity);
            adView.setAdUnitId(resolveBannerAdUnitId(activity));
            adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidthDp));
            container.removeAllViews();
            container.addView(adView);
            loadBannerAd(adView);
        });
    }

    public static void loadInterstitialAd(Context context) {
        if (context == null) {
            return;
        }
        interstitialManager.load(context);
    }

    public static void showHomeInterstitialThen(Activity activity, @Nullable Runnable after) {
        int percent = 30;
        try {
            percent = AppRemoteConfig.getInstance(activity).getInterstitialHomePercent();
        } catch (Exception ignored) {
            percent = activity.getResources().getInteger(R.integer.gecis_reklami_olasilik);
        }
        interstitialManager.showThen(activity, percent, after);
    }

    public static void showFormulaInterstitialThen(Activity activity, @Nullable Runnable after) {
        int percent = 20;
        try {
            percent = AppRemoteConfig.getInstance(activity).getInterstitialFormulaPercent();
        } catch (Exception ignored) {
            percent = activity.getResources().getInteger(R.integer.gecis_reklami_olasilik);
        }
        interstitialManager.showThen(activity, percent, after);
    }

    public static void pauseBanners() {
        for (AdView ad : registeredBanners) {
            if (ad != null) {
                try {
                    ad.pause();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static void resumeBanners() {
        for (AdView ad : registeredBanners) {
            if (ad != null) {
                try {
                    ad.resume();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static void destroyBanners() {
        for (AdView ad : registeredBanners) {
            if (ad != null) {
                try {
                    ad.destroy();
                } catch (Exception ignored) {
                }
            }
        }
        registeredBanners.clear();
    }
}
