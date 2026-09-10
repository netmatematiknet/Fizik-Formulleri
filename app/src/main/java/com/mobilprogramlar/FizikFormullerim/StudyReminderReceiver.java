package com.mobilprogramlar.FizikFormullerim;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * Çalışma hatırlatma bildirimi.
 */
public class StudyReminderReceiver extends BroadcastReceiver {

    public static final String ACTION_STUDY_REMIND = "com.mobilprogramlar.FizikFormullerim.STUDY_REMIND";
    public static final String CHANNEL_ID = "study_reminder";
    private static final int NOTIFICATION_ID = 7102;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            return;
        }
        if (!NotificationPrefs.isStudyReminderEnabled(context)) {
            return;
        }
        showNotification(context);
        StudyReminderScheduler.scheduleNext(context);
    }

    private void showNotification(@NonNull Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) {
            return;
        }
        ensureChannel(context, manager);

        TopicCatalog.Topic topic = FormulaOfTheDayHelper.today(context);
        String title = context.getString(R.string.study_reminder_title);
        String body = topic != null
                ? context.getString(R.string.study_reminder_body_topic, topic.title)
                : context.getString(R.string.study_reminder_body_generic);

        Intent open = new Intent(context, MainActivity.class);
        open.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(
                context, 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        android.app.Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(ContextCompat.getColor(context, R.color.nurullah_primary))
                .build();
        manager.notify(NOTIFICATION_ID, notification);
    }

    public static void ensureChannel(@NonNull Context context, @Nullable NotificationManager manager) {
        if (manager == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        if (manager.getNotificationChannel(CHANNEL_ID) != null) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.study_reminder_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(context.getString(R.string.study_reminder_channel_desc));
        manager.createNotificationChannel(channel);
    }
}
