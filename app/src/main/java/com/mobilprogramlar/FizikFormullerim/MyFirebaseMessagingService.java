package com.mobilprogramlar.FizikFormullerim;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Firebase Console → Messaging ile gönderilen bildirimler.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        if (!NotificationPrefs.areAnnouncementsEnabled(this)) {
            return;
        }
        FirebaseMessaging.getInstance().subscribeToTopic("all_users");
        FirebaseMessaging.getInstance().subscribeToTopic("fizik_formulleri");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (!NotificationPrefs.areAnnouncementsEnabled(this)) {
            return;
        }

        RemoteMessage.Notification n = message.getNotification();
        Map<String, String> data = message.getData();

        String title = n != null && n.getTitle() != null ? n.getTitle() : data.get("title");
        String body = n != null && n.getBody() != null ? n.getBody() : data.get("body");
        if (body == null || body.isEmpty()) {
            body = data.get("message");
        }
        if (body == null || body.isEmpty()) {
            return;
        }
        if (title == null || title.isEmpty()) {
            title = getString(R.string.manifest_activity_splash_app_name);
        }
        showNotification(title, body);
    }

    private void showNotification(@NonNull String title, @NonNull String body) {
        String channelId = getString(R.string.default_notification_channel_id);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) {
            return;
        }
        ensureChannel(this, manager, channelId);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);

        android.app.Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        manager.notify((int) (System.currentTimeMillis() % Integer.MAX_VALUE), notification);
    }

    static void ensureChannel(@NonNull android.content.Context context,
                              @NonNull NotificationManager manager,
                              @NonNull String channelId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        if (manager.getNotificationChannel(channelId) != null) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(
                channelId,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(context.getString(R.string.notification_channel_desc));
        manager.createNotificationChannel(channel);
    }
}
