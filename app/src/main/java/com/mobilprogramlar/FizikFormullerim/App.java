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
 * Tek seferlik başlatma: Firebase, Crashlytics, Performance, Messaging, Remote Config, Billing.
 * AdMob, UMP consent sonrası MainActivity'de başlatılır.
 */
public class App extends Application {

    private BillingManager billingManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);
        AppRemoteConfig.getInstance(this).fetchAndActivate();
        createFcmChannel();
        FirebaseMessaging.getInstance().subscribeToTopic("all_users");
        FirebaseMessaging.getInstance().subscribeToTopic("fizik_formulleri");
        PremiumManager.getInstance(this);
        CrashlyticsKeys.refresh(this);
        billingManager = new BillingManager(this);
        billingManager.connectAndSync();
    }

    public BillingManager getBillingManager() {
        return billingManager;
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
