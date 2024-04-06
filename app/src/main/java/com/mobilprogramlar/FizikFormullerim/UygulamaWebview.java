package com.mobilprogramlar.FizikFormullerim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UygulamaWebview extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uygulama_webview);

        WebView webView = findViewById(R.id.webview1);
        ProgressBar mProgressBar = findViewById(R.id.progressBar);



        if(internetKontrol()){ //internet kontrol methodu çağırılıyor
            //Toast.makeText(getApplicationContext(), "İnternet Bağlı!", Toast.LENGTH_LONG).show();
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("https://www.ossmatematik.com/mobil-programlarimiz");
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(true);
        }else{
            Toast.makeText(getApplicationContext(), "Sayfa Yüklenemedi!\b Sayfanın açılabilmesi için İnternet Bağlantınızın olması gerekir", Toast.LENGTH_LONG).show();
        }
    }

    //@Override
    //public void onBackPressed()
    //7{
    //if-else ile web sitesinin var olup olmadığını sorgulayıp ona göre işlem tanımlıyoruz.
    //if (webView.canGoBack())
    //{
    //    webView.goBack();       // Web sayfasını geri git
    //}
    //else
    //{
    //    super.onBackPressed();      // Varsayılan geri tuşu davranışını çalıştır
    //}
    //}

    protected boolean internetKontrol() { //interneti kontrol eden method

        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        //return true;
        //}
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        // Bağlantı yok
        return nc != null && (nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED));    // Bağlantı mevcut

    }




}
