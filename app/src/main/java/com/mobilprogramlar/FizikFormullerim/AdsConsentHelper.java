package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.MobileAds;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.concurrent.atomic.AtomicBoolean;

public final class AdsConsentHelper {

    private static final AtomicBoolean adsInitialized = new AtomicBoolean(false);

    private AdsConsentHelper() {
    }

    public static void gatherConsentAndInitAds(
            @NonNull Activity activity, @Nullable Runnable onAdsReady) {
        AtomicBoolean readyNotified = new AtomicBoolean(false);
        ConsentInformation consentInformation =
                UserMessagingPlatform.getConsentInformation(activity);

        Runnable notifyReady = () -> {
            if (onAdsReady != null && readyNotified.compareAndSet(false, true)) {
                activity.runOnUiThread(onAdsReady);
            }
        };

        if (consentInformation.canRequestAds()) {
            initializeAds(activity);
            notifyReady.run();
        }

        ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();
        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                () -> UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                        activity,
                        formError -> {
                            if (consentInformation.canRequestAds()) {
                                initializeAds(activity);
                            }
                            notifyReady.run();
                        }),
                formError -> {
                    if (consentInformation.canRequestAds()) {
                        initializeAds(activity);
                    }
                    notifyReady.run();
                });
    }

    /** Eski çağrılar için uyumluluk. */
    public static void initializeMobileAds(@NonNull Context context) {
        initializeAds(context);
    }

    public static void gatherConsent(@NonNull Activity activity, @Nullable Runnable onAdsReady) {
        gatherConsentAndInitAds(activity, onAdsReady);
    }

    public static void initializeAds(@NonNull Context context) {
        if (!adsInitialized.compareAndSet(false, true)) {
            return;
        }
        MobileAds.initialize(context, initializationStatus -> {
        });
    }

    public static boolean canRequestAds(@NonNull Activity activity) {
        return UserMessagingPlatform.getConsentInformation(activity).canRequestAds();
    }

    public static boolean isPrivacyOptionsRequired(@NonNull Activity activity) {
        return UserMessagingPlatform.getConsentInformation(activity)
                .getPrivacyOptionsRequirementStatus()
                == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED;
    }

    public static void showPrivacyOptionsForm(@NonNull Activity activity) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, formError -> {
        });
    }
}
