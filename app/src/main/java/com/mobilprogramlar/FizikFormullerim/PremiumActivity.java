package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

/**
 * Reklamsız (Premium) satın alma bilgisi ve CTA tek ekranda.
 */
public class PremiumActivity extends AppCompatActivity implements BillingManager.Listener {

    private ThemeManager themeManager;
    private AppCompatButton btnPurchase;
    private AppCompatButton btnRestore;
    private TextView tvActiveNote;

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

        LocaleManager localeManager = new LocaleManager(this);
        Context context = localeManager.updateResources(this, localeManager.getLanguage());

        NtHelper.enableEdgeToEdge(this);
        setContentView(R.layout.activity_premium);
        NtHelper.applySystemBarInsets(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = findViewById(R.id.text_container);
        if (toolbarTitle != null) {
            toolbarTitle.setText(getString(R.string.premium_page_title));
        }

        TextView headline = findViewById(R.id.tv_premium_headline);
        TextView intro = findViewById(R.id.tv_premium_intro);
        TextView b1 = findViewById(R.id.tv_premium_b1);
        TextView b2 = findViewById(R.id.tv_premium_b2);
        TextView b3 = findViewById(R.id.tv_premium_b3);
        tvActiveNote = findViewById(R.id.tv_premium_active_note);
        btnPurchase = findViewById(R.id.btn_premium_purchase);
        btnRestore = findViewById(R.id.btn_premium_restore_page);

        headline.setText(getString(R.string.premium_page_headline));
        intro.setText(getString(R.string.premium_page_intro));
        b1.setText(getString(R.string.premium_benefit_1));
        b2.setText(getString(R.string.premium_benefit_2));
        b3.setText(getString(R.string.premium_benefit_3));

        btnPurchase.setText(getString(R.string.premium_buy_button));
        btnRestore.setText(getString(R.string.premium_restore_button));

        BillingManager billing = ((App) getApplication()).getBillingManager();
        billing.setListener(this);
        billing.queryPurchasesAndApply();

        btnPurchase.setOnClickListener(v -> billing.launchPurchaseFlow(this));
        btnRestore.setOnClickListener(v -> {
            billing.queryPurchasesAndApply();
            Toast.makeText(this, getString(R.string.premium_restored), Toast.LENGTH_SHORT).show();
        });

        NtHelper.setOnBackPressed(this, MainActivity.class);
        setupToolbarButtons();
        applyThemeColors();
        refreshPremiumState();
    }

    /** toolbar_layout_1 içindeki Ayarlar / Paylaş / Ana Sayfa (MainActivity ile aynı davranış). */
    private void setupToolbarButtons() {
        ImageButton btnSettings = findViewById(R.id.btn_settings);
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnShare = findViewById(R.id.btn_share);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> NtHelper.startActivity(this, Ayarlar.class));
        }
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> NtHelper.startActivity(this, MainActivity.class));
        }
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> NtHelper.shareText(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((App) getApplication()).getBillingManager().queryPurchasesAndApply();
        refreshPremiumState();
    }

    @Override
    protected void onDestroy() {
        ((App) getApplication()).getBillingManager().setListener(null);
        super.onDestroy();
    }

    private void refreshPremiumState() {
        boolean adFree = PremiumManager.getInstance(this).isAdFree();
        String price = ((App) getApplication()).getBillingManager().getFormattedPrice();
        if (adFree) {
            tvActiveNote.setVisibility(View.VISIBLE);
            tvActiveNote.setText(getString(R.string.premium_page_thanks));
            btnPurchase.setVisibility(View.GONE);
            if (price != null) {
                btnPurchase.setText(getString(R.string.premium_buy_button));
            }
        } else {
            tvActiveNote.setVisibility(View.GONE);
            btnPurchase.setVisibility(View.VISIBLE);
            if (price != null) {
                btnPurchase.setText(getString(R.string.premium_buy_button) + " — " + price);
            } else {
                btnPurchase.setText(getString(R.string.premium_buy_button));
            }
        }
    }

    private void applyThemeColors() {
        ThemeColors c = themeManager.getThemeColors();
        findViewById(R.id.premium_root).setBackgroundColor(c.activityBackgroundColor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(c.toolbarBackgroundColor);
        TextView toolbarTitle = findViewById(R.id.text_container);
        if (toolbarTitle != null) {
            toolbarTitle.setTextColor(c.toolbarTitleTextColor);
        }
        int text = c.activityTextColor;
        for (int id : new int[]{
                R.id.tv_premium_headline, R.id.tv_premium_intro,
                R.id.tv_premium_b1, R.id.tv_premium_b2, R.id.tv_premium_b3, R.id.tv_premium_active_note
        }) {
            TextView tv = findViewById(id);
            if (tv != null) {
                tv.setTextColor(text);
            }
        }
        btnPurchase.setBackgroundColor(c.toolbarBackgroundColor);
        btnPurchase.setTextColor(DialogFit.contrastingOn(c.toolbarBackgroundColor));
        float d = getResources().getDisplayMetrics().density;
        GradientDrawable outline = new GradientDrawable();
        outline.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()));
        outline.setStroke(Math.max(1, Math.round(d * 2f)), c.toolbarBackgroundColor);
        outline.setColor(Color.TRANSPARENT);
        btnRestore.setBackground(outline);
        btnRestore.setTextColor(c.toolbarBackgroundColor);
    }

    @Override
    public void onPremiumStateChanged(boolean isAdFree) {
        runOnUiThread(this::refreshPremiumState);
    }

    @Override
    public void onBillingMessage(@NonNull String message) {
        runOnUiThread(() -> Toast.makeText(PremiumActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
