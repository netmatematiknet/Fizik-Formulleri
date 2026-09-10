package com.mobilprogramlar.FizikFormullerim;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Calendar;

/**
 * Yerel çalışma hatırlatıcısı: her gün ~18:00.
 */
public final class StudyReminderScheduler {

    private static final String TAG = "StudyReminder";
    public static final int REQUEST_CODE = 7101;
    public static final int HOUR = 18;
    public static final int MINUTE = 0;

    private StudyReminderScheduler() {
    }

    public static void applyFromPrefs(@NonNull Context context) {
        if (NotificationPrefs.isStudyReminderEnabled(context)) {
            scheduleNext(context);
        } else {
            cancel(context);
        }
    }

    public static void scheduleNext(@NonNull Context context) {
        Context app = context.getApplicationContext();
        AlarmManager am = (AlarmManager) app.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return;
        }
        PendingIntent pi = pendingIntent(app);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, HOUR);
        cal.set(Calendar.MINUTE, MINUTE);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }
            Log.d(TAG, "scheduled at " + cal.getTime());
        } catch (Exception e) {
            Log.w(TAG, "schedule failed", e);
        }
    }

    public static void cancel(@NonNull Context context) {
        Context app = context.getApplicationContext();
        AlarmManager am = (AlarmManager) app.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(pendingIntent(app));
        }
    }

    @NonNull
    private static PendingIntent pendingIntent(@NonNull Context context) {
        Intent intent = new Intent(context, StudyReminderReceiver.class);
        intent.setAction(StudyReminderReceiver.ACTION_STUDY_REMIND);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, flags);
    }

    /** Boot sonrası yeniden planla. */
    public static class BootReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())
                    || Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
                applyFromPrefs(context);
            }
        }
    }
}
