package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.pm.InstallSourceInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

/**
 * Soft: diyalog + Flexible In-App Update (24s snooze).
 * Zorunlu: Remote Config min sürüm altı + kapatılamaz Immediate.
 */
public final class UpdateHelper {

    private static final String TAG = "UpdateHelper";
    private static final long SNOOZE_MS = 24L * 60L * 60L * 1000L;
    private static boolean promptShownThisSession;

    private UpdateHelper() {
    }

    public static void maybePrompt(
            @NonNull AppCompatActivity activity,
            @NonNull ActivityResultLauncher<IntentSenderRequest> launcher) {
        if (activity.isFinishing() || activity.isDestroyed() || promptShownThisSession) {
            return;
        }
        if (!installedFromPlay(activity)) {
            maybeShowRemoteOnly(activity);
            return;
        }

        AppUpdateManager manager = AppUpdateManagerFactory.create(activity);
        Task<AppUpdateInfo> task = manager.getAppUpdateInfo();
        task.addOnSuccessListener(info -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            try {
                handleInfo(activity, manager, info, launcher);
            } catch (RuntimeException e) {
                Log.w(TAG, "handleInfo", e);
            }
        }).addOnFailureListener(e -> {
            Log.w(TAG, "getAppUpdateInfo", e);
            maybeShowRemoteOnly(activity);
        });
    }

    /** Flexible indirme bitince kurulum. */
    public static void completeFlexibleIfNeeded(@NonNull Activity activity) {
        if (!installedFromPlay(activity)) {
            return;
        }
        AppUpdateManager manager = AppUpdateManagerFactory.create(activity);
        manager.getAppUpdateInfo().addOnSuccessListener(info -> {
            if (info.installStatus() == InstallStatus.DOWNLOADED) {
                manager.completeUpdate();
            }
        });
    }

    private static void handleInfo(
            @NonNull AppCompatActivity activity,
            @NonNull AppUpdateManager manager,
            @NonNull AppUpdateInfo info,
            @NonNull ActivityResultLauncher<IntentSenderRequest> launcher) {
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        int current = BuildConfig.VERSION_CODE;
        int min = rc.getUpdateMinVersionCode();
        boolean forcedByRc = min > 0 && current < min && rc.isUpdateForced();
        boolean softByRc = min > 0 && current < min && !rc.isUpdateForced();
        boolean playHasUpdate = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;

        if (forcedByRc) {
            promptShownThisSession = true;
            showForcedDialog(activity, manager, info, launcher);
            return;
        }

        if (AppPrefs.isUpdateSnoozed(activity, SNOOZE_MS)) {
            return;
        }

        if (softByRc || playHasUpdate) {
            if (playHasUpdate && info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                promptShownThisSession = true;
                showSoftDialog(activity, manager, info, launcher, true);
            } else if (softByRc || playHasUpdate) {
                promptShownThisSession = true;
                showSoftDialog(activity, manager, info, launcher, false);
            }
        }
    }

    private static void maybeShowRemoteOnly(@NonNull AppCompatActivity activity) {
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        int current = BuildConfig.VERSION_CODE;
        int min = rc.getUpdateMinVersionCode();
        if (min <= 0 || current >= min) {
            return;
        }
        if (rc.isUpdateForced()) {
            promptShownThisSession = true;
            showForcedStoreOnly(activity);
            return;
        }
        if (AppPrefs.isUpdateSnoozed(activity, SNOOZE_MS)) {
            return;
        }
        promptShownThisSession = true;
        showSoftStoreOnly(activity);
    }

    private static void showSoftDialog(
            @NonNull AppCompatActivity activity,
            @NonNull AppUpdateManager manager,
            @Nullable AppUpdateInfo info,
            @NonNull ActivityResultLauncher<IntentSenderRequest> launcher,
            boolean canFlexible) {
        String title = title(activity);
        String message = message(activity);
        AlertDialog.Builder b = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.guncelleme_simdi, (d, w) -> {
                    if (canFlexible && info != null) {
                        startFlexible(activity, manager, info, launcher);
                    } else {
                        PlayHelper.openAppStoreListing(activity);
                    }
                })
                .setNegativeButton(R.string.guncelleme_tamam, (d, w) ->
                        AppPrefs.setUpdateSnoozeNow(activity))
                .setCancelable(true);
        AlertDialog dialog = b.create();
        dialog.setOnCancelListener(d -> AppPrefs.setUpdateSnoozeNow(activity));
        dialog.show();
        AppPrefs.setUpdateSnoozeNow(activity);
    }

    private static void showSoftStoreOnly(@NonNull AppCompatActivity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(title(activity))
                .setMessage(message(activity))
                .setPositiveButton(R.string.guncelleme_simdi, (d, w) ->
                        PlayHelper.openAppStoreListing(activity))
                .setNegativeButton(R.string.guncelleme_tamam, (d, w) ->
                        AppPrefs.setUpdateSnoozeNow(activity))
                .setOnCancelListener(d -> AppPrefs.setUpdateSnoozeNow(activity))
                .show();
        AppPrefs.setUpdateSnoozeNow(activity);
    }

    private static void showForcedDialog(
            @NonNull AppCompatActivity activity,
            @NonNull AppUpdateManager manager,
            @NonNull AppUpdateInfo info,
            @NonNull ActivityResultLauncher<IntentSenderRequest> launcher) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title(activity))
                .setMessage(message(activity))
                .setPositiveButton(R.string.guncelleme_simdi, (d, w) -> {
                    if (info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startImmediate(activity, manager, info, launcher);
                    } else if (info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        startFlexible(activity, manager, info, launcher);
                    } else {
                        PlayHelper.openAppStoreListing(activity);
                    }
                })
                .setCancelable(false)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private static void showForcedStoreOnly(@NonNull AppCompatActivity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title(activity))
                .setMessage(message(activity))
                .setPositiveButton(R.string.guncelleme_simdi, (d, w) ->
                        PlayHelper.openAppStoreListing(activity))
                .setCancelable(false)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private static void startFlexible(
            @NonNull Activity activity,
            @NonNull AppUpdateManager manager,
            @NonNull AppUpdateInfo info,
            @NonNull ActivityResultLauncher<IntentSenderRequest> launcher) {
        try {
            manager.startUpdateFlowForResult(
                    info,
                    launcher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build());
        } catch (Exception e) {
            Log.w(TAG, "flexible", e);
            PlayHelper.openAppStoreListing(activity);
        }
    }

    private static void startImmediate(
            @NonNull Activity activity,
            @NonNull AppUpdateManager manager,
            @NonNull AppUpdateInfo info,
            @NonNull ActivityResultLauncher<IntentSenderRequest> launcher) {
        try {
            manager.startUpdateFlowForResult(
                    info,
                    launcher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
        } catch (Exception e) {
            Log.w(TAG, "immediate", e);
            PlayHelper.openAppStoreListing(activity);
        }
    }

    @NonNull
    private static String title(@NonNull Activity activity) {
        String t = AppRemoteConfig.getInstance(activity).getUpdateTitle();
        if (t == null || t.trim().isEmpty()) {
            return activity.getString(R.string.guncelleme_baslik_varsayilan);
        }
        return t.trim();
    }

    @NonNull
    private static String message(@NonNull Activity activity) {
        String m = AppRemoteConfig.getInstance(activity).getUpdateMessage();
        if (m == null || m.trim().isEmpty()) {
            return activity.getString(R.string.guncelleme_mesaj_varsayilan);
        }
        return m.trim();
    }

    private static boolean installedFromPlay(@NonNull Activity activity) {
        try {
            String pkg = activity.getPackageName();
            PackageManager pm = activity.getPackageManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                InstallSourceInfo info = pm.getInstallSourceInfo(pkg);
                return info != null && "com.android.vending".equals(info.getInstallingPackageName());
            }
            //noinspection deprecation
            return "com.android.vending".equals(pm.getInstallerPackageName(pkg));
        } catch (Exception e) {
            return false;
        }
    }
}
