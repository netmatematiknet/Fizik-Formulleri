package com.mobilprogramlar.FizikFormullerim;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Bakım kilidi — bakim_* Remote Config.
 * true iken menü kullanılamaz; çıkış butonu ile kapanır.
 */
public final class MaintenanceHelper {

    private static boolean shownThisSession;

    private MaintenanceHelper() {
    }

    /** @return true ise bakım açık — diğer popup'lar gösterilmemeli */
    public static boolean maybeBlock(@NonNull AppCompatActivity activity) {
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        if (!rc.isMaintenanceEnabled()) {
            return false;
        }
        if (shownThisSession || activity.isFinishing() || activity.isDestroyed()) {
            return true;
        }
        shownThisSession = true;
        String title = rc.getMaintenanceTitle();
        String body = rc.getMaintenanceMessage();
        if (TextUtils.isEmpty(title)) {
            title = activity.getString(R.string.bakim_baslik_varsayilan);
        }
        if (TextUtils.isEmpty(body)) {
            body = activity.getString(R.string.bakim_mesaj_varsayilan);
        }
        String finalTitle = title.trim();
        String finalBody = body.trim();
        activity.runOnUiThread(() -> ThemedRemoteDialog.show(
                activity,
                finalTitle,
                finalBody,
                activity.getString(R.string.bakim_cikis),
                null,
                android.R.drawable.ic_dialog_alert,
                false,
                activity::finishAffinity));
        return true;
    }
}
