package com.mobilprogramlar.FizikFormullerim;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.perf.FirebasePerformance;

/**
 * Tek seferlik başlatma: Firebase, Crashlytics, Performance, Messaging, Remote Config, Billing.
 * AdMob, UMP consent sonrası MainActivity'de başlatılır.
 */
public class App extends Application {

    private static final String TAG = "FizikApp";
    private static final String PREFS = "firebase_bootstrap";
    private static final String KEY_CRASHLYTICS_PROBE = "crashlytics_probe_v21";

    private BillingManager billingManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCrashlyticsCollectionEnabled(true);
        crashlytics.setCustomKey("app_version_name", BuildConfig.VERSION_NAME);
        crashlytics.setCustomKey("app_version_code", BuildConfig.VERSION_CODE);
        crashlytics.log("App.onCreate " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        analytics.setAnalyticsCollectionEnabled(true);
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);

        AppRemoteConfig.getInstance(this).fetchAndActivate();
        createFcmChannel();

        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
                .addOnCompleteListener(task -> Log.d(TAG, "topic all_users ok=" + task.isSuccessful()));
        FirebaseMessaging.getInstance().subscribeToTopic("fizik_formulleri")
                .addOnCompleteListener(task -> Log.d(TAG, "topic fizik_formulleri ok=" + task.isSuccessful()));

        PremiumManager.getInstance(this);
        CrashlyticsKeys.refresh(this);
        maybeSendCrashlyticsProbe(crashlytics);

        billingManager = new BillingManager(this);
        billingManager.connectAndSync();
    }

    public BillingManager getBillingManager() {
        return billingManager;
    }

    /**
     * Crashlytics konsolu "waiting for a crash" gösterir çünkü henüz çökme yok.
     * Tek seferlik non-fatal, SDK'nın veri gönderdiğini doğrular (kullanıcıya görünmez).
     */
    private void maybeSendCrashlyticsProbe(FirebaseCrashlytics crashlytics) {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_CRASHLYTICS_PROBE, false)) {
            return;
        }
        Exception probe = new Exception("CrashlyticsConnectivityProbe_v2.1");
        crashlytics.recordException(probe);
        crashlytics.sendUnsentReports();
        prefs.edit().putBoolean(KEY_CRASHLYTICS_PROBE, true).apply();
        Log.i(TAG, "Crashlytics connectivity probe queued");
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
