package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Random;

public final class InterstitialAdManager {

    private static final String TAG = "InterstitialAdMgr";
    private static final String PREFS_NAME = "AdPreferences";
    private static final String LAST_INTERSTITIAL_TIME = "last_interstitial_time";
    private static final long INTERSTITIAL_COOLDOWN_MS = 60_000L;

    private InterstitialAd interstitialAd;
    private boolean loading;
    private boolean showing;

    public boolean isShowing() {
        return showing;
    }

    public void load(@NonNull Context context) {
        if (!AdManager.isAdEnabled(context)) {
            return;
        }
        if (!AppRemoteConfig.getInstance(context).areInterstitialsEnabled()) {
            return;
        }
        if (loading || interstitialAd != null) {
            return;
        }
        loading = true;
        String adUnitId = AdManager.resolveInterstitialAdUnitId(context);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context.getApplicationContext(), adUnitId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        loading = false;
                        interstitialAd = ad;
                        Log.d(TAG, "Geçiş reklamı yüklendi.");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        loading = false;
                        interstitialAd = null;
                        Log.d(TAG, "Reklam yüklenemedi: " + adError.getMessage());
                    }
                });
    }

    public void showThen(@NonNull Activity activity, int probabilityPercent, @Nullable Runnable after) {
        Runnable safeAfter = () -> {
            if (after != null && !activity.isFinishing()) {
                after.run();
            }
        };

        if (activity.isFinishing() || !AdManager.isAdEnabled(activity)) {
            safeAfter.run();
            return;
        }
        if (!AppRemoteConfig.getInstance(activity).areInterstitialsEnabled()) {
            safeAfter.run();
            return;
        }
        if (showing) {
            safeAfter.run();
            return;
        }
        if (!isCooldownElapsed(activity)) {
            Log.d(TAG, "Geçiş reklamı atlandı: cooldown.");
            safeAfter.run();
            return;
        }
        if (probabilityPercent <= 0) {
            safeAfter.run();
            return;
        }
        if (probabilityPercent > 100) {
            probabilityPercent = 100;
        }
        if (new Random().nextInt(100) >= probabilityPercent) {
            Log.d(TAG, "Geçiş reklamı olasılık nedeniyle gösterilmedi.");
            safeAfter.run();
            return;
        }
        if (interstitialAd == null) {
            load(activity);
            safeAfter.run();
            return;
        }

        final InterstitialAd ad = interstitialAd;
        interstitialAd = null;
        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                showing = true;
                markInterstitialShown(activity);
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                showing = false;
                ad.setFullScreenContentCallback(null);
                load(activity);
                safeAfter.run();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                showing = false;
                ad.setFullScreenContentCallback(null);
                load(activity);
                safeAfter.run();
            }
        });
        ad.show(activity);
    }

    private static boolean isCooldownElapsed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long last = prefs.getLong(LAST_INTERSTITIAL_TIME, 0L);
        return System.currentTimeMillis() - last >= INTERSTITIAL_COOLDOWN_MS;
    }

    private static void markInterstitialShown(Context context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(LAST_INTERSTITIAL_TIME, System.currentTimeMillis())
                .apply();
    }
}
