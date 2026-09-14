package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.LocaleList;
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
        String tag = (language == null || language.isEmpty()) ? "tr" : language;
        Locale locale = Locale.forLanguageTag(tag);
        Locale.setDefault(locale);

        Context scaled = UiScale.wrap(context);
        Configuration config = new Configuration(scaled.getResources().getConfiguration());
        config.setLocales(new LocaleList(locale));
        config.fontScale = 1.0f;

        Context updatedContext = scaled.createConfigurationContext(config);
        Log.d("LocaleManager", "Yerel ayar şu şekilde güncellendi: " + tag);
        return updatedContext;
    }
}
