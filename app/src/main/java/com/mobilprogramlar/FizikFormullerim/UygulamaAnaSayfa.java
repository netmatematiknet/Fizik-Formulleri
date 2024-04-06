package com.mobilprogramlar.FizikFormullerim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;

public class UygulamaAnaSayfa extends AppCompatActivity {
    Button btn1, btn2;
    private AdView mAdView_banner1, mAdView_banner2;    //BANNER Reklamı
    private InterstitialAd mInterstitialAd;     //Geçiş Reklamı
    private final Boolean euConsent = false;    //Geçiş Reklamı

    private static final String TAG = "UygulamaAnasayfa";

    //Google Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    //Google Analytics

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uygulama_anasayfa);

        toolbarCagir();

        //GEÇİŞ REKLAMI
        boolean reklamOlsunmu = new NURfonksiyonlar().reklamGoster(getApplicationContext(),50);
        //GEÇİŞ REKLAMI


        // AdMob SDK'nın başlatılması
        MobileAds.initialize(this, initializationStatus -> {
            if(euConsent){      //Geçiş Reklamı
                //Kullanıcı Onaylı
                createPersonalizedAd();     //Geçiş Reklamı
            }else{  //Geçiş Reklamı
                //Kullanıcı izin vermesi
                //Kullanıcı AB dışında
                createNonPersonalizedAd();  //Geçiş Reklamı
            }   //Geçiş Reklamı
        });

    //BANNER REKLAM
        mAdView_banner1 = findViewById(R.id.adView1);
        mAdView_banner2 = findViewById(R.id.adView2);
        AdRequest adRequest_banner = new AdRequest.Builder().build();
        mAdView_banner1.loadAd(adRequest_banner);
        mAdView_banner2.loadAd(adRequest_banner);
    //BANNER REKLAM


        // Geri tuşuna basıldığında geri gitme işlemi
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent geriButonu = new Intent(getApplicationContext(), UygulamaAnaSayfa.class);
                NavUtils.navigateUpTo(UygulamaAnaSayfa.this, geriButonu);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);




        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);

        btn1.setOnClickListener(v -> {
            //GEÇİŞ REKLAMI
            if (mInterstitialAd != null&& reklamOlsunmu) {
                mInterstitialAd.show(UygulamaAnaSayfa.this);
            }
            //GEÇİŞ REKLAMI
            Intent intent = new Intent(getApplicationContext(),FormullerFizik.class);
            startActivity(intent);
        });
        btn2.setOnClickListener(v -> {
            //GEÇİŞ REKLAMI
            if (mInterstitialAd != null) {
                mInterstitialAd.show(UygulamaAnaSayfa.this);
            }
            //GEÇİŞ REKLAMI
            Intent i4 = new Intent(getApplicationContext(), UygulamaUygulamalar.class);
            startActivity(i4);
        });

    }




    // isTaskRoot() metoduyla uygulamanın o an başlangıç kısmında olup olmadığını kontrol ediyor true false değer dönderen
    // bu metod ile eğer uygulamanın başlangıç aktivitesindeyseniz size uygulamadan çıkmak istediğinize eminmisiniz şeklinde bir soru sormaktadır.
    // Geri butonu ile çıkışı kontrol etme
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //geri butonunu yakalıyoruz
        if(keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
            //Programdan çıkmak isteyip istemediğini soruyoruz
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("ÇIK")
                    .setMessage("çıkacakmısınız")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        finish();                        //Aktiviteyi durduruyoruz
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }


    // DEĞİŞTİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİ
    //TOOLBAR MENÜ - TOOLBAR MENÜ
    // Toolbar oluşturma ve menü işlemleri
    private void toolbarCagir(){
        Toolbar toolbar;
        toolbar= findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.toolbar_anasayfa) {
                anasayfa();
            } else if (id == R.id.toolbar_kapat1) {
                kapat();
            } else if (id == R.id.toolbar_paylas) {
                paylas();
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    // Uygulamayı kapatma işlemi
    public void kapat() {
        this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);     //System.exit() yığında birden fazla etkinliğiniz varsa uygulamanızı öldürmez
        getParent().finish();
    }
    public void anasayfa(){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
    }
    public void paylas(){
        String paylasmesajbasligi = "Üç İşlem Yarışması"+"\n";
        String paylasmesaji = "Toplama, çıkarma, çarpma hepsi bu kadar mı?. \n HAYIR. Bir de zamanla yarışacaksınız.\n" +
                "Üç seviyelik bir oyun. \n 1.seviyede 1 den 10 a kadar olan sayılar kullanılıyor.\n " +
                "Yani çarpım tablosunu öğrenmek isteyen çocuklar için çok uyugun zevkli bir program.\n" +
                "Sadece çocuklar için mi zamanla yarış olduğu için Büyükler için de kendi rekorlarını kırma fırsatı.\n"+
                "Bu haliyle bu oyun hem işlem yeteneğinizi artıracak, hem dikkat ve konsantrasyon yeteneğinizi hem de Beyin-El uyumunu geliştirir.\n"+
                "2. seviye de sayılar 1 den 15 e kadar, 3. seviyede ise 1 den 20 ye kadar sayılar kullanılmakta.\n"+
                "Boş zamanlarınız için hem eğlenceli hem de matematik ve zekanızı geliştirici bir oyun." + "\n"+
                "https://play.google.com/store/apps/details?id=com.mobilprogramlar.ucislemyarismasi";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, paylasmesajbasligi);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, paylasmesaji);
        startActivity(Intent.createChooser(sharingIntent, "Paylaşmak İçin Tıklayınız "));
    }
    // Aktiviteyi kapatma işlemi
    public void cikis(){
        finish();
    }
    //PAYLAŞ


