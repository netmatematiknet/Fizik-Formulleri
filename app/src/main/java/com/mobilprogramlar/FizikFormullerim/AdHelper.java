package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Banner ve geçiş reklamı işlemleri (NtHelper'dan ayrıldı).
 */
public final class AdHelper {

    private static final InterstitialAdManager interstitialManager = new InterstitialAdManager();
    private static final List<AdView> registeredBannerAds = new ArrayList<>();

    private AdHelper() {
    }

    private static boolean isAdFree(Context context) {
        return PremiumManager.getInstance(context).isAdFree();
    }

    /** Debug → test ID; Release → gerçek ID. */
    static String resolveBannerAdUnitId(Context context) {
        if (BuildConfig.DEBUG) {
            return context.getString(R.string.admob_banner_test_id);
        }
        return context.getString(R.string.admob_banner_id);
    }

    static String resolveInterstitialAdUnitId(Context context) {
        if (BuildConfig.DEBUG) {
            return context.getString(R.string.admob_interstitial_test_id);
        }
        return context.getString(R.string.admob_interstitial_id);
    }

    public static void loadBannerAd(Context context, FrameLayout adContainerView) {
        if (isAdFree(context)) {
            return;
        }
        if (!AppRemoteConfig.getInstance(context).areBannersEnabled()) {
            return;
        }
        if (adContainerView.getChildCount() > 0) {
            return;
        }
        AdView adView = new AdView(context);
        adView.setAdUnitId(resolveBannerAdUnitId(context));
        adView.setAdSize(resolveBannerAdSize(context, adContainerView));
        adContainerView.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
        registeredBannerAds.add(adView);
    }

    private static AdSize resolveBannerAdSize(Context context, FrameLayout adContainerView) {
        if (!(context instanceof Activity)) {
            return AdSize.BANNER;
        }
        Activity activity = (Activity) context;
        float density = activity.getResources().getDisplayMetrics().density;
        int adWidthPx = adContainerView.getWidth();
        if (adWidthPx <= 0) {
            adWidthPx = activity.getResources().getDisplayMetrics().widthPixels;
            int padding = adContainerView.getPaddingLeft() + adContainerView.getPaddingRight();
            adWidthPx = Math.max(0, adWidthPx - padding);
        }
        int adWidthDp = Math.max(320, (int) (adWidthPx / density));
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidthDp);
    }

    public static void pauseBannerAd() {
        for (AdView ad : registeredBannerAds) {
            if (ad != null) {
                ad.pause();
            }
        }
    }

    public static void resumeBannerAd() {
        for (AdView ad : registeredBannerAds) {
            if (ad != null) {
                ad.resume();
            }
        }
    }

    public static void destroyBannerAd() {
        for (AdView ad : registeredBannerAds) {
            if (ad != null) {
                ad.destroy();
            }
        }
        registeredBannerAds.clear();
    }

    public static void loadInterstitialAd(Context context) {
        interstitialManager.load(context);
    }

    public static void showInterstitialIfReady(Activity activity) {
        interstitialManager.showIfReady(activity);
    }

    public static void showAdWithProbability(Activity activity, int probabilityPercentage) {
        interstitialManager.showWithProbability(activity, probabilityPercentage);
    }
}
