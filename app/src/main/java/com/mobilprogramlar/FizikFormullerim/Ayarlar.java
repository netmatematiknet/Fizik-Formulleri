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

        tvPremiumTitle = findViewById(R.id.tv_premium_title);
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
        tvPremiumTitle.setText(getString(R.string.premium_section_title));
        btnPremiumBuy.setText(getString(R.string.premium_buy_button));
        btnPremiumRestore.setText(getString(R.string.premium_restore_button));
        btnPremiumBuy.setOnClickListener(v -> ((App) getApplication()).getBillingManager().launchPurchaseFlow(this));
        btnPremiumRestore.setOnClickListener(v -> {
            ((App) getApplication()).getBillingManager().queryPurchasesAndApply();
            Toast.makeText(this, getString(R.string.premium_restored), Toast.LENGTH_SHORT).show();
        });
    }

    private void refreshPremiumUi() {
        boolean adFree = PremiumManager.getInstance(this).isAdFree();
        tvPremiumStatus.setText(adFree ? getString(R.string.premium_status_active) : getString(R.string.premium_status_inactive));
        String price = ((App) getApplication()).getBillingManager().getFormattedPrice();
        if (!adFree && price != null) {
            btnPremiumBuy.setText(getString(R.string.premium_buy_button) + " — " + price);
        } else {
            btnPremiumBuy.setText(getString(R.string.premium_buy_button));
        }
        btnPremiumBuy.setEnabled(!adFree);
        btnPremiumBuy.setVisibility(adFree ? View.GONE : View.VISIBLE);
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

        setTextViewText(R.id.tv_oyver, R.string.uygulama_oyver);
        setTextViewText(R.id.tv_tema_sec, R.string.uygulama_tema_sec);
        setTextViewText(R.id.tv_dilsec, R.string.dil_sec);
        setTextViewText(R.id.text_container, R.string.toolbar_baslik_default);

        Button btnOyver = findViewById(R.id.btn_oyver);
        btnOyver.setText(getResources().getString(R.string.uygulama_oyver_butonu));
        btnOyver.setOnClickListener(v -> showRateDialog());
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
        TextView tvToolbarContainer = findViewById(R.id.text_container);

        ConstraintLayout constraintLayout1 = findViewById(R.id.constraintlayout_1);
        LinearLayout linearLayout1 = findViewById(R.id.linearlayout_1);
        LinearLayout linearLayout2 = findViewById(R.id.linearlayout_2);
        LinearLayout linearLayout3 = findViewById(R.id.linearlayout_3);
        LinearLayout linearLayout4 = findViewById(R.id.linearlayout_4);
        LinearLayout linearLayoutPremium = findViewById(R.id.linearlayout_premium);
        LinearLayout linearLayoutPrivacy = findViewById(R.id.linearlayout_privacy);
        TextView tvOyver = findViewById(R.id.tv_oyver);
        TextView tvPrivacy = findViewById(R.id.tv_privacy);
        TextView tvTemaSec = findViewById(R.id.tv_tema_sec);
        TextView tvDilSec = findViewById(R.id.tv_dilsec);
        Button btnOyver = findViewById(R.id.btn_oyver);
        Button btnPrivacy = findViewById(R.id.btn_privacy);

        toolbar.setBackgroundColor(themeColors.toolbarBackgroundColor);
        tvToolbarContainer.setTextColor(themeColors.toolbarTitleTextColor);
        constraintLayout1.setBackgroundColor(themeColors.activityBackgroundColor);
        linearLayout1.setBackgroundColor(themeColors.activityBackgroundColor);
        linearLayout2.setBackgroundColor(themeColors.cardBackgroundColor);
        linearLayout3.setBackgroundColor(themeColors.cardBackgroundColor);
        linearLayout4.setBackgroundColor(themeColors.cardBackgroundColor);
        if (linearLayoutPremium != null) {
            linearLayoutPremium.setBackgroundColor(themeColors.cardBackgroundColor);
        }
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
        if (tvPremiumTitle != null) {
            tvPremiumTitle.setTextColor(themeColors.activityTextColor);
        }
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
        tvOyver.setTextColor(themeColors.activityTextColor);
        tvTemaSec.setTextColor(themeColors.activityTextColor);
        tvDilSec.setTextColor(themeColors.activityTextColor);
        btnOyver.setBackgroundColor(themeColors.toolbarBackgroundColor);

        applyRadioGroupColors(R.id.theme_group, themeColors);
        applyRadioGroupColors(R.id.language_group, themeColors);
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
                    recreate();
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
        TextView toolbarTitle = findViewById(R.id.text_container);
        if (toolbarTitle != null) {
            toolbarTitle.setText(getResources().getString(R.string.toolbar_baslik_default));
        } else {
            Log.d("Ayarlar", "toolbarTitle BOŞ");
        }
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
            btnPrivacy.setOnClickListener(v -> NtHelper.openWebPage(this, getString(R.string.privacy_policy_url)));
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
            if (context instanceof Ayarlar) {
                ((Ayarlar) context).openPlayStoreListing();
            }
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
