package com.mobilprogramlar.FizikFormullerim;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Bayram / duyuru kartı — kutlama_* Remote Config.
 */
public final class AnnouncementHelper {

    private static boolean shownThisSession;

    private AnnouncementHelper() {
    }

    public static void maybeShow(@NonNull AppCompatActivity activity) {
        if (shownThisSession || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        if (!rc.isAnnouncementEnabled()) {
            return;
        }
        String id = rc.getAnnouncementId();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        if (AppPrefs.isAnnouncementDismissed(activity, id.trim())) {
            return;
        }
        String title = rc.getAnnouncementTitle();
        String body = rc.getAnnouncementMessage();
        if (TextUtils.isEmpty(title)) {
            title = activity.getString(R.string.kutlama_baslik_varsayilan);
        }
        if (TextUtils.isEmpty(body)) {
            return;
        }
        shownThisSession = true;
        String finalId = id.trim();
        String finalTitle = title.trim();
        String finalBody = body.trim();
        String button = rc.getAnnouncementButton();
        if (TextUtils.isEmpty(button)) {
            button = activity.getString(R.string.kutlama_tamam);
        }
        String url = rc.getAnnouncementUrl();
        String imageUrl = rc.getAnnouncementImageUrl();
        String finalButton = button.trim();
        String finalUrl = url == null ? "" : url.trim();
        activity.runOnUiThread(() -> ThemedRemoteDialog.show(
                activity,
                finalTitle,
                finalBody,
                finalButton,
                imageUrl,
                android.R.drawable.ic_dialog_info,
                true,
                () -> {
                    AppPrefs.setAnnouncementDismissed(activity, finalId);
                    if (!TextUtils.isEmpty(finalUrl)) {
                        PlayHelper.openUrl(activity, finalUrl);
                    }
                },
                () -> AppPrefs.setAnnouncementDismissed(activity, finalId)));
    }
}
