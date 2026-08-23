package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public final class CrashlyticsKeys {

    private CrashlyticsKeys() {
    }

    public static void refresh(@NonNull Context context) {
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCustomKey("package_name", context.getPackageName());
        crashlytics.setCustomKey("debug_build", BuildConfig.DEBUG);
        crashlytics.setCustomKey("sdk_int", Build.VERSION.SDK_INT);
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            crashlytics.setCustomKey("version_name", info.versionName != null ? info.versionName : "");
            crashlytics.setCustomKey("version_code", (int) info.getLongVersionCode());
        } catch (Exception ignored) {
        }
    }
}
