package com.mobilprogramlar.FizikFormullerim;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FormulGoster extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulgoster);
        KenarHelper.apply(this);

        FirebaseAnalytics.getInstance(this).logEvent("formul_goster", new Bundle());

        FrameLayout adContainer = findViewById(R.id.ad_banner_container);
        if (AdsConsentHelper.canRequestAds(this)) {
            AdsConsentHelper.initializeAds(this);
            AdManager.loadAdaptiveBannerAd(this, adContainer);
        }

        TextView pageIndicator = findViewById(R.id.tv_page_indicator);
        ExtendedViewPager pager = findViewById(R.id.view_pager);

        int[] images = EEPROM.list;
        String[] titles = EEPROM.listString;
        if (images == null || images.length == 0) {
            Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String title = (titles != null && titles.length > 0 && titles[0] != null)
                ? titles[0]
                : getIntent().getStringExtra("title");
        if (title == null) {
            title = "";
        }

        FormulaPagerHelper.bind(pager, pageIndicator, images, title);

        FloatingActionButton fabBack = findViewById(R.id.fab_back);
        fabBack.setOnClickListener(view -> finish());

        FloatingActionButton fabInfo = findViewById(R.id.fab_info);
        fabInfo.setOnClickListener(view ->
                Toast.makeText(this, R.string.formul_bilgi_mesaj, Toast.LENGTH_LONG).show());
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
    }
}
