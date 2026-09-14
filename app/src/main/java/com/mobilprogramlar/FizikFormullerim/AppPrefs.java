package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Yerel tercihler: güncelleme snooze, kutlama/bilgi/tanıtım/şerit kapatma, ödüllü mola.
 * Kalıcı reklamsız = PremiumManager (IAP).
 */
public final class AppPrefs {

    private static final String PREFS = "fizik_remote_prefs";
    private static final String KEY_UPDATE_SNOOZE_MS = "update_snooze_ms";
    private static final String KEY_REVIEW_ASKED_MS = "review_asked_ms";
    private static final String KEY_ADS_PAUSE_UNTIL_MS = "ads_pause_until_ms";
    private static final String KEY_ANNOUNCE_DISMISSED = "announce_dismissed_id";
    private static final String KEY_INFO_DISMISSED = "info_dismissed_id";
    private static final String KEY_PROMO_DISMISSED = "promo_dismissed_id";
    private static final String KEY_STRIP_HIDDEN = "strip_hidden_id";

    private AppPrefs() {
    }

    private static SharedPreferences p(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static long adsPauseUntilMs(Context context) {
        return p(context).getLong(KEY_ADS_PAUSE_UNTIL_MS, 0L);
    }

    public static boolean isAdsPaused(Context context) {
        return adsPauseUntilMs(context) > System.currentTimeMillis();
    }

    public static void setAdsPausedForHours(Context context, int hours) {
        int h = Math.max(1, hours);
        long until = System.currentTimeMillis() + h * 60L * 60L * 1000L;
        p(context).edit().putLong(KEY_ADS_PAUSE_UNTIL_MS, until).apply();
    }

    public static void clearAdsPause(Context context) {
        p(context).edit().remove(KEY_ADS_PAUSE_UNTIL_MS).apply();
    }

    public static long remainingPauseMs(Context context) {
        return Math.max(0L, adsPauseUntilMs(context) - System.currentTimeMillis());
    }

    /** TrafiQ tarzı: "2 sa 15 dk" / "12 dk". */
    @NonNull
    public static String formatAdsPauseRemaining(Context context) {
        long ms = remainingPauseMs(context);
        if (ms <= 0L) {
            return "";
        }
        long totalMin = (ms + 59_999L) / 60_000L;
        long hours = totalMin / 60L;
        long mins = totalMin % 60L;
        if (hours > 0L) {
            return context.getString(R.string.ads_break_remaining_hm_fmt, hours, mins);
        }
        return context.getString(R.string.ads_break_remaining_m_fmt, Math.max(1L, mins));
    }

    public static void setUpdateSnoozeNow(Context context) {
        p(context).edit().putLong(KEY_UPDATE_SNOOZE_MS, System.currentTimeMillis()).apply();
    }

    public static boolean isUpdateSnoozed(Context context, long snoozeMs) {
        long last = p(context).getLong(KEY_UPDATE_SNOOZE_MS, 0L);
        return last > 0L && System.currentTimeMillis() - last < snoozeMs;
    }

    public static void setReviewAskedNow(Context context) {
        p(context).edit().putLong(KEY_REVIEW_ASKED_MS, System.currentTimeMillis()).apply();
    }

    public static boolean isReviewCoolingDown(Context context, long cooldownMs) {
        long last = p(context).getLong(KEY_REVIEW_ASKED_MS, 0L);
        return last > 0L && System.currentTimeMillis() - last < cooldownMs;
    }

    public static boolean isAnnouncementDismissed(Context context, String id) {
        if (id == null || id.isEmpty()) {
            return true;
        }
        return id.equals(p(context).getString(KEY_ANNOUNCE_DISMISSED, ""));
    }

    public static void setAnnouncementDismissed(Context context, String id) {
        if (id == null) {
            return;
        }
        p(context).edit().putString(KEY_ANNOUNCE_DISMISSED, id).apply();
    }

    public static boolean isInfoDismissed(Context context, String id) {
        if (id == null || id.isEmpty()) {
            return true;
        }
        return id.equals(p(context).getString(KEY_INFO_DISMISSED, ""));
    }

    public static void setInfoDismissed(Context context, String id) {
        if (id == null) {
            return;
        }
        p(context).edit().putString(KEY_INFO_DISMISSED, id).apply();
    }

    public static boolean isPromoDismissed(Context context, String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        return id.equals(p(context).getString(KEY_PROMO_DISMISSED, ""));
    }

    public static void setPromoDismissed(Context context, String id) {
        if (id == null || id.isEmpty()) {
            return;
        }
        p(context).edit().putString(KEY_PROMO_DISMISSED, id).apply();
    }

    public static boolean isStripHidden(Context context, String textKey) {
        if (textKey == null || textKey.isEmpty()) {
            return false;
        }
        return textKey.equals(p(context).getString(KEY_STRIP_HIDDEN, ""));
    }

    public static void setStripHidden(Context context, String textKey) {
        if (textKey == null) {
            return;
        }
        p(context).edit().putString(KEY_STRIP_HIDDEN, textKey).apply();
    }
}
