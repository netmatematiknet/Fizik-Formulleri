package com.mobilprogramlar.FizikFormullerim;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GizlilikPolitikasi extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gizlilik_politikasi);
        KenarHelper.apply(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.gizlilik_politikasi);
        toolbar.setNavigationOnClickListener(v -> finish());

        WebView webView = findViewById(R.id.webview_gizlilik);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl("file:///android_asset/gizlilik-politikasi.html");
    }
}
