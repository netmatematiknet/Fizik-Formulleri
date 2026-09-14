package com.mobilprogramlar.FizikFormullerim;

import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Öncelik: bakim → guncelleme → kutlama → bilgi.
 * Şerit / tanıtım ayrı bind edilir.
 */
public final class RemoteUiCoordinator {

    private RemoteUiCoordinator() {
    }

    public static void runHomeFlow(
            @NonNull AppCompatActivity activity,
            @NonNull ActivityResultLauncher<IntentSenderRequest> updateLauncher,
            @Nullable ViewGroup seritHost,
            @Nullable ViewGroup tanitimHost) {
        AppRemoteConfig.getInstance(activity).fetchAndActivate(task -> activity.runOnUiThread(() -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            SeritHelper.bind(activity, seritHost);
            TanitimHelper.bind(activity, tanitimHost, AppRemoteConfig.YER_ANA);
            if (MaintenanceHelper.maybeBlock(activity)) {
                return;
            }
            UpdateHelper.maybePrompt(activity, updateLauncher);
            activity.getWindow().getDecorView().postDelayed(
                    () -> AnnouncementHelper.maybeShow(activity), 600L);
            activity.getWindow().getDecorView().postDelayed(
                    () -> InfoHelper.maybeShow(activity), 1400L);
            AppOpenAdHelper.load(activity);
            activity.getWindow().getDecorView().postDelayed(
                    () -> AppOpenAdHelper.showIfReady(activity), 2200L);
        }));
    }
}
