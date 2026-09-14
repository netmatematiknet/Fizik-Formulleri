package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

/** App Open — reklam_acilis_aktif. */
public final class AppOpenAdHelper {

    private static final String TAG = "AppOpenAd";
    private static AppOpenAd appOpenAd;
    private static boolean loading;
    private static boolean showing;
    private static long loadTimeMs;

    private AppOpenAdHelper() {
    }

    public static void load(@Nullable Context context) {
        if (context == null || loading || isAdAvailable()) {
            return;
        }
        if (!AdGate.isAdEnabled(context) || !AppRemoteConfig.getInstance(context).isAppOpenEnabled()) {
            return;
        }
        String unitId = AdGate.resolveAppOpenAdUnitId(context);
        if (unitId.isEmpty()) {
            return;
        }
        loading = true;
        AppOpenAd.load(context.getApplicationContext(), unitId, new AdRequest.Builder().build(),
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                        loading = false;
                        loadTimeMs = System.currentTimeMillis();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        appOpenAd = null;
                        loading = false;
                        Log.w(TAG, "load fail: " + loadAdError.getMessage());
                    }
                });
    }

    public static void showIfReady(@Nullable Activity activity) {
        if (activity == null || activity.isFinishing() || showing) {
            return;
        }
        if (!AdGate.isAdEnabled(activity) || !AppRemoteConfig.getInstance(activity).isAppOpenEnabled()) {
            return;
        }
        if (!isAdAvailable()) {
            load(activity);
            return;
        }
        showing = true;
        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                appOpenAd = null;
                showing = false;
                load(activity);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                appOpenAd = null;
                showing = false;
            }
        });
        appOpenAd.show(activity);
    }

    private static boolean isAdAvailable() {
        return appOpenAd != null && (System.currentTimeMillis() - loadTimeMs) < 4L * 60L * 60L * 1000L;
    }
}
