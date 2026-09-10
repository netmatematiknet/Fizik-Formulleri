package com.mobilprogramlar.FizikFormullerim;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.play.core.appupdate.AppUpdateInfo;

/**
 * Soft / zorunlu güncelleme diyaloğu (Millenicom tarzı).
 * Soft: 24 saat snooze. Zorunlu: kapatılamaz.
 */
public final class UpdatePromptCoordinator {

    private static final String PREFS = "update_prompt_prefs";
    private static final String KEY_SNOOZE_UNTIL = "snooze_until_ms";
    private static final long SNOOZE_MS = 24L * 60L * 60L * 1000L;

    private final AppCompatActivity activity;
    private final InAppUpdateHelper updateHelper;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    @Nullable
    private Dialog visibleDialog;
    @Nullable
    private AppUpdateInfo pendingInfo;
    private boolean forceMode;

    public UpdatePromptCoordinator(@NonNull AppCompatActivity activity,
                                   @NonNull InAppUpdateHelper updateHelper) {
        this.activity = activity;
        this.updateHelper = updateHelper;
    }

    public void checkAfterRemoteConfig() {
        if (activity.isFinishing()) {
            return;
        }
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        int minVersion = rc.getUpdateMinVersionCode();
        int current = BuildConfig.VERSION_CODE;
        boolean belowMin = minVersion > 0 && current < minVersion;
        boolean force = belowMin && rc.isUpdateForced();

        updateHelper.checkAvailability((playAvailable, info) -> mainHandler.post(() -> {
            if (activity.isFinishing()) {
                return;
            }
            pendingInfo = info;
            forceMode = force;

            if (belowMin) {
                showDialog(force);
                return;
            }
            // RC eşiği kapalı (0) veya üstündeyiz: Play'de yeni sürüm varsa soft diyalog
            if (playAvailable && !isSnoozed()) {
                forceMode = false;
                showDialog(false);
            }
        }));
    }

    public void onUpdateButtonClicked() {
        updateHelper.startFlow(pendingInfo, forceMode);
    }

    public void resumeIfNeeded() {
        updateHelper.resumeIfNeeded();
    }

    public void destroy() {
        dismissDialog();
        updateHelper.unregister();
    }

    private boolean isSnoozed() {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return System.currentTimeMillis() < prefs.getLong(KEY_SNOOZE_UNTIL, 0L);
    }

    private void snooze() {
        activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putLong(KEY_SNOOZE_UNTIL, System.currentTimeMillis() + SNOOZE_MS)
                .apply();
    }

    private void showDialog(boolean force) {
        if (visibleDialog != null && visibleDialog.isShowing()) {
            return;
        }
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update_prompt, null, false);
        TextView title = view.findViewById(R.id.tv_update_title);
        TextView message = view.findViewById(R.id.tv_update_message);
        MaterialButton btnNow = view.findViewById(R.id.btn_update_now);
        TextView btnLater = view.findViewById(R.id.btn_update_later);
        ImageButton btnClose = view.findViewById(R.id.btn_update_close);

        title.setText(rc.getUpdateTitle(activity));
        message.setText(rc.getUpdateMessage(activity));

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(!force);
        dialog.setCanceledOnTouchOutside(!force);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btnNow.setOnClickListener(v -> {
            if (!force) {
                dismissDialog();
            }
            onUpdateButtonClicked();
        });

        View.OnClickListener dismissSoft = v -> {
            if (force) {
                return;
            }
            snooze();
            dismissDialog();
        };
        btnLater.setOnClickListener(dismissSoft);
        btnClose.setOnClickListener(dismissSoft);

        if (force) {
            btnLater.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
        }

        visibleDialog = dialog;
        dialog.show();
    }

    private void dismissDialog() {
        if (visibleDialog != null) {
            try {
                visibleDialog.dismiss();
            } catch (Exception ignored) {
            }
            visibleDialog = null;
        }
    }
}
