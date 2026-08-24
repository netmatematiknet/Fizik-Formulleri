package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Random;

/**
 * Tek bir geçiş reklamı örneği; Activity'ler arası paylaşım burada toplanır (static karmaşa NtHelper'dan ayrıldı).
 */
public final class InterstitialAdManager {

    private static final String TAG = "InterstitialAdMgr";

    private InterstitialAd interstitialAd;
    private boolean loading;

    public void load(@NonNull Context context) {
        if (PremiumManager.getInstance(context).isAdFree()) {
            return;
        }
        if (!AppRemoteConfig.getInstance(context).areInterstitialsEnabled()) {
            return;
        }
        if (loading || interstitialAd != null) {
            return;
        }
        loading = true;
        String adUnitId = AdHelper.resolveInterstitialAdUnitId(context);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, adUnitId, adRequest, new InterstitialAdLoadCallback() {
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

    public void showIfReady(@NonNull Activity activity) {
        if (PremiumManager.getInstance(activity).isAdFree()) {
            return;
        }
        if (interstitialAd == null) {
            Log.d(TAG, "Gösterilecek reklam hazır değil.");
            return;
        }
        final InterstitialAd ad = interstitialAd;
        interstitialAd = null;
        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                ad.setFullScreenContentCallback(null);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                ad.setFullScreenContentCallback(null);
            }

            @Override
            public void onAdShowedFullScreenContent() {
            }
        });
        ad.show(activity);
    }

    /**
     * Olasılık isabetinde: hazırsa göster, değilse (ve zaten yüklenmiyorsa) yükle.
     */
    public void showWithProbability(@NonNull Activity activity, int probabilityPercent) {
        if (PremiumManager.getInstance(activity).isAdFree()) {
            return;
        }
        if (!AppRemoteConfig.getInstance(activity).areInterstitialsEnabled()) {
            return;
        }
        if (probabilityPercent <= 0) {
            return;
        }
        if (probabilityPercent > 100) {
            probabilityPercent = 100;
        }
        if (new Random().nextInt(100) >= probabilityPercent) {
            return;
        }
        if (interstitialAd != null) {
            showIfReady(activity);
        } else if (!loading) {
            load(activity);
        }
    }
}
