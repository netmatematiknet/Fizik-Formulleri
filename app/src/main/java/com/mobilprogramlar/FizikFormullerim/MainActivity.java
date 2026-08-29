package com.mobilprogramlar.FizikFormullerim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String LAST_SHOW_DATE_KEY = "LastShowDate";
    private static final String FIRST_TIME_KEY = "FirstTime_MainActivity";

    private ThemeManager themeManager;
    private LocaleManager localeManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextToSpeech tts;
    private String selectedLanguage;
    private boolean previousAdFree;
    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            NotificationPermissionHelper.register(this);

    @Override
    protected void attachBaseContext(Context newBase) {
        LocaleManager lm = new LocaleManager(newBase);
        super.attachBaseContext(lm.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.applyTheme(this);
        super.onCreate(savedInstanceState);

        localeManager = new LocaleManager(this);
        Context context = localeManager.updateResources(this, localeManager.getLanguage());
        selectedLanguage = localeManager.getLanguage();

        NtHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_main);
        NtHelper.applySystemBarInsets(this);
        AdConsentHelper.gatherConsentAndInitAds(this, () -> {
            AdHelper.loadInterstitialAd(this);
            AdHelper.showAdWithProbability(
                    this, AppRemoteConfig.getInstance(this).getInterstitialMainPercent());
        });
        NotificationPermissionHelper.requestIfNeeded(this, notificationPermissionLauncher);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenParams = new Bundle();
        screenParams.putString(FirebaseAnalytics.Param.SCREEN_NAME, "MainActivity");
        screenParams.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, screenParams);

        previousAdFree = PremiumManager.getInstance(this).isAdFree();

        setupButtons(context);
        updateUIComponents(themeManager.getTheme());

        NtHelper.setOnBackPressed(this, MainActivity.class);

        setupRecyclerView(context);

        // Initialize TextToSpeech
        tts = new TextToSpeech(this, this); // TextToSpeech başlatıldı.

        // Rastgele metni ve tarihi seçip okuma
        //readRandomTextAndDate(); // Başlatma onInit'den sonra yapılacak
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = selectedLanguage.equals("tr") ? new Locale("tr", "TR") : Locale.ENGLISH;
            int result = tts.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Dil desteklenmiyor veya eksik veri."); // Hata kaydı eklendi
                if (selectedLanguage.equals("tr")) {
                    Log.e(TAG, "Türkçe dili desteklenmiyor veya eksik veri."); // Türkçe dilinin desteklenmediğini belirtir
                } else {
                    Log.e(TAG, "İngilizce dili desteklenmiyor veya eksik veri."); // İngilizce dilinin desteklenmediğini belirtir
                }
            } else {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean firstTime = settings.getBoolean(FIRST_TIME_KEY, true);
                String lastShowDate = settings.getString(LAST_SHOW_DATE_KEY, "");

                // Mevcut tarih
                String currentDate = new SimpleDateFormat("dd MMMM yyyy", new Locale(selectedLanguage)).format(new Date());

                if (firstTime) {
                    // Uygulama ilk kez kurulduğunda
                    readThankYouMessage(); // Teşekkür mesajı göster
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(FIRST_TIME_KEY, false);
                    editor.putString(LAST_SHOW_DATE_KEY, currentDate); // Tarihi güncelle
                    editor.apply();
                } else if (!lastShowDate.equals(currentDate)) {
                    // Gün değiştiğinde
                    readRandomTextAndDate(); // TextToSpeech başlatıldığında metni ve tarihi okuma.
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(LAST_SHOW_DATE_KEY, currentDate); // Tarihi güncelle
                    editor.apply();
                }
            }
        } else {
            Log.e(TAG, "TextToSpeech başlatma başarısız."); // Hata kaydı eklendi
        }
    }

    private void readThankYouMessage() {
        String toSpeak = getString(R.string.thank_you_message);
        Log.d(TAG, "Konuşulacak metin: " + toSpeak); // Konuşulacak metin log kaydı
        int speakStatus = tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
        if (speakStatus == TextToSpeech.ERROR) {
            Log.e(TAG, "Konuşma hatası meydana geldi."); // Konuşma hatası durumunu kontrol eder ve loglar
        }
    }

    private void readRandomTextAndDate() {
        String[] texts = {
                getString(R.string.welcome_message_1),
                getString(R.string.welcome_message_2),
                getString(R.string.welcome_message_3),
                getString(R.string.welcome_message_4),
                getString(R.string.welcome_message_5),
                getString(R.string.welcome_message_6),
                getString(R.string.welcome_message_7),
                getString(R.string.welcome_message_8),
                getString(R.string.welcome_message_9),
                getString(R.string.welcome_message_10),
                getString(R.string.welcome_message_11),
                getString(R.string.welcome_message_12),
                getString(R.string.welcome_message_13),
                getString(R.string.welcome_message_14),
                getString(R.string.welcome_message_15),
                getString(R.string.welcome_message_16),
                getString(R.string.welcome_message_17),
                getString(R.string.welcome_message_18),
                getString(R.string.welcome_message_19),
                getString(R.string.welcome_message_20),
                getString(R.string.welcome_message_21),
                getString(R.string.welcome_message_22),
                getString(R.string.welcome_message_23),
                getString(R.string.welcome_message_24),
                getString(R.string.welcome_message_25),
                getString(R.string.welcome_message_26),
                getString(R.string.welcome_message_27),
                getString(R.string.welcome_message_28),
                getString(R.string.welcome_message_29),
                getString(R.string.welcome_message_30)
        };
        String randomText = texts[new Random().nextInt(texts.length)];
        String currentDate = new SimpleDateFormat("dd MMMM yyyy", new Locale(selectedLanguage)).format(new Date());
        String toSpeak = getString(R.string.welcome_message_0) + " " + currentDate + "   " + randomText;

        // Noktalama işaretlerini kaldırma
        toSpeak = toSpeak.replaceAll("[.,]", "");

        Log.d(TAG, "Konuşulacak metin: " + toSpeak); // Konuşulacak metin log kaydı
        int speakStatus = tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null); // Rastgele metin ve tarih okundu.
        if (speakStatus == TextToSpeech.ERROR) {
            Log.e(TAG, "Konuşma hatası meydana geldi."); // Konuşma hatası durumunu kontrol eder ve loglar
        }
    }

    @Override
    protected void onPause() {
        if (tts != null) {
            tts.stop();
        }
        super.onPause();
        if (mFirebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString("state", "onPause");
            mFirebaseAnalytics.logEvent("app_state_change", bundle);

        }
        AdHelper.pauseBannerAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseAnalytics != null) {
            mFirebaseAnalytics.logEvent("onResume", null);
        }
        boolean nowAdFree = PremiumManager.getInstance(this).isAdFree();
        if (nowAdFree != previousAdFree) {
            previousAdFree = nowAdFree;
            AdHelper.destroyBannerAd();
            setupRecyclerView(localeManager.updateResources(this, localeManager.getLanguage()));
        }
        refreshMainPremiumEntry();
        AdHelper.resumeBannerAd();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown(); // TextToSpeech durduruldu ve kapatıldı.
        }
        super.onDestroy();
        if (mFirebaseAnalytics != null) {
            mFirebaseAnalytics.logEvent("onDestroy", null);
        }
        AdHelper.destroyBannerAd();
    }

    private void setupRecyclerView(Context context) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_1);

        recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                holder.itemView.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.itemView.setAlpha(1);
                    }
                }).start();
                return super.animateRemove(holder);
            }

            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {
                holder.itemView.setAlpha(0);
                holder.itemView.animate().alpha(1).setDuration(500).start();
                return super.animateAdd(holder);
            }
        });

        String[] topics_main = context.getResources().getStringArray(R.array.main_topics);
        Adapter_MainActivity adapter = new Adapter_MainActivity(context, topics_main);
        int columns = NtHelper.listColumnCount(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == Adapter_MainActivity.TYPE_AD) {
                    return columns;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    private void updateUIComponents(int themeId) {
        ThemeColors themeColor = themeManager.getThemeColors();

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.toolbar_title);
        TextView tvToolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        ConstraintLayout constraintLayout3 = findViewById(R.id.constraintlayout_2);

        toolbar.setBackgroundColor(themeColor.toolbarBackgroundColor);
        tvToolbarTitle.setTextColor(themeColor.toolbarTitleTextColor);
        tvToolbarSubtitle.setTextColor(themeColor.toolbarSubtitleTextColor);
        constraintLayout3.setBackgroundColor(themeColor.activityBackgroundColor);
        refreshMainPremiumEntry();
    }

    /** Ana sayfa altındaki reklamsız / premium bilgi sayfası girişi. */
    private void refreshMainPremiumEntry() {
        AppCompatButton btn = findViewById(R.id.btn_main_premium_entry);
        if (btn == null) {
            return;
        }
        ThemeColors c = themeManager.getThemeColors();
        if (PremiumManager.getInstance(this).isAdFree()) {
            btn.setVisibility(View.GONE);
            return;
        }
        btn.setVisibility(View.VISIBLE);
        btn.setText(R.string.main_premium_entry_button);
        btn.setBackgroundColor(c.toolbarBackgroundColor);
        btn.setTextColor(c.activityTextColor);
    }

    private void setupButtons(Context context) {
        ImageButton btnSettings = findViewById(R.id.btn_settings);
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnShare = findViewById(R.id.btn_share);
        AppCompatButton btnPremiumEntry = findViewById(R.id.btn_main_premium_entry);

        setButtonClickListener(btnSettings, context, Ayarlar.class);
        setButtonClickListener(btnHome, context, MainActivity.class);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> NtHelper.shareText(this));
        }
        if (btnPremiumEntry != null) {
            btnPremiumEntry.setOnClickListener(v -> NtHelper.startActivity(this, PremiumActivity.class));
        }
    }

    private void setButtonClickListener(ImageButton button, Context context, Class<?> targetActivity) {
        if (button != null) {
            button.setOnClickListener(v -> NtHelper.startActivity(this, targetActivity));
        }
    }
}