//REKLAM KONTROL
    /** Etkinlikten ayrılırken çağrıldı */
    @Override
    public void onPause() {
        if (mAdView_banner1!=null||mAdView_banner2!=null) {
            assert mAdView_banner1 != null;
            mAdView_banner1.pause();
            mAdView_banner2.pause();
        }
        super.onPause();
    }
    /** Etkinliğe geri döndüğünüzde çağrıldı */
    @Override
    public void onResume() {
        if (mAdView_banner1!=null||mAdView_banner2!=null) {
            assert mAdView_banner1 != null;
            mAdView_banner1.pause();
            mAdView_banner2.pause();
        }
        super.onResume();
    }
    /** Etkinlik yok edilmeden önce çağrılır */
    @Override
    public void onDestroy() {
        if (mAdView_banner1!=null||mAdView_banner2!=null) {
            assert mAdView_banner1 != null;
            mAdView_banner1.pause();
            mAdView_banner2.pause();
        }
        super.onDestroy();
    }
//REKLAM KONTROL






    //GEÇİŞ REKLAMI
    private  void createPersonalizedAd(){
        Log.d("---Admob", "KişiselleştirilmişReklam İsteği\n");
        // Kişiselleştirilmiş reklam oluşturma
        AdRequest adRequest = new AdRequest.Builder().build();
        createInterstitialAd(adRequest);
    }
    private  void createNonPersonalizedAd(){
        Log.d("---Admob", "Kişiselleştirilmemiş Reklam İsteği\n");
        // Kişiselleştirilmemiş reklam oluşturma
        Bundle networkExtrasBundle = new Bundle();
        networkExtrasBundle.putInt("rdp", 1);
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, networkExtrasBundle)
                .build();
        createInterstitialAd(adRequest);
    }
    private void createInterstitialAd(AdRequest adRequest){
        // Geçiş reklamı oluşturma
        InterstitialAd.load(this,getString(R.string.interstitial_gecis_gecerli_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.d("---Admob", "onAdLoaded");
                //Toast.makeText(getApplicationContext(), "onAdLoaded() GÖSTERİLDİ", Toast.LENGTH_SHORT).show();

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Reklam kapatıldığında yapılacak işlemler
                        // Tam ekran içeriği kapatıldığında çağrılır.
                        Log.d("TAG", "Reklam reddedildi.");
                        //Intent intent = new Intent(Turev_Uygulama_Icerigi.this, UygulamaUrunlerimiz.class);
                        //startActivity(intent);
                        if(euConsent){      //Geçiş Reklamı
                            //Kullanıcı Onaylı
                            createPersonalizedAd();     //Geçiş Reklamı
                        }else{  //Geçiş Reklamı
                            //Kullanıcı izin vermesi
                            //Kullanıcı EU'de değil
                            createNonPersonalizedAd();  //Geçiş Reklamı
                        }   //Geçiş Reklamı
                    }
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Tam ekran içerik gösterilemediğinde çağrılır.
                        // Reklam gösterilemediğinde yapılacak işlemler
                        Log.d("TAG", "Reklam gösterilemedi.");
                    }
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Tam ekran içerik gösterildiğinde çağrılır.
                        // Referansınızı ikinci kez göstermemek için null olarak ayarladığınızdan emin olun.
                        // Reklam gösterildiğinde yapılacak işlemler
                        mInterstitialAd = null;
                        Log.d("TAG", "Reklam gösterildi.");
                    }
                });
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Hatayı ele alın
                // Reklam yüklenemediğinde yapılacak işlemler
                Log.d("Reklam yüklenemedi: ", loadAdError.getMessage());
                //Toast.makeText(getApplicationContext(), "Reklam yüklenemedi:: "+loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                mInterstitialAd = null;
            }
        });
    }
    //GEÇİŞ REKLAMI



}