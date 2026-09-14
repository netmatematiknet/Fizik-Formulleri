package com.mobilprogramlar.FizikFormullerim;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Linkli bilgilendirme — bilgi_* Remote Config.
 */
public final class InfoHelper {

    private static boolean shownThisSession;

    private InfoHelper() {
    }

    public static void maybeShow(@NonNull AppCompatActivity activity) {
        if (shownThisSession || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        if (!rc.isInfoEnabled()) {
            return;
        }
        String id = rc.getInfoId();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        if (AppPrefs.isInfoDismissed(activity, id.trim())) {
            return;
        }
        String title = rc.getInfoTitle();
        String body = rc.getInfoMessage();
        if (TextUtils.isEmpty(body)) {
            return;
        }
        if (TextUtils.isEmpty(title)) {
            title = activity.getString(R.string.bilgi_baslik_varsayilan);
        }
        shownThisSession = true;
        String finalId = id.trim();
        String button = rc.getInfoButton();
        if (TextUtils.isEmpty(button)) {
            button = activity.getString(R.string.kutlama_tamam);
        }
        String url = rc.getInfoUrl();
        String imageUrl = rc.getInfoImageUrl();
        String finalUrl = url == null ? "" : url.trim();
        String finalButton = button.trim();
        String finalTitle = title.trim();
        String finalBody = body.trim();
        activity.runOnUiThread(() -> ThemedRemoteDialog.show(
                activity,
                finalTitle,
                finalBody,
                finalButton,
                imageUrl,
                android.R.drawable.ic_menu_info_details,
                true,
                () -> {
                    AppPrefs.setInfoDismissed(activity, finalId);
                    if (!TextUtils.isEmpty(finalUrl)) {
                        PlayHelper.openUrl(activity, finalUrl);
                    }
                },
                () -> AppPrefs.setInfoDismissed(activity, finalId)));
    }
}
