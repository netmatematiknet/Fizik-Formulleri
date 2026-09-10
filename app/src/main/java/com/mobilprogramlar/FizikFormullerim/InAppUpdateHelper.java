package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

/**
 * Play In-App Update + Play Store yedek açılış.
 */
public final class InAppUpdateHelper {

    private static final String TAG = "InAppUpdateHelper";

    public interface AvailabilityCallback {
        void onResult(boolean updateAvailable, @Nullable AppUpdateInfo info);
    }

    private final AppCompatActivity activity;
    private final AppUpdateManager appUpdateManager;
    private final ActivityResultLauncher<IntentSenderRequest> updateLauncher;
    @Nullable
    private InstallStateUpdatedListener installListener;

    public InAppUpdateHelper(@NonNull AppCompatActivity activity,
                             @NonNull ActivityResultLauncher<IntentSenderRequest> updateLauncher) {
        this.activity = activity;
        this.appUpdateManager = AppUpdateManagerFactory.create(activity);
        this.updateLauncher = updateLauncher;
    }

    public void checkAvailability(@NonNull AvailabilityCallback callback) {
        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(info -> {
            boolean available = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
            callback.onResult(available, info);
        });
        task.addOnFailureListener(e -> {
            Log.w(TAG, "update check failed", e);
            callback.onResult(false, null);
        });
    }

    /** Uygulama ön plana gelince yarıda kalan immediate güncellemeyi sürdür. */
    public void resumeIfNeeded() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startFlow(info, true);
            } else if (info.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate();
            }
        });
    }

    public void startFlow(@Nullable AppUpdateInfo info, boolean forceImmediate) {
        if (info == null) {
            openPlayStore(activity);
            return;
        }
        int type = forceImmediate ? AppUpdateType.IMMEDIATE : AppUpdateType.FLEXIBLE;
        if (!info.isUpdateTypeAllowed(type)) {
            type = forceImmediate ? AppUpdateType.FLEXIBLE : AppUpdateType.IMMEDIATE;
            if (!info.isUpdateTypeAllowed(type)) {
                openPlayStore(activity);
                return;
            }
        }
        if (type == AppUpdateType.FLEXIBLE) {
            registerFlexibleListener();
        }
        try {
            appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(type).build());
        } catch (Exception e) {
            Log.e(TAG, "startUpdateFlow failed", e);
            openPlayStore(activity);
        }
    }

    private void registerFlexibleListener() {
        if (installListener != null) {
            return;
        }
        installListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate();
            }
        };
        appUpdateManager.registerListener(installListener);
    }

    public void unregister() {
        if (installListener != null) {
            appUpdateManager.unregisterListener(installListener);
            installListener = null;
        }
    }

    public static void openPlayStore(@NonNull Activity activity) {
        String packageName = activity.getPackageName();
        try {
            Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            market.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(market);
        } catch (ActivityNotFoundException e) {
            Intent web = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(web);
        }
    }
}
