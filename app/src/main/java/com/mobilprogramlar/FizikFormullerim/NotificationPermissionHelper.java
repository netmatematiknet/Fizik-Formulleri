package com.mobilprogramlar.FizikFormullerim;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public final class NotificationPermissionHelper {

    private static final String PREFS = "notification_permission_prefs";
    private static final String KEY_ASKED = "notification_permission_asked";

    private NotificationPermissionHelper() {
    }

    public static boolean hasPermission(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true;
        }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    @NonNull
    public static ActivityResultLauncher<String> register(@NonNull ComponentActivity activity) {
        return activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> prefs(activity).edit().putBoolean(KEY_ASKED, true).apply());
    }

    public static void requestIfNeeded(
            @NonNull Activity activity,
            @NonNull ActivityResultLauncher<String> launcher) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return;
        }
        if (hasPermission(activity)) {
            return;
        }
        if (prefs(activity).getBoolean(KEY_ASKED, false)) {
            return;
        }
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.notification_permission_title)
                    .setMessage(R.string.notification_permission_message)
                    .setPositiveButton(R.string.notification_permission_allow, (d, w) ->
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS))
                    .setNegativeButton(R.string.notification_permission_deny, (d, w) ->
                            prefs(activity).edit().putBoolean(KEY_ASKED, true).apply())
                    .setCancelable(true)
                    .show();
            return;
        }
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
