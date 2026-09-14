package com.mobilprogramlar.FizikFormullerim;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.messaging.FirebaseMessaging;

public class Ayarlar extends AppCompatActivity implements BillingManager.Listener {

    private LocaleManager localeManager;
    private ThemeManager themeManager;
    private TextView tvPremiumTitle;
    private TextView tvPremiumStatus;
    private Button btnPremiumBuy;
    private Button btnPremiumRestore;
    private SwitchMaterial switchAnnouncements;
    private SwitchMaterial switchStudyReminder;

    @Override
    protected void attachBaseContext(Context newBase) {
        localeManager = new LocaleManager(newBase);
        Context context = localeManager.setLocale(newBase);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.applyTheme(this);
        super.onCreate(savedInstanceState);

        NtHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_ayarlar);
        NtHelper.applySystemBarInsets(this);

        tvPremiumTitle = null;
        tvPremiumStatus = findViewById(R.id.tv_premium_status);
        btnPremiumBuy = findViewById(R.id.btn_premium_buy);
        btnPremiumRestore = findViewById(R.id.btn_premium_restore);

        // Toolbar'ı ayarla
        setupToolbar();
        // Aktivite bileşenlerini ayarla
        setupActivityComponents();
        // Butonları ayarla
        setupButtons();

        // Seçili temayı alıp güncellemek için `updateUIComponents` çağrılır
        updateUIComponents(themeManager.getTheme());

        // Tema değişikliklerini ayarla
        setupThemeChangeListener();
        // Dil değişikliklerini ayarla
        setupLanguageChangeListener();

        // Geri butonu için callback oluşturulur, geri butonuna basıldığında ne olacağını tanımlar.
        NtHelper.setOnBackPressed(this, MainActivity.class);

        // İlk açılışta temayı oku ve ilgili RadioButton'u seçili yap
        selectInitialTheme();

