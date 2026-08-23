package com.mobilprogramlar.FizikFormullerim;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.perf.FirebasePerformance;

/**
 * Firebase Analytics, Crashlytics, Performance, Messaging ve Remote Config
 * uygulama açılışında bir kez başlatılır. AdMob, UMP izninden sonra açılır.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);
        AppRemoteConfig.getInstance(this).fetchAndActivate();
        createFcmChannel();
        FirebaseMessaging.getInstance().subscribeToTopic("all_users");
        FirebaseMessaging.getInstance().subscribeToTopic("fizik_formulleri");
        CrashlyticsKeys.refresh(this);
    }

    private void createFcmChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager == null) {
            return;
        }
        String channelId = getString(R.string.default_notification_channel_id);
        NotificationChannel channel = new NotificationChannel(
                channelId,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(getString(R.string.notification_channel_desc));
        manager.createNotificationChannel(channel);
    }
}
