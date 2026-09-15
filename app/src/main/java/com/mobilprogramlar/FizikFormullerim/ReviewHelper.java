package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.pm.InstallSourceInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

/** Puan ver diyaloğu: Play Store + In-App Review. */
public final class ReviewHelper {

    private static final String TAG = "ReviewHelper";

    private ReviewHelper() {
    }

    public static void askFromButton(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        View root = LayoutInflater.from(activity).inflate(R.layout.dialog_rate, null, false);
        try {
            ThemeColors tc = new ThemeManager(activity).getThemeColors();
            com.google.android.material.button.MaterialButton play = root.findViewById(R.id.btn_rate_play);
            com.google.android.material.button.MaterialButton inapp = root.findViewById(R.id.btn_rate_inapp);
            android.widget.TextView cancel = root.findViewById(R.id.btn_rate_iptal);
            if (play != null) {
                play.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tc.toolbarBackgroundColor));
                play.setTextColor(DialogFit.contrastingOn(tc.toolbarBackgroundColor));
            }
            if (inapp != null) {
                inapp.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tc.toolbarBackgroundColor));
                inapp.setTextColor(DialogFit.contrastingOn(tc.toolbarBackgroundColor));
            }
            if (cancel != null) {
                cancel.setTextColor(tc.toolbarBackgroundColor);
            }
        } catch (Throwable ignored) {
        }
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(root)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        root.findViewById(R.id.btn_rate_play).setOnClickListener(v -> {
            dialog.dismiss();
            PlayHelper.openAppStoreListing(activity);
        });
        root.findViewById(R.id.btn_rate_inapp).setOnClickListener(v -> {
            dialog.dismiss();
            launchReview(activity, true);
        });
        root.findViewById(R.id.btn_rate_iptal).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        DialogFit.apply(dialog);
    }

    private static void launchReview(@NonNull Activity activity, boolean fromButton) {
        if (fromButton && !installedFromPlay(activity)) {
            Toast.makeText(activity, R.string.puan_inceleme_play_gerekli, Toast.LENGTH_LONG).show();
            PlayHelper.openAppStoreListing(activity);
            return;
        }
        try {
            ReviewManager manager = ReviewManagerFactory.create(activity);
            manager.requestReviewFlow()
                    .addOnSuccessListener(reviewInfo -> launch(activity, manager, reviewInfo, fromButton))
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "requestReviewFlow", e);
                        if (fromButton) {
                            Toast.makeText(activity, R.string.puan_inceleme_yok, Toast.LENGTH_LONG).show();
                            PlayHelper.openAppStoreListing(activity);
                        }
                    });
        } catch (RuntimeException e) {
            Log.w(TAG, "launchReview", e);
            if (fromButton) {
                Toast.makeText(activity, R.string.puan_inceleme_yok, Toast.LENGTH_LONG).show();
                PlayHelper.openAppStoreListing(activity);
            }
        }
    }

    private static void launch(
            @NonNull Activity activity,
            @NonNull ReviewManager manager,
            @NonNull ReviewInfo info,
            boolean fromButton) {
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        manager.launchReviewFlow(activity, info)
                .addOnCompleteListener(task -> {
                    AppPrefs.setReviewAskedNow(activity);
                    if (fromButton && !activity.isFinishing()) {
                        Toast.makeText(activity, R.string.puan_inceleme_bilgi, Toast.LENGTH_LONG).show();
                    }
                });
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
