package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Reklamsız (Premium) durumu; Play Billing doğrulaması sonrası güncellenir.
 */
public final class PremiumManager {

    private static final String PREFS = "mf_premium_prefs";
    private static final String KEY_AD_FREE = "ad_free_unlocked";

    private static volatile PremiumManager instance;
    private final Context appContext;
    private final SharedPreferences prefs;

    private PremiumManager(Context appContext) {
        this.appContext = appContext.getApplicationContext();
        prefs = this.appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static PremiumManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PremiumManager.class) {
                if (instance == null) {
                    instance = new PremiumManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public boolean isAdFree() {
        return prefs.getBoolean(KEY_AD_FREE, false);
    }

    /** Sadece paket içi (Billing) tarafından çağrılmalıdır. */
    void setAdFree(boolean adFree) {
        prefs.edit().putBoolean(KEY_AD_FREE, adFree).apply();
        CrashlyticsKeys.refresh(appContext);
    }
}
