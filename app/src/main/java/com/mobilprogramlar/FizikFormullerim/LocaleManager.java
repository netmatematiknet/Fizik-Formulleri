package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

public class LocaleManager {
    private static final String LANGUAGE_KEY = "language_key";
    private static final String PREFS_NAME = "AppPrefs";
    private final SharedPreferences prefs;

    public LocaleManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getLanguage() {
        return prefs.getString(LANGUAGE_KEY, "tr");
    }

    public void saveLanguage(String lang) {
        prefs.edit().putString(LANGUAGE_KEY, lang).apply();
    }

    public Context setLocale(Context context) {
        return updateResources(context, getLanguage());
    }

    Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        Context updatedContext = context.createConfigurationContext(config);
        Log.d("LocaleManager", "Yerel ayar şu şekilde güncellendi: " + language);
        return updatedContext;
    }
}
