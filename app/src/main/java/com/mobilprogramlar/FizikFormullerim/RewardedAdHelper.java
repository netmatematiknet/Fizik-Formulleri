package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

/** Ödüllü video → reklam_odullu_molasi_saat kadar reklamsız mola. */
public final class RewardedAdHelper {

    private static final String TAG = "RewardedAd";
    private static RewardedAd rewardedAd;
    private static boolean loading;
    private static boolean rewardEarned;

    private RewardedAdHelper() {
    }

    public static boolean canOfferPause(@Nullable Activity activity) {
        if (activity == null) {
            return false;
        }
        if (PremiumManager.getInstance(activity).isAdFree() || AppPrefs.isAdsPaused(activity)) {
            return false;
        }
        return AppRemoteConfig.getInstance(activity).isRewardedEnabled();
    }

    public static void preload(@Nullable Activity activity) {
        if (activity == null || loading || rewardedAd != null) {
            return;
        }
        if (!canOfferPause(activity)) {
            return;
        }
        loading = true;
        String unitId = AdGate.resolveRewardedAdUnitId(activity);
        RewardedAd.load(activity.getApplicationContext(), unitId, new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        loading = false;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                        loading = false;
                        Log.w(TAG, "load fail: " + loadAdError.getMessage());
                    }
                });
    }

    public static void showForAdPause(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        if (PremiumManager.getInstance(activity).isAdFree()) {
            Toast.makeText(activity, R.string.reklamsiz_alindi, Toast.LENGTH_SHORT).show();
            return;
        }
        if (AppPrefs.isAdsPaused(activity)) {
            Toast.makeText(activity, R.string.odullu_mola_aktif, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!AppRemoteConfig.getInstance(activity).isRewardedEnabled()) {
            Toast.makeText(activity, R.string.odullu_kapali, Toast.LENGTH_SHORT).show();
            return;
        }
        int hours = AppRemoteConfig.getInstance(activity).getRewardedPauseHours();
        if (activity instanceof AppCompatActivity) {
            ThemedRemoteDialog.show(
                    (AppCompatActivity) activity,
                    activity.getString(R.string.odullu_mola_onay_baslik),
                    activity.getString(R.string.odullu_mola_onay_mesaj, hours),
                    activity.getString(R.string.odullu_mola_onay_izle),
                    null,
                    android.R.drawable.ic_menu_slideshow,
                    true,
                    () -> showRewardedNow(activity),
                    null);
        } else {
            showRewardedNow(activity);
        }
    }

    private static void showRewardedNow(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        if (rewardedAd == null) {
            Toast.makeText(activity, R.string.odullu_yukleniyor, Toast.LENGTH_SHORT).show();
            preload(activity);
            return;
        }
        rewardEarned = false;
        final RewardedAd ad = rewardedAd;
        rewardedAd = null;
        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                if (rewardEarned) {
                    applyPause(activity);
                }
                preload(activity);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.w(TAG, "show fail: " + adError.getMessage());
                Toast.makeText(activity, R.string.odullu_yuklenemedi, Toast.LENGTH_SHORT).show();
                preload(activity);
            }
        });
        ad.show(activity, (OnUserEarnedRewardListener) rewardItem -> rewardEarned = true);
    }

    private static void applyPause(@NonNull Activity activity) {
        int hours = AppRemoteConfig.getInstance(activity).getRewardedPauseHours();
        AppPrefs.setAdsPausedForHours(activity, hours);
        AdHelper.destroyBannerAd();
        Toast.makeText(activity, activity.getString(R.string.odullu_mola_basarili, hours), Toast.LENGTH_LONG).show();
    }
}