        setupPremiumSection();
        setupNotificationPrefs();
        BillingManager billing = ((App) getApplication()).getBillingManager();
        billing.setListener(this);
        billing.queryPurchasesAndApply();
        refreshPremiumUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((App) getApplication()).getBillingManager().queryPurchasesAndApply();
        refreshPremiumUi();
    }

    @Override
    protected void onDestroy() {
        ((App) getApplication()).getBillingManager().setListener(null);
        super.onDestroy();
    }

    private void setupNotificationPrefs() {
        switchAnnouncements = findViewById(R.id.switch_announcements);
        switchStudyReminder = findViewById(R.id.switch_study_reminder);
        if (switchAnnouncements == null || switchStudyReminder == null) {
            return;
        }
        switchAnnouncements.setChecked(NotificationPrefs.areAnnouncementsEnabled(this));
        switchStudyReminder.setChecked(NotificationPrefs.isStudyReminderEnabled(this));

        switchAnnouncements.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationPrefs.setAnnouncementsEnabled(this, isChecked);
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("all_users");
                FirebaseMessaging.getInstance().subscribeToTopic("fizik_formulleri");
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all_users");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("fizik_formulleri");
            }
        });
        switchStudyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationPrefs.setStudyReminderEnabled(this, isChecked);
            StudyReminderScheduler.applyFromPrefs(this);
            Toast.makeText(this,
                    isChecked ? R.string.pref_study_reminder_on : R.string.pref_study_reminder_off,
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setupPremiumSection() {
        if (btnPremiumBuy != null) {
            btnPremiumBuy.setText(R.string.settings_buy_button);
            btnPremiumBuy.setOnClickListener(v ->
                    ((App) getApplication()).getBillingManager().launchPurchaseFlow(this));
        }
        if (btnPremiumRestore != null) {
            btnPremiumRestore.setText(R.string.settings_restore_button);
            btnPremiumRestore.setOnClickListener(v -> {
                ((App) getApplication()).getBillingManager().queryPurchasesAndApply();
                Toast.makeText(this, getString(R.string.premium_restored), Toast.LENGTH_SHORT).show();
            });
        }
        Button btnOdulluMola = findViewById(R.id.btn_odullu_mola);
        if (btnOdulluMola != null) {
            btnOdulluMola.setText(R.string.settings_watch_button);
            btnOdulluMola.setOnClickListener(v -> RewardedAdHelper.showForAdPause(this));
        }
    }

    private void refreshPremiumUi() {
        boolean adFree = PremiumManager.getInstance(this).isAdFree();
        boolean paused = AppPrefs.isAdsPaused(this);
        boolean showRemove = AdGate.isRemoveAdsButtonEnabled(this);
        int hours = AppRemoteConfig.getInstance(this).getRewardedPauseHours();

        TextView buyHint = findViewById(R.id.tv_settings_buy_hint);
        TextView watchHint = findViewById(R.id.tv_settings_watch_hint);
        if (buyHint != null) {
            buyHint.setText(R.string.settings_buy_hint);
            buyHint.setVisibility(adFree ? View.GONE : View.VISIBLE);
        }
        if (watchHint != null) {
            if (paused) {
                String left = AppPrefs.formatAdsPauseRemaining(this);
                watchHint.setText(getString(R.string.settings_watch_active_fmt, left));
                watchHint.setVisibility(View.VISIBLE);
            } else if (RewardedAdHelper.canOfferPause(this) || AppRemoteConfig.getInstance(this).isRewardedEnabled()) {
                watchHint.setText(getString(R.string.settings_watch_hint_fmt, hours));
                watchHint.setVisibility(View.VISIBLE);
            } else {
                watchHint.setVisibility(View.GONE);
            }
        }

        if (tvPremiumStatus != null) {
            if (adFree) {
                tvPremiumStatus.setText(R.string.reklamlar_kaldirildi);
            } else if (paused) {
                tvPremiumStatus.setText(getString(R.string.settings_watch_active_fmt,
                        AppPrefs.formatAdsPauseRemaining(this)));
            } else {
                tvPremiumStatus.setText("");
            }
        }

        if (btnPremiumBuy != null) {
            String price = ((App) getApplication()).getBillingManager().getFormattedPrice();
            if (!adFree && price != null) {
                btnPremiumBuy.setText(getString(R.string.settings_buy_button) + " — " + price);
            } else {
                btnPremiumBuy.setText(R.string.settings_buy_button);
            }
            btnPremiumBuy.setEnabled(!adFree && showRemove);
            btnPremiumBuy.setVisibility((!adFree && showRemove) ? View.VISIBLE : View.GONE);
        }
        if (btnPremiumRestore != null) {
            btnPremiumRestore.setVisibility(showRemove ? View.VISIBLE : View.GONE);
        }

        Button btnOdulluMola = findViewById(R.id.btn_odullu_mola);
        if (btnOdulluMola != null) {
            boolean offer = !adFree && AppRemoteConfig.getInstance(this).isRewardedEnabled();
            btnOdulluMola.setVisibility(offer ? View.VISIBLE : View.GONE);
            btnOdulluMola.setEnabled(!paused);
            btnOdulluMola.setText(paused
                    ? getString(R.string.ads_fab_break_active)
                    : getString(R.string.settings_watch_button));
        }
    }

    @Override
    public void onPremiumStateChanged(boolean isAdFree) {
        runOnUiThread(this::refreshPremiumUi);
    }

    @Override
    public void onBillingMessage(@NonNull String message) {
        runOnUiThread(() -> Toast.makeText(Ayarlar.this, message, Toast.LENGTH_SHORT).show());
    }

    private void setupThemeChangeListener() {
        RadioGroup themeGroup = findViewById(R.id.theme_group);
        if (themeGroup != null) {
            themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
                int selectedTheme = getThemeId(checkedId);
                if (selectedTheme != themeManager.getTheme()) {
                    themeManager.setTheme(this, selectedTheme);
                    applyThemeDynamically(selectedTheme);
                    animateRadioButtonChange();
                }
            });
        }
    }

    private int getThemeId(int checkedId) {
        int themeId = themeManager.getTheme();
        if (checkedId == R.id.radio_nurullah) {
            themeId = R.style.Theme_Nurullah;
        } else if (checkedId == R.id.radio_nadiye) {
            themeId = R.style.Theme_Nadiye;
        } else if (checkedId == R.id.radio_sude) {
            themeId = R.style.Theme_Sude;
        } else if (checkedId == R.id.radio_kubra) {
            themeId = R.style.Theme_Kubra;
        } else if (checkedId == R.id.radio_zeynep) {
            themeId = R.style.Theme_Zeynep;
        } else if (checkedId == R.id.radio_irem) {
            themeId = R.style.Theme_Irem;
        } else if (checkedId == R.id.radio_ilknur) {
            themeId = R.style.Theme_Ilknur;
        } else if (checkedId == R.id.radio_rukiye) {
            themeId = R.style.Theme_Rukiye;
        } else if (checkedId == R.id.radio_hilal) {
            themeId = R.style.Theme_Hilal;
        }
        return themeId;
    }

    private void applyThemeDynamically(int themeId) {
        setTheme(themeId);
        updateUIComponents(themeId);
    }

    private void setupActivityComponents() {
        setupThemeGroupLabels(R.id.theme_group, new int[]{
                R.string.tema_nurullah,
                R.string.tema_nadiye,
                R.string.tema_sude,
                R.string.tema_kubra,
                R.string.tema_zeynep,
                R.string.tema_irem,
                R.string.tema_ilknur,
                R.string.tema_rukiye,
                R.string.tema_hilal
        }, new int[]{
                R.string.tema_nurullah_altmetin,
                R.string.tema_nadiye_altmetin,
                R.string.tema_sude_altmetin,
                R.string.tema_kubra_altmetin,
                R.string.tema_zeynep_altmetin,
                R.string.tema_irem_altmetin,
                R.string.tema_ilknur_altmetin,
                R.string.tema_rukiye_altmetin,
                R.string.tema_hilal_altmetin
        });

        setupLanguageGroupLabels(R.id.language_group, new int[]{
                R.string.dil_sec_en,
                R.string.dil_sec_tr
        });

        setTextViewText(R.id.tv_tema_sec, R.string.uygulama_tema_sec);
        setTextViewText(R.id.tv_dilsec, R.string.dil_sec);
        ToolbarHelper.bindPage(this, getString(R.string.toolbar_ayarlar));

        View btnWidgets = findViewById(R.id.btn_settings_widgets);
        if (btnWidgets != null) {
            btnWidgets.setOnClickListener(v -> WidgetPinHelper.showPicker(this));
        }
        View btnRate = findViewById(R.id.btn_settings_rate);
        if (btnRate != null) {
            btnRate.setOnClickListener(v -> ReviewHelper.askFromButton(this));
        }
    }

    private void setupThemeGroupLabels(int radioGroupId, int[] labelIds, int[] descriptionIds) {
        RadioGroup radioGroup = findViewById(radioGroupId);
        if (radioGroup != null) {
            int index = 0;
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (radioGroup.getChildAt(i) instanceof RadioButton && index < labelIds.length) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    radioButton.setText(getResources().getString(labelIds[index]));
                    if (index < descriptionIds.length) {
                        TextView textView = (TextView) radioGroup.getChildAt(i + 1);
                        textView.setText(getResources().getString(descriptionIds[index]));
                    }
                    index++;
                } else if (radioGroup.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayout = (LinearLayout) radioGroup.getChildAt(i);
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        if (linearLayout.getChildAt(j) instanceof RadioButton && index < labelIds.length) {
                            RadioButton radioButton = (RadioButton) linearLayout.getChildAt(j);
                            radioButton.setText(getResources().getString(labelIds[index]));
                            if (j + 1 < linearLayout.getChildCount() && linearLayout.getChildAt(j + 1) instanceof TextView) {
                                TextView textView = (TextView) linearLayout.getChildAt(j + 1);
                                textView.setText(getResources().getString(descriptionIds[index]));
                            }
                            index++;
                        }
                    }
                }
            }
        }
    }

    private void setupLanguageGroupLabels(int radioGroupId, int[] labelIds) {
        RadioGroup radioGroup = findViewById(radioGroupId);
        if (radioGroup != null) {
            int index = 0;
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (radioGroup.getChildAt(i) instanceof RadioButton && index < labelIds.length) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    radioButton.setText(getResources().getString(labelIds[index]));
                    index++;
                }
            }
        }
    }

    private void setTextViewText(int textViewId, int stringId) {
        TextView textView = findViewById(textViewId);
        if (textView != null) {
            textView.setText(getResources().getString(stringId));
        }
    }

    private void updateUIComponents(int themeId) {
        ThemeColors themeColors = themeManager.getThemeColors();

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.toolbar_title);
        TextView tvToolbarSubtitle = findViewById(R.id.toolbar_subtitle);

        ConstraintLayout constraintLayout1 = findViewById(R.id.constraintlayout_1);
        LinearLayout linearLayout1 = findViewById(R.id.linearlayout_1);
        LinearLayout linearLayout3 = findViewById(R.id.linearlayout_3);
        LinearLayout linearLayout4 = findViewById(R.id.linearlayout_4);
        LinearLayout linearLayoutPrivacy = findViewById(R.id.linearlayout_privacy);
        LinearLayout engageCard = findViewById(R.id.card_settings_engage);
        TextView tvPrivacy = findViewById(R.id.tv_privacy);
        TextView tvTemaSec = findViewById(R.id.tv_tema_sec);
        TextView tvDilSec = findViewById(R.id.tv_dilsec);
        Button btnPrivacy = findViewById(R.id.btn_privacy);

        toolbar.setBackgroundColor(themeColors.toolbarBackgroundColor);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setTextColor(themeColors.toolbarTitleTextColor);
        }
        if (tvToolbarSubtitle != null) {
            tvToolbarSubtitle.setTextColor(
                    androidx.core.content.ContextCompat.getColor(this, R.color.toolbar_subtitle_readable));
        }
        constraintLayout1.setBackgroundColor(themeColors.activityBackgroundColor);
        linearLayout1.setBackgroundColor(themeColors.activityBackgroundColor);
        if (engageCard != null) {
            engageCard.setBackgroundColor(themeColors.cardBackgroundColor);
        }
        linearLayout3.setBackgroundColor(themeColors.cardBackgroundColor);
        linearLayout4.setBackgroundColor(themeColors.cardBackgroundColor);
        if (linearLayoutPrivacy != null) {
            linearLayoutPrivacy.setBackgroundColor(themeColors.cardBackgroundColor);
        }
        if (tvPrivacy != null) {
            tvPrivacy.setTextColor(themeColors.activityTextColor);
        }
        if (btnPrivacy != null) {
            btnPrivacy.setBackgroundColor(themeColors.toolbarBackgroundColor);
            btnPrivacy.setTextColor(themeColors.activityTextColor);
        }
        int textColor = themeColors.activityTextColor;
        int iconColor = themeColors.cardTextColor != 0 ? themeColors.cardTextColor : textColor;
        int[] engageTextIds = {
                R.id.tv_settings_ads_section,
                R.id.tv_settings_buy_hint,
                R.id.tv_settings_watch_hint,
                R.id.tv_settings_widgets_title,
                R.id.tv_settings_widgets_summary,
                R.id.tv_settings_rate_title,
                R.id.tv_settings_rate_summary,
                R.id.tv_premium_status
        };
        for (int id : engageTextIds) {
            TextView tv = findViewById(id);
            if (tv != null) {
                tv.setTextColor(textColor);
            }
        }
        tintImage(R.id.icon_settings_widgets, iconColor);
        tintImage(R.id.icon_settings_rate, iconColor);
        tintImage(R.id.chevron_widgets, iconColor);
        tintImage(R.id.chevron_rate, iconColor);
        if (tvPremiumStatus != null) {
            tvPremiumStatus.setTextColor(themeColors.activityTextColor);
        }
        if (btnPremiumBuy != null) {
            btnPremiumBuy.setBackgroundColor(themeColors.toolbarBackgroundColor);
            btnPremiumBuy.setTextColor(themeColors.activityTextColor);
        }
        if (btnPremiumRestore != null) {
            btnPremiumRestore.setBackgroundColor(themeColors.toolbarBackgroundColor);
            btnPremiumRestore.setTextColor(themeColors.activityTextColor);
        }
        Button btnOdulluMola = findViewById(R.id.btn_odullu_mola);
        if (btnOdulluMola != null) {
            btnOdulluMola.setBackgroundColor(themeColors.toolbarBackgroundColor);
            btnOdulluMola.setTextColor(themeColors.activityTextColor);
        }
        TextView tvNotifTitle = findViewById(R.id.tv_notifications_title);
        TextView tvPrefAnn = findViewById(R.id.tv_pref_announcements);
        TextView tvPrefStudy = findViewById(R.id.tv_pref_study_reminder);
        TextView tvPrefHint = findViewById(R.id.tv_pref_study_hint);
        LinearLayout layoutNotif = findViewById(R.id.linearlayout_notifications);
        if (layoutNotif != null) {
            layoutNotif.setBackgroundColor(themeColors.cardBackgroundColor);
        }
        if (tvNotifTitle != null) {
            tvNotifTitle.setTextColor(themeColors.activityTextColor);
        }
        if (tvPrefAnn != null) {
            tvPrefAnn.setTextColor(themeColors.activityTextColor);
        }
        if (tvPrefStudy != null) {
            tvPrefStudy.setTextColor(themeColors.activityTextColor);
        }
        if (tvPrefHint != null) {
            tvPrefHint.setTextColor(themeColors.activityTextColor);
        }
        if (tvTemaSec != null) {
            tvTemaSec.setTextColor(themeColors.activityTextColor);
        }
        if (tvDilSec != null) {
            tvDilSec.setTextColor(themeColors.activityTextColor);
        }

        applyRadioGroupColors(R.id.theme_group, themeColors);
        applyRadioGroupColors(R.id.language_group, themeColors);
    }

    private void tintImage(int id, int color) {
        android.widget.ImageView iv = findViewById(id);
        if (iv != null) {
            iv.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private void applyRadioGroupColors(int radioGroupId, ThemeColors themeColors) {
        RadioGroup radioGroup = findViewById(radioGroupId);
        if (radioGroup != null) {
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            {android.R.attr.state_checked},
                            {-android.R.attr.state_checked}
                    },
                    new int[]{
                            themeColors.btnBackgroundColor,
                            Color.GRAY
                    }
            );

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (radioGroup.getChildAt(i) instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    radioButton.setTextColor(themeColors.activityTextColor);
                    radioButton.setButtonTintList(colorStateList);
                } else if (radioGroup.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayout = (LinearLayout) radioGroup.getChildAt(i);
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        if (linearLayout.getChildAt(j) instanceof RadioButton) {
                            RadioButton radioButton = (RadioButton) linearLayout.getChildAt(j);
                            radioButton.setTextColor(themeColors.activityTextColor);
                            radioButton.setButtonTintList(colorStateList);
                        }
                    }
                }
            }
        }
    }

    private void setupLanguageChangeListener() {
        RadioGroup languageGroup = findViewById(R.id.language_group);
        if (languageGroup != null) {
            String currentLang = localeManager.getLanguage();
            if ("tr".equals(currentLang)) {
                languageGroup.check(R.id.radio_turkish);
            } else {
                languageGroup.check(R.id.radio_english);
            }
            languageGroup.setOnCheckedChangeListener((group, checkedId) -> {
                String newLanguage = checkedId == R.id.radio_english ? "en" : "tr";
                if (!newLanguage.equals(localeManager.getLanguage())) {
                    localeManager.saveLanguage(newLanguage);
                    CrashlyticsKeys.refresh(Ayarlar.this);
                    // Tüm ekranların yeni dile geçmesi için ana sayfadan başlat
                    Intent intent = new Intent(Ayarlar.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void selectInitialTheme() {
        int selectedTheme = themeManager.getTheme();
        RadioGroup themeGroup = findViewById(R.id.theme_group);
        if (themeGroup != null) {
            if (selectedTheme == R.style.Theme_Nurullah) {
                themeGroup.check(R.id.radio_nurullah);
            } else if (selectedTheme == R.style.Theme_Nadiye) {
                themeGroup.check(R.id.radio_nadiye);
            } else if (selectedTheme == R.style.Theme_Sude) {
                themeGroup.check(R.id.radio_sude);
            } else if (selectedTheme == R.style.Theme_Kubra) {
                themeGroup.check(R.id.radio_kubra);
            } else if (selectedTheme == R.style.Theme_Zeynep) {
                themeGroup.check(R.id.radio_zeynep);
            } else if (selectedTheme == R.style.Theme_Irem) {
                themeGroup.check(R.id.radio_irem);
            } else if (selectedTheme == R.style.Theme_Ilknur) {
                themeGroup.check(R.id.radio_ilknur);
            } else if (selectedTheme == R.style.Theme_Rukiye) {
                themeGroup.check(R.id.radio_rukiye);
            } else if (selectedTheme == R.style.Theme_Hilal) {
                themeGroup.check(R.id.radio_hilal);
            } else {
                themeGroup.check(R.id.radio_nurullah);
            }
        }
    }

    private void animateRadioButtonChange() {
        RadioGroup themeGroup = findViewById(R.id.theme_group);
        if (themeGroup != null) {
            for (int i = 0; i < themeGroup.getChildCount(); i++) {
                if (themeGroup.getChildAt(i) instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) themeGroup.getChildAt(i);
                    AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(1500);
                    radioButton.startAnimation(animation);
                }
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ToolbarHelper.bindPage(this, getString(R.string.toolbar_ayarlar));
    }

    private void setupButtons() {
        ImageButton btnSettings = findViewById(R.id.btn_settings);
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnShare = findViewById(R.id.btn_share);

        setButtonClickListener(btnSettings, Ayarlar.class);
        setButtonClickListener(btnHome, MainActivity.class);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> NtHelper.shareText(this));
        }

        Button btnPrivacy = findViewById(R.id.btn_privacy);
        if (btnPrivacy != null) {
            btnPrivacy.setOnClickListener(v -> NtHelper.openWebPage(this,
                    AppRemoteConfig.getInstance(this).getPrivacyUrl(this)));
        }
    }

    private void setButtonClickListener(ImageButton button, Class<?> targetActivity) {
        if (button != null) {
            button.setOnClickListener(v -> NtHelper.startActivity(this, targetActivity));
        }
    }




    private AlertDialog createRateDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        builder.setTitle(getResources().getString(R.string.oylama_soru));
        builder.setMessage(getResources().getString(R.string.oylama_lutfen));

        builder.setPositiveButton(getResources().getString(R.string.oylama_oyla), (dialog, which) -> {
            // Önce uygulama içi puan; olmazsa Play Store sayfası
            InAppReviewHelper.requestReviewNow(Ayarlar.this, this::openPlayStoreListing);
        });

        builder.setNegativeButton(getResources().getString(R.string.oylama_vazgec), (dialog, which) -> dialog.dismiss());
        builder.setNeutralButton(getResources().getString(R.string.oylama_sonra), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        return dialog;
    }

    /** Play Store uygulama sayfası (In-App Review API: dependency com.google.android.play:review + ReviewManagerFactory). */
    void openPlayStoreListing() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void showRateDialog() {
        AlertDialog dialog = createRateDialog(this);
        dialog.show();

        // Diyalog başlığı ve mesajı özelleştir
        TextView titleView = dialog.findViewById(android.R.id.title);
        TextView messageView = dialog.findViewById(android.R.id.message);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        if (titleView != null) {
            titleView.setTextColor(Color.MAGENTA);
            titleView.setShadowLayer(1.5f, -1, 1, Color.GRAY);  // Gölge efekti
        }

        if (messageView != null) {
            messageView.setTextColor(Color.WHITE);
            messageView.setShadowLayer(1.5f, -1, 1, Color.GRAY);  // Gölge efekti
        }

        if (positiveButton != null) {
            positiveButton.setTextColor(Color.GREEN);
            positiveButton.setShadowLayer(1.5f, -1, 1, Color.GRAY);  // Gölge efekti
        }

        if (negativeButton != null) {
            negativeButton.setTextColor(Color.RED);
            negativeButton.setShadowLayer(1.5f, -1, 1, Color.GRAY);  // Gölge efekti
        }

        if (neutralButton != null) {
            neutralButton.setTextColor(Color.YELLOW);
            neutralButton.setShadowLayer(1.5f, -1, 1, Color.GRAY);  // Gölge efekti
        }

        // Özelleştirilmiş diyalog arka planı ve gölge
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.7f);  // Gölge seviyesi
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }


}
