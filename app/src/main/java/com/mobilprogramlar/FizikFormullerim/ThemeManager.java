package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.content.ContextCompat;

public class ThemeManager {
    private static final String THEME_KEY = "theme_key";
    private static final String PREFS_NAME = "AppThemePrefs";
    private final SharedPreferences prefs;
    private final Context context;

    public ThemeManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getTheme() {
        int themeId = prefs.getInt(THEME_KEY, R.style.Theme_Nurullah);
        if (!isKnownTheme(themeId)) {
            return R.style.Theme_Nurullah;
        }
        return themeId;
    }

    private boolean isKnownTheme(int themeId) {
        return themeId == R.style.Theme_Nurullah
                || themeId == R.style.Theme_Nadiye
                || themeId == R.style.Theme_Sude
                || themeId == R.style.Theme_Kubra
                || themeId == R.style.Theme_Zeynep
                || themeId == R.style.Theme_Irem
                || themeId == R.style.Theme_Ilknur
                || themeId == R.style.Theme_Rukiye
                || themeId == R.style.Theme_Hilal;
    }

    public void saveTheme(int themeId) {
        prefs.edit().putInt(THEME_KEY, themeId).apply();
    }

    public void applyTheme(Activity activity) {
        activity.setTheme(getTheme());
    }

    public void setTheme(Activity activity, int themeId) {
        saveTheme(themeId);
        CrashlyticsKeys.refresh(activity);
        activity.recreate();
    }

    public ThemeColors getThemeColors() {
        int themeId = getTheme();
        int activityBackgroundColor,activityTextColor,cardBackgroundColor,cardTextColor,btnBbackgroundColor,btnTextColor,toolbarBackgroundColor,toolbarTitleTextColor,toolbarSubtitleTextColor,textColor_1,textColor_2,uygulama_metin_rengi_01,uygulama_metin_rengi_02;

        // Varsayılan renkler
        activityBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_activityBackgroundColor);
        activityTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
        cardBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_cardBackgroundColor);
        cardTextColor = ContextCompat.getColor(context, R.color.nurullah_cardTextColor);
        btnBbackgroundColor = ContextCompat.getColor(context, R.color.nurullah_buttonBackgroundColor);
        btnTextColor = ContextCompat.getColor(context, R.color.nurullah_button_metin_rengi_01);
        toolbarBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_toolbar_rengi);
        toolbarTitleTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
        toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_02);
        textColor_1 = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
        textColor_2 = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_02);
        uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.nurullah_uygulama_metin_rengi_01);
        uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.nurullah_uygulama_metin_rengi_02);

        // Tema kontrolü
        if (themeId == R.style.Theme_Nurullah) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.nurullah_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.nurullah_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.nurullah_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.nurullah_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.nurullah_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Nadiye) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.nadiye_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.nadiye_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.nadiye_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.nadiye_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.nadiye_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.nadiye_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.nadiye_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.nadiye_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.nadiye_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.nadiye_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.nadiye_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.nadiye_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.nadiye_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Sude) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.sude_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.sude_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.sude_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.sude_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.sude_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.sude_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.sude_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.sude_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.sude_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.sude_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.sude_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.sude_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.sude_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Kubra) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.kubra_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.kubra_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.kubra_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.kubra_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.kubra_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.kubra_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.kubra_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.kubra_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.kubra_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.kubra_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.kubra_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.kubra_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.kubra_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Zeynep) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.zeynep_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.zeynep_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.zeynep_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.zeynep_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.zeynep_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.zeynep_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.zeynep_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.zeynep_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.zeynep_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.zeynep_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.zeynep_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.zeynep_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.zeynep_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Irem) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.irem_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.irem_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.irem_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.irem_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.irem_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.irem_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.irem_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.irem_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.irem_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.irem_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.irem_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.irem_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.irem_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Ilknur) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.ilknur_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.ilknur_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.ilknur_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.ilknur_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.ilknur_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.ilknur_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.ilknur_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.ilknur_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.ilknur_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.ilknur_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.ilknur_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.ilknur_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.ilknur_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Rukiye) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.rukiye_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.rukiye_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.rukiye_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.rukiye_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.rukiye_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.rukiye_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.rukiye_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.rukiye_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.rukiye_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.rukiye_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.rukiye_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.rukiye_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.rukiye_uygulama_metin_rengi_02);

        } else if (themeId == R.style.Theme_Hilal) {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.hilal_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.hilal_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.hilal_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.hilal_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.hilal_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.hilal_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.hilal_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.hilal_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.hilal_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.hilal_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.hilal_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.hilal_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.hilal_uygulama_metin_rengi_02);

        } else {
            activityBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_activityBackgroundColor);
            activityTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            cardBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_cardBackgroundColor);
            cardTextColor = ContextCompat.getColor(context, R.color.nurullah_cardTextColor);
            btnBbackgroundColor = ContextCompat.getColor(context, R.color.nurullah_buttonBackgroundColor);
            btnTextColor = ContextCompat.getColor(context, R.color.nurullah_button_metin_rengi_01);
            toolbarBackgroundColor = ContextCompat.getColor(context, R.color.nurullah_toolbar_rengi);
            toolbarTitleTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            toolbarSubtitleTextColor = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            textColor_1 = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_01);
            textColor_2 = ContextCompat.getColor(context, R.color.nurullah_metin_rengi_02);
            uygulama_metin_rengi_01 = ContextCompat.getColor(context, R.color.nurullah_uygulama_metin_rengi_01);
            uygulama_metin_rengi_02 = ContextCompat.getColor(context, R.color.nurullah_uygulama_metin_rengi_02);
        }

        return new ThemeColors(activityBackgroundColor,activityTextColor,cardBackgroundColor,cardTextColor,btnBbackgroundColor,btnTextColor,toolbarBackgroundColor,toolbarTitleTextColor,toolbarSubtitleTextColor,textColor_1,textColor_2,uygulama_metin_rengi_01,uygulama_metin_rengi_02);
    }
}

