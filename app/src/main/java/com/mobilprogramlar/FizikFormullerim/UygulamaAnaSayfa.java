package com.mobilprogramlar.FizikFormullerim;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import com.google.firebase.analytics.FirebaseAnalytics;

public class UygulamaAnaSayfa extends AppCompatActivity {

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            NotificationPermissionHelper.register(this);
    private InAppUpdateHelper inAppUpdateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uygulama_anasayfa);
        KenarHelper.apply(this);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "UygulamaAnaSayfa");
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        setupToolbar();
        NotificationPermissionHelper.requestIfNeeded(this, notificationPermissionLauncher);
        inAppUpdateHelper = new InAppUpdateHelper(this);
        inAppUpdateHelper.checkForUpdate();

        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        TextView tvMessage = findViewById(R.id.tv_home_message);
        FrameLayout adContainer = findViewById(R.id.ad_banner_container);

        String homeMessage = AppRemoteConfig.getInstance(this).getHomeMessage();
        if (!homeMessage.isEmpty()) {
            tvMessage.setText(homeMessage);
            tvMessage.setVisibility(View.VISIBLE);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });

        btn1.setOnClickListener(v -> AdManager.showHomeInterstitialThen(
                UygulamaAnaSayfa.this,
                () -> startActivity(new Intent(UygulamaAnaSayfa.this, FormullerFizik.class))));
        btn2.setOnClickListener(v -> AdManager.showHomeInterstitialThen(
                UygulamaAnaSayfa.this,
                () -> NURfonksiyonlar.openDeveloperPage(UygulamaAnaSayfa.this, "mobilprogramlar.com")));

        AdsConsentHelper.gatherConsentAndInitAds(this, () -> {
            AdManager.loadAdaptiveBannerAd(this, adContainer);
            AdManager.loadInterstitialAd(this);
        });
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.cikis_baslik)
                .setMessage(R.string.cikis_mesaj)
                .setPositiveButton(R.string.evet, (dialog, which) -> finishAffinity())
                .setNegativeButton(R.string.hayir, null)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.toolbar_anasayfa) {
                return true;
            } else if (id == R.id.toolbar_kapat1) {
                finishAffinity();
            } else if (id == R.id.toolbar_paylas) {
                shareApp();
            } else if (id == R.id.toolbar_gizlilik) {
                startActivity(new Intent(this, GizlilikPolitikasi.class));
            } else if (id == R.id.toolbar_reklam_tercihleri) {
                if (AdsConsentHelper.isPrivacyOptionsRequired(this)) {
                    AdsConsentHelper.showPrivacyOptionsForm(this);
                } else {
                    startActivity(new Intent(this, GizlilikPolitikasi.class));
                }
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void shareApp() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.paylas_baslik));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.paylas_mesaj_tam));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.paylas_mesaj_3)));
    }

    @Override
    public void onPause() {
        AdManager.pauseBanners();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        AdManager.resumeBanners();
        AppRemoteConfig.getInstance(this).fetchAndActivate();
        if (inAppUpdateHelper != null) {
            inAppUpdateHelper.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (inAppUpdateHelper != null) {
            inAppUpdateHelper.unregister();
        }
        super.onDestroy();
    }
}
