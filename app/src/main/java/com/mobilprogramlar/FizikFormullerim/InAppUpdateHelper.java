package com.mobilprogramlar.FizikFormullerim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

/**
 * Play Store uygulama içi güncelleme (flexible).
 */
public final class InAppUpdateHelper {

    public static final int REQUEST_CODE = 3912;

    private final AppCompatActivity activity;
    private final AppUpdateManager updateManager;
    private InstallStateUpdatedListener listener;

    public InAppUpdateHelper(@NonNull AppCompatActivity activity) {
        this.activity = activity;
        this.updateManager = AppUpdateManagerFactory.create(activity);
    }

    public void checkForUpdate() {
        Task<AppUpdateInfo> task = updateManager.getAppUpdateInfo();
        task.addOnSuccessListener(info -> {
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startFlexible(info);
            } else if (info.installStatus() == InstallStatus.DOWNLOADED) {
                promptCompleteUpdate();
            }
        });
    }

    public void onResume() {
        updateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            if (info.installStatus() == InstallStatus.DOWNLOADED) {
                promptCompleteUpdate();
            }
        });
    }

    public void unregister() {
        if (listener != null) {
            updateManager.unregisterListener(listener);
            listener = null;
        }
    }

    private void startFlexible(@NonNull AppUpdateInfo info) {
        listener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                promptCompleteUpdate();
            }
        };
        updateManager.registerListener(listener);
        try {
            updateManager.startUpdateFlowForResult(
                    info,
                    activity,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                    REQUEST_CODE);
        } catch (Exception ignored) {
            unregister();
        }
    }

    private void promptCompleteUpdate() {
        View root = activity.findViewById(android.R.id.content);
        if (root == null) {
            return;
        }
        Snackbar.make(root, R.string.guncelleme_hazir, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.guncelle_yeniden_baslat, v -> updateManager.completeUpdate())
                .show();
    }
}
