package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Arrays;

public class Formula_List extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_INDEX = "category_index";

    private RecyclerView recyclerView;
    private ThemeManager themeManager;
    private FirebaseAnalytics firebaseAnalytics;
    String category;
    private int categoryIndex = -1;

    @Override
    protected void attachBaseContext(Context newBase) {
        LocaleManager localeManager = new LocaleManager(newBase);
        Context context = localeManager.setLocale(newBase);
        super.attachBaseContext(context);
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        Log.d("Formula_List", "Base context updated with locale: " + lang);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.applyTheme(this);
        super.onCreate(savedInstanceState);

        NtHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_formula_list);
        NtHelper.applySystemBarInsets(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle screenParams = new Bundle();
        screenParams.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Formula_List");
        screenParams.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Formula_List");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, screenParams);

        // Toolbar ayarları
        setupToolbar();

        AdHelper.loadInterstitialAd(this);
        AdHelper.showAdWithProbability(this, AppRemoteConfig.getInstance(this).getInterstitialFormulaPercent());

        category = getIntent().getStringExtra("category");
        categoryIndex = getIntent().getIntExtra(EXTRA_CATEGORY_INDEX, -1);
        //Toast.makeText(this, "category: " + category, Toast.LENGTH_SHORT).show();

        setupButtons();

        // Seçili temayı alıp güncellemek için `updateUIComponents` çağrılır
        updateUIComponents(themeManager.getTheme());

        // Geri butonu için callback oluşturulur, geri butonuna basıldığında ne olacağını tanımlar.
        NtHelper.setOnBackPressed(this, MainActivity.class);

        recyclerView = findViewById(R.id.recyclerView_1);
        int themeID = themeManager.getTheme();
        setupRecyclerView(category, themeID);

        // Dil değişikliği olduğunda aktiviteyi yeniden yaratmak için dinleyici ekleyin
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferencesChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(sharedPreferencesChangeListener);
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesChangeListener = (sharedPreferences, key) -> {
        if ("language_key".equals(key)) {
            recreate(); // Yeniden yaratmayı tetikle
        }
    };

    private void setupRecyclerView(String category, int themeID) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, NtHelper.listColumnCount(this)));

        if (category == null) {
            //Toast.makeText(this, "Kategori bilgisi bulunamadı!", Toast.LENGTH_SHORT).show();
            finish(); // Kategori yoksa aktiviteyi kapat
            return;
        }

        String[] konu_dizisi;
        if (CategoryHelper.isApplications(this, category) || CategoryHelper.isApplicationsIndex(categoryIndex)) {
            finish();
            return;
        } else {
            konu_dizisi = IconManager.physicsTopicTitles();
        }
        Log.d("FormulaListActivity", "konu_dizisi loaded: " + Arrays.toString(konu_dizisi));

        if (konu_dizisi.length == 0) {
            Log.e("FormulaListActivity", "No topics for category: " + category);
            return;
        }

        Adapter_Formula_List adapter = new Adapter_Formula_List(this, konu_dizisi, category, themeID);
        recyclerView.setAdapter(adapter);

        // LayoutAnimation ekle
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    /** Dil bağımsız: önce {@link #EXTRA_CATEGORY_INDEX}, yoksa yerelleştirilmiş başlık eşlemesi. */
    private int getTopicArrayId(String category, int indexFromIntent) {
        if (indexFromIntent == CategoryHelper.INDEX_PHYSICS) {
            return R.array.topics_math;
        }
        if (indexFromIntent == CategoryHelper.INDEX_APPLICATIONS) {
            return R.array.topics_applications;
        }
        String[] mainTopics = getResources().getStringArray(R.array.main_topics);
        if (category != null && mainTopics.length > 0 && category.equals(mainTopics[0])) {
            return R.array.topics_math;
        } else if (CategoryHelper.isApplications(this, category)) {
            return R.array.topics_applications;
        } else {
            Log.e("FormulaListActivity", getString(R.string.hata_mesaji_4) + category);
            return 0;
        }
    }

    private void updateUIComponents(int themeId) {
        ThemeColors themeColor = themeManager.getThemeColors();

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.toolbar_title);
        TextView tvToolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        ConstraintLayout constraintLayout3 = findViewById(R.id.constraintlayout_3);

        toolbar.setBackgroundColor(themeColor.toolbarBackgroundColor);
        tvToolbarTitle.setTextColor(themeColor.toolbarTitleTextColor);
        tvToolbarSubtitle.setTextColor(themeColor.toolbarSubtitleTextColor);
        constraintLayout3.setBackgroundColor(themeColor.activityBackgroundColor);
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
    }

    private void setButtonClickListener(ImageButton button, Class<?> targetActivity) {
        if (button != null) {
            button.setOnClickListener(v -> NtHelper.startActivity(this, targetActivity));
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        TextView toolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        if (toolbarTitle != null) {
            String category = getIntent().getStringExtra("category");
            if (CategoryHelper.isApplicationsIndex(categoryIndex)
                    || CategoryHelper.isApplications(this, category)) {
                toolbarTitle.setText(R.string.toolbar_baslik_uygulamalarimiz);
            } else {
                toolbarTitle.setText(R.string.toolbar_baslik_default);
            }
        } else {
            Log.d("Formula_List", "toolbarTitle BOŞ ");
        }

        if (toolbarSubtitle != null) {
            //toolbarSubtitle.setText(getResources().getString(R.string.toolbar_altbaslik_default));
            toolbarSubtitle.setText(getIntent().getStringExtra("category"));

        } else {
            Log.d("Formula_List", "toolbarSubtitle BOŞ ");
        }
    }
}
