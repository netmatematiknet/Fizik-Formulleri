package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Bildirim tercihleri: duyurular (FCM) + çalışma hatırlatıcısı (yerel).
 */
public final class NotificationPrefs {

    private static final String PREFS = "notification_prefs";
    public static final String KEY_ANNOUNCEMENTS = "pref_announcements";
    public static final String KEY_STUDY_REMINDER = "pref_study_reminder";

    private NotificationPrefs() {
    }

    @NonNull
    private static SharedPreferences prefs(@NonNull Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static boolean areAnnouncementsEnabled(@NonNull Context context) {
        return prefs(context).getBoolean(KEY_ANNOUNCEMENTS, true);
    }

    public static void setAnnouncementsEnabled(@NonNull Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_ANNOUNCEMENTS, enabled).apply();
    }

    public static boolean isStudyReminderEnabled(@NonNull Context context) {
        return prefs(context).getBoolean(KEY_STUDY_REMINDER, false);
    }

    public static void setStudyReminderEnabled(@NonNull Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_STUDY_REMINDER, enabled).apply();
    }
}
