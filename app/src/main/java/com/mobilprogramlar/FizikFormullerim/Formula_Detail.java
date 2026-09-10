package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class Formula_Detail extends AppCompatActivity {
    private IconManager iconManager;
    private MediaPlayer mediaPlayer;
    private int[] tabIcons;
    private int[] tabIconsActive;
    LocaleManager localeManager;
    ThemeManager themeManager;

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

        NtHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_formula_detail);
        NtHelper.applySystemBarInsets(this);

        setupToolbar(context);
        setupButtons(context);
        updateUIComponents(themeManager.getTheme());

        iconManager = new IconManager(context);

        SafeViewPager viewPager = (SafeViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabDots);
        setupViewPager(context, viewPager, tabLayout);

        NtHelper.setOnBackPressed(this, Formula_List.class);

        mediaPlayer = MediaPlayer.create(context, R.raw.page_turn);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            Log.e("MediaPlayer", "Ses dosyası yüklenemedi");
        }

        InAppReviewHelper.onFormulaDetailOpened(this);
    }

    // Toolbar ayarları. Toolbarı belirler ve başlık, alt başlık ayarlamalarını yapar.
    private void setupToolbar(Context context) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        TextView toolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        if (toolbarTitle != null) {
            toolbarTitle.setText(context.getResources().getString(R.string.toolbar_baslik_default));
        } else {
            Log.d("MainActivity", "toolbarTitle BOŞ ");
        }
        if (toolbarSubtitle != null) {
            //toolbarSubtitle.setText(context.getResources().getString(R.string.toolbar_altbaslik_default));
            toolbarSubtitle.setText(getIntent().getStringExtra("formula_title"));

        } else {
            Log.d("MainActivity", "toolbarSubtitle BOŞ ");
        }
    }

    // ViewPager ve TabLayout'ın kurulumunu yapan metod.
    private void setupViewPager(Context context, SafeViewPager viewPager, TabLayout tabLayout) {
        String category = getIntent().getStringExtra("formula_category");
        String topic = getIntent().getStringExtra("formula_title");

        int[] imageResources = iconManager.getDrawableIdsForTopic(topic);
        ViewPagerAdapter adapter = new ViewPagerAdapter(context, imageResources);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager, true);  // TabLayout ile ViewPager bağlanır.

        tabIcons = IconManager.getTabIcons();
        tabIconsActive = IconManager.getTabIconsActive();
        final int iconCount = tabIcons.length;

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null || iconCount == 0) {
                continue;
            }
            View customTab = LayoutInflater.from(context).inflate(R.layout.custom_tab, tabLayout, false);
            ImageView iconView = customTab.findViewById(R.id.tab_icon);
            iconView.setImageResource(tabIcons[i % iconCount]);
            tab.setCustomView(customTab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }

                int position = tab.getPosition();
                View custom = tab.getCustomView();
                if (custom == null || iconCount == 0) {
                    RandomEffectApplier.applyRandomEffect(viewPager);
                    return;
                }
                ImageView iconView = custom.findViewById(R.id.tab_icon);
                if (iconView != null) {
                    iconView.setImageResource(tabIconsActive[position % iconCount]);
                    iconView.animate().rotationY(360f).scaleX(1.4f).scaleY(1.43f).setDuration(500)
                            .withEndAction(() -> iconView.setRotationX(0))
                            .start();
                } else {
                    Log.e("TabAnimation", getString(R.string.hata_mesaji_1));
                }

                RandomEffectApplier.applyRandomEffect(viewPager);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                View custom = tab.getCustomView();
                if (custom == null || iconCount == 0) {
                    return;
                }
                ImageView iconView = custom.findViewById(R.id.tab_icon);
                if (iconView != null) {
                    iconView.setImageResource(tabIcons[position % iconCount]);
                    iconView.animate().rotationX(360f).scaleX(1f).scaleY(1f).setDuration(500)
                            .withEndAction(() -> iconView.setRotationY(0)).start();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Butonları ayarla
    private void setupButtons(Context context) {
        View back = findViewById(R.id.btn_back);
        if (back != null) {
            back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }
        findViewById(R.id.btn_home).setOnClickListener(v -> NtHelper.startActivity(context, MainActivity.class));
        findViewById(R.id.btn_share).setOnClickListener(v -> NtHelper.shareText(this));
    }

    // Arayüz bileşenlerini güncellemek için
    private void updateUIComponents(int themeId) {
        ThemeColors themeColor = themeManager.getThemeColors();

        // Bileşenlerin tanımlanması
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.toolbar_title);
        TextView tvToolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        ConstraintLayout constraintLayout3 = findViewById(R.id.constraintlayout_4);
        TabLayout tabLayout = findViewById(R.id.tabDots);

        // Renklerin atanması
        toolbar.setBackgroundColor(themeColor.toolbarBackgroundColor);
        tvToolbarTitle.setTextColor(themeColor.toolbarTitleTextColor);
        tvToolbarSubtitle.setTextColor(themeColor.toolbarSubtitleTextColor);
        constraintLayout3.setBackgroundColor(themeColor.activityBackgroundColor);
        tabLayout.setBackgroundColor(themeColor.cardBackgroundColor);
    }
}